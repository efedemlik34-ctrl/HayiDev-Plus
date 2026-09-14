package com.hayidev.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Achievement(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val category: AchievementCategory = AchievementCategory.GENERAL,
    val tier: AchievementTier = AchievementTier.BRONZE,
    val requiredProgress: Int = 0,
    val coinReward: Int = 0,
    val xpReward: Int = 0,
    val badgeUrl: String = "",
    val isHidden: Boolean = false,
    val isLimitedTime: Boolean = false,
    val startsAt: Timestamp? = null,
    val endsAt: Timestamp? = null,
    val requirements: Map<String, Any> = emptyMap(),
    val order: Int = 0,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "description" to description,
            "iconUrl" to iconUrl,
            "category" to category.name,
            "tier" to tier.name,
            "requiredProgress" to requiredProgress,
            "coinReward" to coinReward,
            "xpReward" to xpReward,
            "badgeUrl" to badgeUrl,
            "isHidden" to isHidden,
            "isLimitedTime" to isLimitedTime,
            "requirements" to requirements,
            "order" to order,
            "createdAt" to (createdAt ?: Timestamp.now())
        )
    }

    fun isAvailable(): Boolean {
        if (!isLimitedTime) return true
        val now = Timestamp.now()
        val start = startsAt?.toDate()?.time ?: return true
        val end = endsAt?.toDate()?.time ?: return true
        val currentTime = now.toDate().time
        return currentTime in start..end
    }

    companion object {
        const val COLLECTION_NAME = "achievements"
    }
}

enum class AchievementCategory {
    GENERAL,
    SOCIAL,
    CONTENT,
    GAMING,
    STREAK,
    REFERRAL,
    PREMIUM,
    EVENT,
    SEASONAL,
    MILESTONE,
    COLLECTION,
    EXPLORATION,
    CREATIVE,
    COMPETITIVE,
    COMMUNITY
}

enum class AchievementTier {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND,
    MASTER,
    LEGENDARY
}

data class UserAchievement(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val achievementId: String = "",
    val achievementName: String = "",
    val achievementDescription: String = "",
    val achievementIconUrl: String = "",
    val category: AchievementCategory = AchievementCategory.GENERAL,
    val tier: AchievementTier = AchievementTier.BRONZE,
    val currentProgress: Int = 0,
    val requiredProgress: Int = 0,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false,
    val completedAt: Timestamp? = null,
    val claimedAt: Timestamp? = null,
    val coinReward: Int = 0,
    val xpReward: Int = 0,
    val progressPercentage: Float
        get() = if (requiredProgress > 0) {
            (currentProgress.toFloat() / requiredProgress * 100).coerceAtMost(100f)
        } else 0f
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "achievementId" to achievementId,
            "achievementName" to achievementName,
            "achievementDescription" to achievementDescription,
            "achievementIconUrl" to achievementIconUrl,
            "category" to category.name,
            "tier" to tier.name,
            "currentProgress" to currentProgress,
            "requiredProgress" to requiredProgress,
            "isCompleted" to isCompleted,
            "isClaimed" to isClaimed,
            "coinReward" to coinReward,
            "xpReward" to xpReward,
            "updatedAt" to (updatedAt ?: Timestamp.now())
        )
    }

    fun canClaim(): Boolean = isCompleted && !isClaimed

    companion object {
        const val COLLECTION_NAME = "user_achievements"
        const val FIELD_USER_ID = "userId"
        const val FIELD_ACHIEVEMENT_ID = "achievementId"
        const val FIELD_IS_COMPLETED = "isCompleted"
        const val FIELD_IS_CLAIMED = "isClaimed"
    }
}

data class AchievementProgress(
    val userId: String = "",
    val category: AchievementCategory = AchievementCategory.GENERAL,
    val actionType: String = "",
    val count: Int = 0,
    val metadata: Map<String, Any> = emptyMap(),
    @ServerTimestamp
    val lastUpdatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "category" to category.name,
            "actionType" to actionType,
            "count" to count,
            "metadata" to metadata,
            "lastUpdatedAt" to (lastUpdatedAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "achievement_progress"
    }
}

sealed class AchievementResult {
    data class Success(val achievements: List<Achievement>) : AchievementResult()
    data class UserAchievementsSuccess(val achievements: List<UserAchievement>) : AchievementResult()
    data class ProgressUpdated(val progress: UserAchievement) : AchievementResult()
    data class RewardClaimed(val achievement: UserAchievement, val coins: Int, val xp: Int) : AchievementResult()
    data class Error(val message: String, val exception: Exception? = null) : AchievementResult()
    object Loading : AchievementResult()
}
