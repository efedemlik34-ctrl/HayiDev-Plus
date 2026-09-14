package com.hayidev.app.data.model

import com.google.firebase.Timestamp

data class GameResult(
    val gameId: String = "",
    val gameType: GameType = GameType.LUCKY_GAME,
    val userId: String = "",
    val roomId: String = "",
    val betAmount: Int = 0,
    val winAmount: Int = 0,
    val winDiamond: Int = 0,
    val winMultiple: Int = 0,
    val isJackpot: Boolean = false,
    val assetUrl: String = "",
    val result: String = "",
    val details: Map<String, Any> = emptyMap(),
    val timestamp: Timestamp = Timestamp.now()
)

enum class GameType(val displayName: String, val icon: String) {
    LUCKY_GAME("Şans Oyunu", "🎰"),
    LUCKY_GIFT("Şanslı Hediye", "🎁"),
    JACKPOT("Jackpot", "💎"),
    ROCKET("Roket", "🚀"),
    WHEEL("Çarkıfelek", "🎡"),
    DICE("Zar", "🎲"),
    COIN_FLIP("Yazı Tura", "🪙"),
    SCRATCH("Kazı Kazan", "🎫"),
    PUZZLE("Bulmaca", "🧩"),
    MINI_GAMES("Mini Oyunlar", "🎮")
}

data class LuckyGameConfig(
    val gameId: String = "",
    val gameType: String = "wheel",
    val betOptions: List<Int> = listOf(10, 25, 50, 100, 200, 500),
    val prizes: List<LuckyPrize> = emptyList(),
    val jackpotPool: Int = 0,
    val jackpotProbability: Float = 0.01f,
    val dailyLimit: Int = 50,
    val isPremiumOnly: Boolean = false
)

data class LuckyPrize(
    val prizeId: String = "",
    val name: String = "",
    val amount: Int = 0,
    val type: PrizeType = PrizeType.COINS,
    val probability: Float = 0.1f,
    val color: Long = 0xFFFF6B35,
    val isSpecial: Boolean = false
)

data class LuckyGiftInfo(
    val giftId: String = "",
    val giftName: String = "",
    val giftIcon: String = "",
    val price: Int = 0,
    val luckyMultiple: List<Int> = listOf(1, 2, 3, 5, 10),
    val luckyProbability: Float = 0.3f,
    val comboBonus: Float = 0.1f,
    val comboLevels: List<ComboLevel> = emptyList()
)

data class ComboLevel(
    val level: Int = 1,
    val comboCount: Int = 10,
    val bonusPercent: Float = 0.1f,
    val rewardCoins: Int = 0
)

data class JackpotRecord(
    val recordId: String = "",
    val winnerName: String = "",
    val winnerPhoto: String = "",
    val winAmount: Int = 0,
    val gameType: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

data class JackpotPool(
    val currentPool: Int = 0,
    val totalContributed: Int = 0,
    val lastWinAmount: Int = 0,
    val lastWinnerName: String = "",
    val lastWinTime: Timestamp? = null,
    val rules: List<String> = listOf(
        "Her Şans Oyunu oynaması Jackpot'u tetikleme şansı verir",
        "Şans Oyunu'ndaki bahisler Jackpot havuzuna eklenir",
        "Jackpot kayıtları sadece Jackpot kazanımlarını gösterir",
        "Şans Oyunu kazanç kayıtlarını ilgili oyunda görüntüleyin"
    )
)

data class RoomGameConfig(
    val gameId: String = "",
    val gameUrl: String = "",
    val gameHeight: Int = 400,
    val gameHeightMin: Int = 300,
    val gameHeightMax: Int = 600,
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val loadFailed: Boolean = false
)

data class RocketGame(
    val gameId: String = "",
    val multiplier: Float = 1.0f,
    val maxMultiplier: Float = 100.0f,
    val crashPoint: Float = 0f,
    val betAmount: Int = 0,
    val cashedOut: Boolean = false,
    val cashOutMultiplier: Float = 0f,
    val winAmount: Int = 0,
    val isActive: Boolean = false,
    val history: List<Float> = emptyList()
)

enum class PrizeType {
    COINS, DIAMONDS, GIFT, STICKER, NOTHING
}

data class GameStats(
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val totalCoinsWon: Int = 0,
    val totalCoinsLost: Int = 0,
    val biggestWin: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val jackpotsWon: Int = 0,
    val lastGameTime: Timestamp? = null
)

data class DailyGameLimit(
    val luckyGameRemaining: Int = 50,
    val luckyGiftRemaining: Int = 100,
    val rocketRemaining: Int = 20,
    val wheelRemaining: Int = 10,
    val diceRemaining: Int = 20,
    val coinFlipRemaining: Int = 20,
    val scratchRemaining: Int = 5,
    val lastResetDate: String = ""
)
