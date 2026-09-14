package com.hayidev.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hayidev.app.data.model.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiftRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val giftsCollection = firestore.collection("gifts")
    private val transactionsCollection = firestore.collection("gift_transactions")

    suspend fun getGifts(): List<Gift> {
        return try {
            val snapshot = giftsCollection
                .orderBy("category")
                .get()
                .await()
            snapshot.toObjects(Gift::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getGiftsByCategory(category: GiftCategory): List<Gift> {
        return try {
            val snapshot = giftsCollection
                .whereEqualTo("category", category)
                .get()
                .await()
            snapshot.toObjects(Gift::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun sendGift(
        receiverId: String,
        gift: Gift,
        count: Int = 1,
        roomId: String? = null
    ): Result<GiftTransaction> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
        return try {
            val userDoc = firestore.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java)
                ?: return Result.failure(Exception("User not found"))

            val totalCost = gift.price * count
            val currencyField = if (gift.currency == Currency.COIN) "coins" else "diamonds"
            val userBalance = if (gift.currency == Currency.COIN) user.coins else user.diamonds

            if (userBalance < totalCost) {
                return Result.failure(Exception("Yetersiz bakiye"))
            }

            val transactionId = transactionsCollection.document().id
            val transaction = GiftTransaction(
                transactionId = transactionId,
                senderId = uid,
                receiverId = receiverId,
                roomId = roomId ?: "",
                gift = gift,
                count = count
            )

            transactionsCollection.document(transactionId).set(transaction).await()

            firestore.collection("users").document(uid)
                .update(currencyField, com.google.firebase.firestore.FieldValue.increment(-totalCost.toLong()))
                .await()
            firestore.collection("users").document(receiverId)
                .update(currencyField, com.google.firebase.firestore.FieldValue.increment(totalCost.toLong()))
                .await()

            Result.success(transaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistory(limit: Long = 50): List<GiftTransaction> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val sentSnapshot = transactionsCollection
                .whereEqualTo("senderId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            val receivedSnapshot = transactionsCollection
                .whereEqualTo("receiverId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            val allTransactions = sentSnapshot.toObjects(GiftTransaction::class.java) +
                    receivedSnapshot.toObjects(GiftTransaction::class.java)

            allTransactions.sortedByDescending { it.timestamp }
                .take(limit.toInt())
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getSentGifts(userId: String, limit: Long = 50): List<GiftTransaction> {
        return try {
            val snapshot = transactionsCollection
                .whereEqualTo("senderId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            snapshot.toObjects(GiftTransaction::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getReceivedGifts(userId: String, limit: Long = 50): List<GiftTransaction> {
        return try {
            val snapshot = transactionsCollection
                .whereEqualTo("receiverId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            snapshot.toObjects(GiftTransaction::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
