package com.hayidev.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class AppNotification(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val type: NotificationType = NotificationType.GENERAL,
    val senderId: String = "",
    val senderUsername: String = "",
    val senderProfileUrl: String = "",
    val referenceId: String = "",
    val referenceType: String = "",
    val imageUrl: String? = null,
    val isRead: Boolean = false,
    val isDeleted: Boolean = false,
    val actionUrl: String? = null,
    val metadata: Map<String, Any> = emptyMap(),
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val readAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "title" to title,
            "body" to body,
            "type" to type.name,
            "senderId" to senderId,
            "senderUsername" to senderUsername,
            "senderProfileUrl" to senderProfileUrl,
            "referenceId" to referenceId,
            "referenceType" to referenceType,
            "imageUrl" to imageUrl,
            "isRead" to isRead,
            "isDeleted" to isDeleted,
            "actionUrl" to actionUrl,
            "metadata" to metadata,
            "createdAt" to (createdAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "notifications"
        const val FIELD_USER_ID = "userId"
        const val FIELD_IS_READ = "isRead"
        const val FIELD_IS_DELETED = "isDeleted"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_TYPE = "type"
    }
}

enum class NotificationType {
    GENERAL,
    LIKE,
    COMMENT,
    FOLLOW,
    FOLLOW_REQUEST,
    MENTION,
    TAG,
    STORY_VIEW,
    STORY_REACTION,
    STORY_REPLY,
    MESSAGE,
    GROUP_INVITE,
    GROUP_JOIN,
    GROUP_MESSAGE,
    ACHIEVEMENT_UNLOCKED,
    ACHIEVEMENT_REWARD,
    LEADERBOARD_RANK,
    LEADERBOARD_REWARD,
    DAILY_REWARD,
    REFERRAL_REWARD,
    PREMIUM_EXPIRING,
    PREMIUM_EXPIRED,
    PREMIUM_RENEWED,
    COIN_PURCHASE,
    COIN_REWARD,
    STICKER_PURCHASE,
    GAME_RESULT,
    SPIN_WHEEL,
    SLOT_MACHINE,
    LUCKY_BOX,
    SYSTEM_ANNOUNCEMENT,
    SECURITY_ALERT,
    ACCOUNT_VERIFIED,
    REPORT_RESOLVED,
    LIVE_STREAM_START,
    LIVE_STREAM_END,
    EVENT_REMINDER,
    PROMOTIONAL,
    WEEKLY_SUMMARY,
    MONTHLY_SUMMARY
}

data class NotificationGroup(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val type: NotificationType = NotificationType.GENERAL,
    val notifications: List<AppNotification> = emptyList(),
    val totalCount: Int = 0,
    val unreadCount: Int = 0,
    val latestNotification: AppNotification? = null,
    val isCollapsed: Boolean = false,
    @ServerTimestamp
    val lastUpdatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "type" to type.name,
            "totalCount" to totalCount,
            "unreadCount" to unreadCount,
            "isCollapsed" to isCollapsed,
            "lastUpdatedAt" to (lastUpdatedAt ?: Timestamp.now())
        )
    }
}

data class NotificationSettings(
    val userId: String = "",
    val pushEnabled: Boolean = true,
    val emailEnabled: Boolean = false,
    val likesEnabled: Boolean = true,
    val commentsEnabled: Boolean = true,
    val followsEnabled: Boolean = true,
    val mentionsEnabled: Boolean = true,
    val storiesEnabled: Boolean = true,
    val messagesEnabled: Boolean = true,
    val groupMessagesEnabled: Boolean = true,
    val achievementsEnabled: Boolean = true,
    val leaderboardEnabled: Boolean = true,
    val dailyRewardsEnabled: Boolean = true,
    val promotionsEnabled: Boolean = false,
    val systemEnabled: Boolean = true
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "pushEnabled" to pushEnabled,
            "emailEnabled" to emailEnabled,
            "likesEnabled" to likesEnabled,
            "commentsEnabled" to commentsEnabled,
            "followsEnabled" to followsEnabled,
            "mentionsEnabled" to mentionsEnabled,
            "storiesEnabled" to storiesEnabled,
            "messagesEnabled" to messagesEnabled,
            "groupMessagesEnabled" to groupMessagesEnabled,
            "achievementsEnabled" to achievementsEnabled,
            "leaderboardEnabled" to leaderboardEnabled,
            "dailyRewardsEnabled" to dailyRewardsEnabled,
            "promotionsEnabled" to promotionsEnabled,
            "systemEnabled" to systemEnabled
        )
    }

    companion object {
        const val COLLECTION_NAME = "notification_settings"
    }
}

sealed class NotificationResult {
    data class Success(val notifications: List<AppNotification>) : NotificationResult()
    data class GroupedSuccess(val groups: List<NotificationGroup>) : NotificationResult()
    data class Error(val message: String, val exception: Exception? = null) : NotificationResult()
    data class TokenSaved(val token: String) : NotificationResult()
    object Loading : NotificationResult()
    object Cleared : NotificationResult()
}
