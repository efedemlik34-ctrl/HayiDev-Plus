package com.hayidev.app.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class ConversationInfo(
    val id: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false
)

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val content: String = "",
    val type: String = "text",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Singleton
class RongCloudService @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun sendMessage(
        roomId: String,
        senderId: String,
        content: String,
        type: String = "text"
    ): Boolean {
        return try {
            val message = hashMapOf(
                "senderId" to senderId,
                "content" to content,
                "type" to type,
                "timestamp" to System.currentTimeMillis(),
                "isRead" to false
            )
            firestore.collection("chats").document(roomId)
                .collection("messages").add(message).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getConversations(userId: String): List<ConversationInfo> {
        return try {
            val result = firestore.collection("chats")
                .whereArrayContains("participants", userId)
                .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                .get().await()
            result.documents.map { doc ->
                ConversationInfo(
                    id = doc.id,
                    name = doc.getString("name") ?: "",
                    avatarUrl = doc.getString("avatarUrl") ?: "",
                    lastMessage = doc.getString("lastMessage") ?: "",
                    lastMessageTime = doc.getLong("lastMessageTime") ?: 0L,
                    unreadCount = (doc.getLong("unreadCount") ?: 0L).toInt()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMessages(roomId: String, limit: Long = 50): List<ChatMessage> {
        return try {
            val result = firestore.collection("chats").document(roomId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get().await()
            result.documents.map { doc ->
                ChatMessage(
                    id = doc.id,
                    senderId = doc.getString("senderId") ?: "",
                    content = doc.getString("content") ?: "",
                    type = doc.getString("type") ?: "text",
                    timestamp = doc.getLong("timestamp") ?: 0L,
                    isRead = doc.getBoolean("isRead") ?: false
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
