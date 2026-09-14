package com.hayidev.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Story(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val userProfileUrl: String = "",
    val items: List<StoryItem> = emptyList(),
    val viewers: List<String> = emptyList(),
    val viewCount: Int = 0,
    val isActive: Boolean = true,
    val expiresAt: Timestamp? = null,
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "username" to username,
            "userProfileUrl" to userProfileUrl,
            "viewers" to viewers,
            "viewCount" to viewCount,
            "isActive" to isActive,
            "expiresAt" to (expiresAt ?: Timestamp.now()),
            "createdAt" to (createdAt ?: Timestamp.now()),
            "updatedAt" to (updatedAt ?: Timestamp.now())
        )
    }

    fun isExpired(): Boolean {
        val expiry = expiresAt?.toDate()?.time ?: return true
        return System.currentTimeMillis() > expiry
    }

    companion object {
        const val COLLECTION_NAME = "stories"
        const val FIELD_USER_ID = "userId"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_EXPIRES_AT = "expiresAt"
        const val FIELD_IS_ACTIVE = "isActive"
        const val STORY_DURATION_HOURS = 24
    }
}

data class StoryItem(
    @DocumentId
    val id: String = "",
    val storyId: String = "",
    val mediaUrl: String = "",
    val mediaType: StoryMediaType = StoryMediaType.IMAGE,
    val caption: String = "",
    val backgroundColor: String = "#000000",
    val textColor: String = "#FFFFFF",
    val durationSeconds: Int = 5,
    val viewers: List<String> = emptyList(),
    val reactions: List<StoryReaction> = emptyList(),
    val replies: List<StoryReply> = emptyList(),
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "storyId" to storyId,
            "mediaUrl" to mediaUrl,
            "mediaType" to mediaType.name,
            "caption" to caption,
            "backgroundColor" to backgroundColor,
            "textColor" to textColor,
            "durationSeconds" to durationSeconds,
            "createdAt" to (createdAt ?: Timestamp.now())
        )
    }
}

enum class StoryMediaType {
    IMAGE,
    VIDEO,
    TEXT,
    BOOMERANG,
    REWIND
}

data class StoryReaction(
    @DocumentId
    val id: String = "",
    val storyItemId: String = "",
    val userId: String = "",
    val username: String = "",
    val userProfileUrl: String = "",
    val emoji: String = "",
    val reactionType: StoryReactionType = StoryReactionType.LIKE,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "storyItemId" to storyItemId,
            "userId" to userId,
            "username" to username,
            "userProfileUrl" to userProfileUrl,
            "emoji" to emoji,
            "reactionType" to reactionType.name,
            "createdAt" to (createdAt ?: Timestamp.now())
        )
    }
}

enum class StoryReactionType {
    LIKE,
    LOVE,
    FIRE,
    LAUGH,
    WOW,
    SAD,
    ANGRY,
    CUSTOM
}

data class StoryReply(
    @DocumentId
    val id: String = "",
    val storyItemId: String = "",
    val userId: String = "",
    val username: String = "",
    val userProfileUrl: String = "",
    val message: String = "",
    val isRead: Boolean = false,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "storyItemId" to storyItemId,
            "userId" to userId,
            "username" to username,
            "userProfileUrl" to userProfileUrl,
            "message" to message,
            "isRead" to isRead,
            "createdAt" to (createdAt ?: Timestamp.now())
        )
    }
}

data class StoryView(
    @DocumentId
    val id: String = "",
    val storyId: String = "",
    val storyItemId: String = "",
    val userId: String = "",
    val username: String = "",
    val viewedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "storyId" to storyId,
            "storyItemId" to storyItemId,
            "userId" to userId,
            "username" to username,
            "viewedAt" to (viewedAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "story_views"
    }
}

sealed class StoryResult {
    data class Success(val stories: List<Story>) : StoryResult()
    data class StoryCreated(val story: Story) : StoryResult()
    data class Error(val message: String, val exception: Exception? = null) : StoryResult()
    object Loading : StoryResult()
}
