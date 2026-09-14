package com.hayidev.app.data.model

import com.google.firebase.Timestamp

data class LiveRoom(
    val roomId: String = "",
    val hostId: String = "",
    val hostName: String = "",
    val hostPhotoUrl: String = "",
    val title: String = "",
    val coverUrl: String = "",
    val description: String = "",
    val type: RoomType = RoomType.PUBLIC,
    val viewerCount: Int = 0,
    val likeCount: Int = 0,
    val guestCount: Int = 0,
    val maxGuests: Int = 6,
    val guests: List<Guest> = emptyList(),
    val isLive: Boolean = false,
    val createdAt: Timestamp = Timestamp.now(),
    val tags: List<String> = emptyList()
)

data class Guest(
    val userId: String = "",
    val username: String = "",
    val photoUrl: String = "",
    val isMuted: Boolean = true,
    val joinedAt: Timestamp = Timestamp.now()
)

enum class RoomType {
    PUBLIC,
    PRIVATE,
    FOLLOWERS_ONLY
}

data class Gift(
    val giftId: String = "",
    val name: String = "",
    val iconUrl: String = "",
    val animationUrl: String = "",
    val price: Int = 0,
    val currency: Currency = Currency.COIN,
    val category: GiftCategory = GiftCategory.NORMAL
)

enum class Currency {
    COIN,
    DIAMOND
}

enum class GiftCategory {
    NORMAL,
    RARE,
    EPIC,
    LEGENDARY
}

data class GiftTransaction(
    val transactionId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val roomId: String = "",
    val gift: Gift = Gift(),
    val count: Int = 1,
    val timestamp: Timestamp = Timestamp.now()
)
