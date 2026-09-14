package com.hayidev.app.data.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderPhotoUrl: String = "",
    val receiverId: String = "",
    val content: String = "",
    val type: ChatMessageType = ChatMessageType.TEXT,
    val imageUrl: String = "",
    val voiceUrl: String = "",
    val voiceDuration: Long = 0,
    val giftId: String = "",
    val giftName: String = "",
    val giftIcon: String = "",
    val stickerId: String = "",
    val stickerUrl: String = "",
    val videoUrl: String = "",
    val fileUrl: String = "",
    val fileName: String = "",
    val fileSize: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val locationName: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val isRead: Boolean = false,
    val isDeleted: Boolean = false,
    val isEdited: Boolean = false,
    val replyToMessageId: String = "",
    val replyToContent: String = "",
    val reactions: Map<String, List<String>> = emptyMap()
) {
    companion object {
        fun createTextMessage(
            chatId: String,
            senderId: String,
            senderName: String,
            senderPhotoUrl: String,
            receiverId: String,
            content: String
        ) = ChatMessage(
            chatId = chatId,
            senderId = senderId,
            senderName = senderName,
            senderPhotoUrl = senderPhotoUrl,
            receiverId = receiverId,
            content = content,
            type = ChatMessageType.TEXT
        )

        fun createImageMessage(
            chatId: String,
            senderId: String,
            senderName: String,
            senderPhotoUrl: String,
            receiverId: String,
            imageUrl: String
        ) = ChatMessage(
            chatId = chatId,
            senderId = senderId,
            senderName = senderName,
            senderPhotoUrl = senderPhotoUrl,
            receiverId = receiverId,
            content = "📷 Fotoğraf",
            type = ChatMessageType.IMAGE,
            imageUrl = imageUrl
        )

        fun createVoiceMessage(
            chatId: String,
            senderId: String,
            senderName: String,
            senderPhotoUrl: String,
            receiverId: String,
            voiceUrl: String,
            duration: Long
        ) = ChatMessage(
            chatId = chatId,
            senderId = senderId,
            senderName = senderName,
            senderPhotoUrl = senderPhotoUrl,
            receiverId = receiverId,
            content = "🎤 Sesli Mesaj",
            type = ChatMessageType.VOICE,
            voiceUrl = voiceUrl,
            voiceDuration = duration
        )

        fun createGiftMessage(
            chatId: String,
            senderId: String,
            senderName: String,
            senderPhotoUrl: String,
            receiverId: String,
            giftId: String,
            giftName: String,
            giftIcon: String
        ) = ChatMessage(
            chatId = chatId,
            senderId = senderId,
            senderName = senderName,
            senderPhotoUrl = senderPhotoUrl,
            receiverId = receiverId,
            content = "$giftIcon $giftName",
            type = ChatMessageType.GIFT,
            giftId = giftId,
            giftName = giftName,
            giftIcon = giftIcon
        )

        fun createStickerMessage(
            chatId: String,
            senderId: String,
            senderName: String,
            senderPhotoUrl: String,
            receiverId: String,
            stickerId: String,
            stickerUrl: String
        ) = ChatMessage(
            chatId = chatId,
            senderId = senderId,
            senderName = senderName,
            senderPhotoUrl = senderPhotoUrl,
            receiverId = receiverId,
            content = "🎨 Sticker",
            type = ChatMessageType.STICKER,
            stickerId = stickerId,
            stickerUrl = stickerUrl
        )
    }
}

enum class ChatMessageType(val displayName: String) {
    TEXT("Metin"),
    IMAGE("Fotoğraf"),
    VOICE("Sesli Mesaj"),
    GIFT("Hediye"),
    STICKER("Sticker"),
    VIDEO("Video"),
    FILE("Dosya"),
    LOCATION("Konum"),
    VIDEO_CALL("Video Arama"),
    SYSTEM("Sistem"),
    REPLY("Yanıt")
}

data class ConversationInfo(
    val chatId: String = "",
    val otherUserId: String = "",
    val otherUserName: String = "",
    val otherUserPhoto: String = "",
    val lastMessage: ChatMessage? = null,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val lastSeen: Timestamp? = null,
    val isTyping: Boolean = false,
    val isBlocked: Boolean = false
)
