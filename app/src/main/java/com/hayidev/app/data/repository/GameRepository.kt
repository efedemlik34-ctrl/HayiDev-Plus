package com.hayidev.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hayidev.app.data.model.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val gameResultsCollection = firestore.collection("game_results")
    private val jackpotCollection = firestore.collection("jackpot_pool")
    private val leaderboardCollection = firestore.collection("leaderboard")

    suspend fun placeBet(
        gameType: GameType,
        betAmount: Int,
        roomId: String? = null
    ): Result<GameResult> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
        return try {
            val userDoc = firestore.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java)
                ?: return Result.failure(Exception("User not found"))

            if (user.coins < betAmount) {
                return Result.failure(Exception("Yetersiz bakiye"))
            }

            val result = playGame(gameType, betAmount, uid, roomId)

            firestore.collection("users").document(uid)
                .update("coins", com.google.firebase.firestore.FieldValue.increment(
                    (result.winAmount - betAmount).toLong()
                )).await()

            if (result.winAmount > 0) {
                updateLeaderboard(uid, result.winAmount)
            }

            val resultId = gameResultsCollection.document().id
            val gameResult = result.copy(gameId = resultId)
            gameResultsCollection.document(resultId).set(gameResult).await()

            if (result.isJackpot) {
                updateJackpotPool(result.winAmount)
            }

            Result.success(gameResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun playGame(
        gameType: GameType,
        betAmount: Int,
        userId: String,
        roomId: String?
    ): GameResult {
        val random = Math.random()
        val multiplier = when {
            random < 0.01 -> 100
            random < 0.05 -> 50
            random < 0.15 -> 10
            random < 0.30 -> 5
            random < 0.50 -> 2
            else -> 0
        }

        val winAmount = betAmount * multiplier
        val isJackpot = random < 0.001

        return GameResult(
            gameType = gameType,
            userId = userId,
            roomId = roomId ?: "",
            betAmount = betAmount,
            winAmount = winAmount,
            winMultiple = multiplier,
            isJackpot = isJackpot,
            result = "x$multiplier"
        )
    }

    suspend fun getBalance(): Result<Int> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            val user = doc.toObject(User::class.java)
            Result.success(user?.coins ?: 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistory(limit: Long = 30): List<GameResult> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = gameResultsCollection
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            snapshot.toObjects(GameResult::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getLeaderboard(limit: Long = 50): List<LeaderboardEntry> {
        return try {
            val snapshot = leaderboardCollection
                .orderBy("totalWinnings", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            snapshot.toObjects(LeaderboardEntry::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun updateLeaderboard(userId: String, winAmount: Int) {
        val doc = leaderboardCollection.document(userId).get().await()
        if (doc.exists()) {
            leaderboardCollection.document(userId).update(
                mapOf(
                    "totalWinnings" to com.google.firebase.firestore.FieldValue.increment(winAmount.toLong()),
                    "gamesPlayed" to com.google.firebase.firestore.FieldValue.increment(1)
                )
            ).await()
        } else {
            val user = firestore.collection("users").document(userId).get().await()
                .toObject(User::class.java)
            leaderboardCollection.document(userId).set(
                LeaderboardEntry(
                    userId = userId,
                    username = user?.username ?: "",
                    photoUrl = user?.photoUrl ?: "",
                    totalWinnings = winAmount,
                    gamesPlayed = 1
                )
            ).await()
        }
    }

    private suspend fun updateJackpotPool(amount: Int) {
        jackpotCollection.document("main").update(
            "currentPool", com.google.firebase.firestore.FieldValue.increment(amount.toLong())
        ).await()
    }

    suspend fun getJackpotPool(): JackpotPool? {
        return try {
            jackpotCollection.document("main").get().await()
                .toObject(JackpotPool::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getGameConfig(gameType: GameType): LuckyGameConfig? {
        return try {
            firestore.collection("game_configs")
                .document(gameType.name)
                .get()
                .await()
                .toObject(LuckyGameConfig::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getDailyLimits(): DailyGameLimit? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            firestore.collection("daily_limits")
                .document(uid)
                .get()
                .await()
                .toObject(DailyGameLimit::class.java)
        } catch (e: Exception) {
            null
        }
    }
}

data class LeaderboardEntry(
    val userId: String = "",
    val username: String = "",
    val photoUrl: String = "",
    val totalWinnings: Int = 0,
    val gamesPlayed: Int = 0,
    val rank: Int = 0
)
