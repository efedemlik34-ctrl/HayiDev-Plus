package com.hayidev.app.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hayidev.app.data.model.ChatMessage
import com.hayidev.app.data.model.ChatMessageType
import com.hayidev.app.data.model.User
import com.hayidev.app.data.service.AnalyticsService
import com.hayidev.app.data.service.RongCloudService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendMessageUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val rongCloudService: RongCloudService,
    private val analyticsService: AnalyticsService
) {
    data class SendMessageResult(
        val success: Boolean,
        val message: ChatMessage? = null,
        val error: String? = null
    )

    suspend fun sendTextMessage(
        chatId: String,
        receiverId: String,
        content: String
    ): SendMessageResult {
        val uid = auth.currentUser?.uid ?: return SendMessageResult(false, error = "Not logged in")
        return try {
            val user = firestore.collection("users").document(uid).get().await()
                .toObject(User::class.java)

            val message = ChatMessage.createTextMessage(
                chatId = chatId,
                senderId = uid,
                senderName = user?.username ?: "",
                senderPhotoUrl = user?.photoUrl ?: "",
                receiverId = receiverId,
                content = content
            )

            saveMessage(message)
            rongCloudService.sendMessage(chatId, uid, content, "text")
            updateLastMessage(chatId, message)
            analyticsService.logEvent("message_sent", android.os.Bundle().apply {
                putString("type", "text")
                putString("chat_id", chatId)
            })

            SendMessageResult(true, message)
        } catch (e: Exception) {
            SendMessageResult(false, error = e.message)
        }
    }

    suspend fun sendImageMessage(
        chatId: String,
        receiverId: String,
        imageUrl: String
    ): SendMessageResult {
        val uid = auth.currentUser?.uid ?: return SendMessageResult(false, error = "Not logged in")
        return try {
            val user = firestore.collection("users").document(uid).get().await()
                .toObject(User::class.java)

            val message = ChatMessage.createImageMessage(
                chatId = chatId,
                senderId = uid,
                senderName = user?.username ?: "",
                senderPhotoUrl = user?.photoUrl ?: "",
                receiverId = receiverId,
                imageUrl = imageUrl
            )

            saveMessage(message)
            rongCloudService.sendMessage(chatId, uid, imageUrl, "image")
            updateLastMessage(chatId, message)
            analyticsService.logEvent("message_sent", android.os.Bundle().apply {
                putString("type", "image")
                putString("chat_id", chatId)
            })

            SendMessageResult(true, message)
        } catch (e: Exception) {
            SendMessageResult(false, error = e.message)
        }
    }

    suspend fun sendVoiceMessage(
        chatId: String,
        receiverId: String,
        voiceUrl: String,
        duration: Long
    ): SendMessageResult {
        val uid = auth.currentUser?.uid ?: return SendMessageResult(false, error = "Not logged in")
        return try {
            val user = firestore.collection("users").document(uid).get().await()
                .toObject(User::class.java)

            val message = ChatMessage.createVoiceMessage(
                chatId = chatId,
                senderId = uid,
                senderName = user?.username ?: "",
                senderPhotoUrl = user?.photoUrl ?: "",
                receiverId = receiverId,
                voiceUrl = voiceUrl,
                duration = duration
            )

            saveMessage(message)
            rongCloudService.sendMessage(chatId, uid, voiceUrl, "voice")
            updateLastMessage(chatId, message)
            analyticsService.logEvent("message_sent", android.os.Bundle().apply {
                putString("type", "voice")
                putString("chat_id", chatId)
            })

            SendMessageResult(true, message)
        } catch (e: Exception) {
            SendMessageResult(false, error = e.message)
        }
    }

    suspend fun sendGiftMessage(
        chatId: String,
        receiverId: String,
        giftId: String,
        giftName: String,
        giftIcon: String
    ): SendMessageResult {
        val uid = auth.currentUser?.uid ?: return SendMessageResult(false, error = "Not logged in")
        return try {
            val user = firestore.collection("users").document(uid).get().await()
                .toObject(User::class.java)

            val message = ChatMessage.createGiftMessage(
                chatId = chatId,
                senderId = uid,
                senderName = user?.username ?: "",
                senderPhotoUrl = user?.photoUrl ?: "",
                receiverId = receiverId,
                giftId = giftId,
                giftName = giftName,
                giftIcon = giftIcon
            )

            saveMessage(message)
            updateLastMessage(chatId, message)
            analyticsService.logGiftSent(giftId, receiverId, 0)
            analyticsService.logEvent("message_sent", android.os.Bundle().apply {
                putString("type", "gift")
                putString("chat_id", chatId)
                putString("gift_id", giftId)
            })

            SendMessageResult(true, message)
        } catch (e: Exception) {
            SendMessageResult(false, error = e.message)
        }
    }

    suspend fun sendStickerMessage(
        chatId: String,
        receiverId: String,
        stickerId: String,
        stickerUrl: String
    ): SendMessageResult {
        val uid = auth.currentUser?.uid ?: return SendMessageResult(false, error = "Not logged in")
        return try {
            val user = firestore.collection("users").document(uid).get().await()
                .toObject(User::class.java)

            val message = ChatMessage.createStickerMessage(
                chatId = chatId,
                senderId = uid,
                senderName = user?.username ?: "",
                senderPhotoUrl = user?.photoUrl ?: "",
                receiverId = receiverId,
                stickerId = stickerId,
                stickerUrl = stickerUrl
            )

            saveMessage(message)
            updateLastMessage(chatId, message)
            analyticsService.logEvent("message_sent", android.os.Bundle().apply {
                putString("type", "sticker")
                putString("chat_id", chatId)
            })

            SendMessageResult(true, message)
        } catch (e: Exception) {
            SendMessageResult(false, error = e.message)
        }
    }

    private suspend fun saveMessage(message: ChatMessage) {
        firestore.collection("messages").document(message.messageId).set(message).await()
    }

    private suspend fun updateLastMessage(chatId: String, message: ChatMessage) {
        firestore.collection("chat_rooms").document(chatId).update(
            mapOf(
                "lastMessage" to message.content,
                "lastMessageTime" to message.timestamp,
                "lastMessageSenderId" to message.senderId
            )
        ).await()
    }
}
