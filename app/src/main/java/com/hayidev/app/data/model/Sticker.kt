package com.hayidev.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class StickerPack(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val author: String = "",
    val authorId: String = "",
    val coverUrl: String = "",
    val stickers: List<Sticker> = emptyList(),
    val stickerCount: Int = 0,
    val price: Int = 0,
    val isPremium: Boolean = false,
    val isFree: Boolean = false,
    val isFeatured: Boolean = false,
    val isNew: Boolean = false,
    val category: StickerCategory = StickerCategory.GENERAL,
    val tags: List<String> = emptyList(),
    val downloadCount: Long = 0,
    val rating: Float = 0f,
    val ratingCount: Int = 0,
    val totalRevenue: Long = 0,
    val isAvailable: Boolean = true,
    val version: Int = 1,
    val minAppVersion: String = "",
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "description" to description,
            "author" to author,
            "authorId" to authorId,
            "coverUrl" to coverUrl,
            "stickerCount" to stickerCount,
            "price" to price,
            "isPremium" to isPremium,
            "isFree" to isFree,
            "isFeatured" to isFeatured,
            "isNew" to isNew,
            "category" to category.name,
            "tags" to tags,
            "downloadCount" to downloadCount,
            "rating" to rating,
            "ratingCount" to ratingCount,
            "isAvailable" to isAvailable,
            "version" to version,
            "createdAt" to (createdAt ?: Timestamp.now()),
            "updatedAt" to (updatedAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "sticker_packs"
        const val FIELD_CATEGORY = "category"
        const val FIELD_IS_FEATURED = "isFeatured"
        const val FIELD_IS_FREE = "isFree"
        const val FIELD_PRICE = "price"
    }
}

enum class StickerCategory(val displayName: String) {
    GENERAL("General"),
    EMOTIONS("Emotions"),
    REACTIONS("Reactions"),
    ANIMALS("Animals"),
    FOOD("Food"),
    TRAVEL("Travel"),
    SPORTS("Sports"),
    MUSIC("Music"),
    GAMING("Gaming"),
    MEMES("Memes"),
    CUTE("Cute"),
    FUNNY("Funny"),
    LOVE("Love"),
    CELEBRATIONS("Celebrations"),
    SEASONS("Seasons"),
    PROFESSIONS("Professions"),
    CUSTOM("Custom"),
    PREMIUM("Premium")
}

data class Sticker(
    @DocumentId
    val id: String = "",
    val packId: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val animatedUrl: String? = null,
    val thumbnailUrl: String = "",
    val emoji: String = "",
    val keywords: List<String> = emptyList(),
    val category: StickerCategory = StickerCategory.GENERAL,
    val usageCount: Long = 0,
    val price: Int = 0,
    val isPremium: Boolean = false,
    val isAnimated: Boolean = false,
    val width: Int = 512,
    val height: Int = 512,
    val fileFormat: String = "png",
    val fileSize: Long = 0,
    val order: Int = 0,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "packId" to packId,
            "name" to name,
            "imageUrl" to imageUrl,
            "animatedUrl" to animatedUrl,
            "thumbnailUrl" to thumbnailUrl,
            "emoji" to emoji,
            "keywords" to keywords,
            "category" to category.name,
            "usageCount" to usageCount,
            "price" to price,
            "isPremium" to isPremium,
            "isAnimated" to isAnimated,
            "width" to width,
            "height" to height,
            "fileFormat" to fileFormat,
            "fileSize" to fileSize,
            "order" to order,
            "createdAt" to (createdAt ?: Timestamp.now())
        )
    }
}

data class UserStickerCollection(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val packId: String = "",
    val packName: String = "",
    val purchasedAt: Timestamp? = null,
    val totalUsageCount: Int = 0,
    val favoriteStickers: List<String> = emptyList(),
    val recentStickers: List<String> = emptyList()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "packId" to packId,
            "packName" to packName,
            "purchasedAt" to (purchasedAt ?: Timestamp.now()),
            "totalUsageCount" to totalUsageCount,
            "favoriteStickers" to favoriteStickers,
            "recentStickers" to recentStickers
        )
    }

    companion object {
        const val COLLECTION_NAME = "user_sticker_collections"
        const val FIELD_USER_ID = "userId"
        const val FIELD_PACK_ID = "packId"
    }
}

data class StickerUsage(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val stickerId: String = "",
    val packId: String = "",
    val chatId: String = "",
    val usedInMessage: Boolean = false,
    @ServerTimestamp
    val usedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "stickerId" to stickerId,
            "packId" to packId,
            "chatId" to chatId,
            "usedInMessage" to usedInMessage,
            "usedAt" to (usedAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "sticker_usages"
    }
}

data class StickerRating(
    @DocumentId
    val id: String = "",
    val packId: String = "",
    val userId: String = "",
    val rating: Int = 0,
    val review: String = "",
    val isHelpful: Boolean = false,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "packId" to packId,
            "userId" to userId,
            "rating" to rating,
            "review" to review,
            "isHelpful" to isHelpful,
            "createdAt" to (createdAt ?: Timestamp.now())
        )
    }
}

sealed class StickerResult {
    data class Success(val packs: List<StickerPack>) : StickerResult()
    data class PackPurchased(val pack: StickerPack) : StickerResult()
    data class CollectionLoaded(val collection: List<UserStickerCollection>) : StickerResult()
    data class Error(val message: String, val exception: Exception? = null) : StickerResult()
    object Loading : StickerResult()
}
