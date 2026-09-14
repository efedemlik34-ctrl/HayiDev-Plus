package com.hayidev.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class LeaderboardEntry(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val userProfileUrl: String = "",
    val score: Long = 0,
    val rank: Int = 0,
    val previousRank: Int = 0,
    val rankChange: Int = 0,
    val level: Int = 1,
    val xp: Long = 0,
    val isOnline: Boolean = false,
    val isPremium: Boolean = false,
    val badgeCount: Int = 0,
    val streakDays: Int = 0,
    val totalLikes: Long = 0,
    val totalComments: Long = 0,
    val totalShares: Long = 0,
    val achievementsUnlocked: Int = 0,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val extraData: Map<String, Any> = emptyMap(),
    @ServerTimestamp
    val lastActiveAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "username" to username,
            "userProfileUrl" to userProfileUrl,
            "score" to score,
            "rank" to rank,
            "previousRank" to previousRank,
            "rankChange" to rankChange,
            "level" to level,
            "xp" to xp,
            "isOnline" to isOnline,
            "isPremium" to isPremium,
            "badgeCount" to badgeCount,
            "streakDays" to streakDays,
            "totalLikes" to totalLikes,
            "totalComments" to totalComments,
            "totalShares" to totalShares,
            "achievementsUnlocked" to achievementsUnlocked,
            "gamesPlayed" to gamesPlayed,
            "gamesWon" to gamesWon,
            "extraData" to extraData,
            "updatedAt" to (updatedAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "leaderboard_entries"
        const val FIELD_USER_ID = "userId"
        const val FIELD_SCORE = "score"
        const val FIELD_RANK = "rank"
        const val FIELD_LAST_ACTIVE_AT = "lastActiveAt"
    }
}

enum class LeaderboardPeriod(val displayName: String, val durationHours: Int) {
    DAILY("Daily", 24),
    WEEKLY("Weekly", 168),
    MONTHLY("Monthly", 720),
    ALL_TIME("All Time", -1),
    SEASONAL("Seasonal", 2160)
}

enum class LeaderboardType(val displayName: String) {
    OVERALL("Overall"),
    SOCIAL("Social"),
    GAMING("Gaming"),
    CONTENT_CREATION("Content Creation"),
    STREAK("Streak"),
    ACHIEVEMENTS("Achievements"),
    REFERRALS("Referrals"),
    PREMIUM("Premium"),
    EVENTS("Events")
}

data class Leaderboard(
    @DocumentId
    val id: String = "",
    val type: LeaderboardType = LeaderboardType.OVERALL,
    val period: LeaderboardPeriod = LeaderboardPeriod.WEEKLY,
    val entries: List<LeaderboardEntry> = emptyList(),
    val totalParticipants: Int = 0,
    val seasonNumber: Int = 1,
    val seasonName: String = "",
    val seasonStart: Timestamp? = null,
    val seasonEnd: Timestamp? = null,
    val prizes: LeaderboardPrizes = LeaderboardPrizes(),
    val isActive: Boolean = true,
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to type.name,
            "period" to period.name,
            "totalParticipants" to totalParticipants,
            "seasonNumber" to seasonNumber,
            "seasonName" to seasonName,
            "isActive" to isActive,
            "createdAt" to (createdAt ?: Timestamp.now()),
            "updatedAt" to (updatedAt ?: Timestamp.now())
        )
    }

    fun getUserRank(userId: String): LeaderboardEntry? {
        return entries.find { it.userId == userId }
    }

    fun getTopEntries(count: Int = 10): List<LeaderboardEntry> {
        return entries.sortedBy { it.rank }.take(count)
    }

    companion object {
        const val COLLECTION_NAME = "leaderboards"
        const val FIELD_TYPE = "type"
        const val FIELD_PERIOD = "period"
        const val FIELD_IS_ACTIVE = "isActive"
    }
}

data class LeaderboardPrizes(
    val firstPlace: Prize = Prize(1000, "Gold Crown", "premium_7_days"),
    val secondPlace: Prize = Prize(500, "Silver Crown", "premium_3_days"),
    val thirdPlace: Prize = Prize(250, "Bronze Crown", "premium_1_day"),
    val fourthToTenth: Prize = Prize(100, "Participation Badge", ""),
    val topHundred: Prize = Prize(50, "Top 100 Badge", "")
)

data class Prize(
    val coins: Int = 0,
    val badgeName: String = "",
    val premiumDays: String = "",
    val extraRewards: Map<String, Any> = emptyMap()
)

data class LeaderboardRewards(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val leaderboardId: String = "",
    val rank: Int = 0,
    val coins: Int = 0,
    val badgeName: String = "",
    val premiumDays: Int = 0,
    val isClaimed: Boolean = false,
    val claimedAt: Timestamp? = null,
    @ServerTimestamp
    val awardedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "leaderboardId" to leaderboardId,
            "rank" to rank,
            "coins" to coins,
            "badgeName" to badgeName,
            "premiumDays" to premiumDays,
            "isClaimed" to isClaimed,
            "awardedAt" to (awardedAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "leaderboard_rewards"
    }
}

data class RankHistory(
    val entryId: String = "",
    val userId: String = "",
    val period: LeaderboardPeriod = LeaderboardPeriod.WEEKLY,
    val type: LeaderboardType = LeaderboardType.OVERALL,
    val rank: Int = 0,
    val score: Long = 0,
    val seasonNumber: Int = 1,
    @ServerTimestamp
    val recordedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "entryId" to entryId,
            "userId" to userId,
            "period" to period.name,
            "type" to type.name,
            "rank" to rank,
            "score" to score,
            "seasonNumber" to seasonNumber,
            "recordedAt" to (recordedAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "rank_history"
    }
}

sealed class LeaderboardResult {
    data class Success(val leaderboard: Leaderboard) : LeaderboardResult()
    data class UserRankSuccess(val entry: LeaderboardEntry?) : LeaderboardResult()
    data class ScoreUpdated(val entry: LeaderboardEntry) : LeaderboardResult()
    data class RewardsClaimed(val rewards: LeaderboardRewards) : LeaderboardResult()
    data class History(val history: List<RankHistory>) : LeaderboardResult()
    data class Error(val message: String, val exception: Exception? = null) : LeaderboardResult()
    object Loading : LeaderboardResult()
}
