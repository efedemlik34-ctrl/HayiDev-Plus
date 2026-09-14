package com.hayidev.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hayidev.app.data.model.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val roomsCollection = firestore.collection("live_rooms")
    private val giftsCollection = firestore.collection("gift_transactions")

    suspend fun createRoom(room: LiveRoom): LiveRoom {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val roomId = roomsCollection.document().id
        val newRoom = room.copy(roomId = roomId, hostId = uid, isLive = true)
        roomsCollection.document(roomId).set(newRoom).await()
        return newRoom
    }

    suspend fun joinRoom(roomId: String): Result<Unit> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
        return try {
            val doc = roomsCollection.document(roomId).get().await()
            val room = doc.toObject(LiveRoom::class.java)
                ?: return Result.failure(Exception("Room not found"))

            val user = firestore.collection("users").document(uid).get().await()
                .toObject(User::class.java)
                ?: return Result.failure(Exception("User not found"))

            val guest = Guest(
                userId = uid,
                username = user.username,
                photoUrl = user.photoUrl
            )

            roomsCollection.document(roomId).update(
                mapOf(
                    "guests" to com.google.firebase.firestore.FieldValue.arrayUnion(guest),
                    "guestCount" to (room.guestCount + 1)
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun leaveRoom(roomId: String): Result<Unit> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
        return try {
            val doc = roomsCollection.document(roomId).get().await()
            val room = doc.toObject(LiveRoom::class.java)
                ?: return Result.failure(Exception("Room not found"))

            if (room.hostId == uid) {
                roomsCollection.document(roomId).update("isLive", false).await()
            } else {
                val updatedGuests = room.guests.filter { it.userId != uid }
                roomsCollection.document(roomId).update(
                    mapOf(
                        "guests" to updatedGuests,
                        "guestCount" to maxOf(0, room.guestCount - 1)
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRooms(
        limit: Long = 20,
        type: RoomType? = null
    ): List<LiveRoom> {
        return try {
            var query: Query = roomsCollection.whereEqualTo("isLive", true)
            type?.let { query = query.whereEqualTo("type", it) }
            val snapshot = query
                .orderBy("viewerCount", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            snapshot.toObjects(LiveRoom::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRoomById(roomId: String): LiveRoom? {
        return try {
            roomsCollection.document(roomId).get().await()
                .toObject(LiveRoom::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun sendGift(
        roomId: String,
        gift: Gift,
        count: Int = 1
    ): Result<GiftTransaction> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
        return try {
            val transactionId = giftsCollection.document().id
            val room = getRoomById(roomId)
                ?: return Result.failure(Exception("Room not found"))

            val transaction = GiftTransaction(
                transactionId = transactionId,
                senderId = uid,
                receiverId = room.hostId,
                roomId = roomId,
                gift = gift,
                count = count
            )

            giftsCollection.document(transactionId).set(transaction).await()

            val cost = gift.price * count
            val currencyField = if (gift.currency == Currency.COIN) "coins" else "diamonds"
            firestore.collection("users").document(uid)
                .update(currencyField, com.google.firebase.firestore.FieldValue.increment(-cost.toLong()))
                .await()
            firestore.collection("users").document(room.hostId)
                .update(currencyField, com.google.firebase.firestore.FieldValue.increment(cost.toLong()))
                .await()

            Result.success(transaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun likeRoom(roomId: String): Result<Unit> {
        return try {
            roomsCollection.document(roomId)
                .update("likeCount", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun endRoom(roomId: String): Result<Unit> {
        return try {
            roomsCollection.document(roomId)
                .update("isLive", false)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
