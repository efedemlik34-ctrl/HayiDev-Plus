package com.hayidev.app.data.model

import com.google.firebase.Timestamp

data class GiftItem(
    val giftId: String = "",
    val name: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val animationUrl: String = "",
    val lottieUrl: String = "",
    val price: Int = 0,
    val currency: Currency = Currency.COIN,
    val category: GiftCategory = GiftCategory.NORMAL,
    val rarity: GiftRarity = GiftRarity.COMMON,
    val isAnimated: Boolean = false,
    val isNew: Boolean = false,
    val isPopular: Boolean = false,
    val sortOrder: Int = 0,
    val availableFrom: Timestamp? = null,
    val availableUntil: Timestamp? = null
)

enum class GiftRarity(val displayName: String, val color: Long) {
    COMMON("Ortak", 0xFF808080),
    UNCOMMON("Nadide", 0xFF1EFF00),
    RARE("Nadir", 0xFF0070FF),
    EPIC("Epik", 0xFFA335EE),
    LEGENDARY("Efsanevi", 0xFFFF8000),
    MYTHIC("Mistik", 0xFFFF0000)
}

data class GiftPackage(
    val packageId: String = "",
    val name: String = "",
    val description: String = "",
    val gifts: List<GiftItem> = emptyList(),
    val bonusGifts: List<GiftItem> = emptyList(),
    val originalPrice: Int = 0,
    val discountedPrice: Int = 0,
    val discountPercent: Int = 0,
    val isLimited: Boolean = false,
    val expiresAt: Timestamp? = null
)

data class GiftHistoryEntry(
    val entryId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderPhoto: String = "",
    val receiverId: String = "",
    val receiverName: String = "",
    val receiverPhoto: String = "",
    val gift: GiftItem = GiftItem(),
    val count: Int = 1,
    val totalValue: Int = 0,
    val roomId: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val isHighlighted: Boolean = false
)

data class GiftStats(
    val totalSent: Int = 0,
    val totalReceived: Int = 0,
    val totalValueSent: Int = 0,
    val totalValueReceived: Int = 0,
    val topGift: GiftItem? = null,
    val topReceiver: String = "",
    val topSender: String = ""
)
