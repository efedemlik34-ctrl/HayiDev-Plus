package com.hayidev.app.data.model

import com.google.firebase.Timestamp

data class ChatRoom(
    val roomId: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Timestamp? = null,
    val unreadCount: Map<String, Int> = emptyMap(),
    val isMatch: Boolean = false,
    val matchTime: Timestamp? = null
)

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val content: String = "",
    val type: MessageType = MessageType.TEXT,
    val imageUrl: String = "",
    val voiceUrl: String = "",
    val giftId: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val isRead: Boolean = false,
    val isDeleted: Boolean = false
)

enum class MessageType {
    TEXT,
    IMAGE,
    VOICE,
    GIFT,
    VIDEO_CALL,
    SYSTEM
}
