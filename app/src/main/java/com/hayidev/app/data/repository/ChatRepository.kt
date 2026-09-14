package com.hayidev.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hayidev.app.data.model.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val roomsCollection = firestore.collection("chat_rooms")
    private val messagesCollection = firestore.collection("messages")

    suspend fun getChatRooms(): List<ChatRoom> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = roomsCollection
                .whereArrayContains("participants", uid)
                .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.toObjects(ChatRoom::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getOrCreateChatRoom(otherUserId: String): ChatRoom {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")

        // Mevcut oda var mı kontrol et
        val existingRoom = roomsCollection
            .whereArrayContains("participants", uid)
            .get()
            .await()
            .toObjects(ChatRoom::class.java)
            .find { otherUserId in it.participants }

        if (existingRoom != null) return existingRoom

        // Yeni oda oluştur
        val roomId = roomsCollection.document().id
        val room = ChatRoom(
            roomId = roomId,
            participants = listOf(uid, otherUserId),
            isMatch = true,
            matchTime = com.google.firebase.Timestamp.now()
        )
        roomsCollection.document(roomId).set(room).await()
        return room
    }

    suspend fun getMessages(roomId: String, limit: Long = 50): List<Message> {
        return try {
            val snapshot = messagesCollection
                .whereEqualTo("roomId", roomId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            snapshot.toObjects(Message::class.java).reversed()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun sendMessage(roomId: String, message: Message) {
        val messageId = messagesCollection.document().id
        val msg = message.copy(messageId = messageId)

        messagesCollection.document(messageId).set(msg).await()

        // Oda son mesajını güncelle
        roomsCollection.document(roomId).update(
            mapOf(
                "lastMessage" to message.content,
                "lastMessageTime" to com.google.firebase.Timestamp.now()
            )
        ).await()
    }

    suspend fun markAsRead(roomId: String) {
        val uid = auth.currentUser?.uid ?: return
        roomsCollection.document(roomId)
            .update("unreadCount.$uid", 0)
            .await()
    }

    suspend fun deleteChatHistory(roomId: String) {
        val snapshot = messagesCollection
            .whereEqualTo("roomId", roomId)
            .get()
            .await()

        val batch = firestore.batch()
        snapshot.documents.forEach { doc ->
            batch.delete(doc.reference)
        }
        batch.commit().await()
    }

    suspend fun unmatch(roomId: String) {
        val room = roomsCollection.document(roomId).get().await()
            .toObject(ChatRoom::class.java) ?: return

        // Her iki kullanıcının da matches listesinden çıkar
        for (participantId in room.participants) {
            val otherId = room.participants.first { it != participantId }
            firestore.collection("users").document(participantId)
                .update("matches", com.google.firebase.firestore.FieldValue.arrayRemove(otherId))
                .await()
        }

        // Odayı sil
        roomsCollection.document(roomId).delete().await()
    }
}
