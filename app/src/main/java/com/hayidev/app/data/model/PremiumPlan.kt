package com.hayidev.app.data.model

import com.google.firebase.Timestamp

data class PremiumPlan(
    val planId: String = "",
    val name: String = "",
    val displayName: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val currency: String = "TRY",
    val period: SubscriptionPeriod = SubscriptionPeriod.MONTHLY,
    val features: List<PremiumFeature> = emptyList(),
    val productId: String = "",
    val isActive: Boolean = true
)

enum class SubscriptionPeriod(val displayName: String, val months: Int) {
    WEEKLY("Haftalık", 1),
    MONTHLY("Aylık", 1),
    QUARTERLY("3 Aylık", 3),
    YEARLY("Yıllık", 12)
}

data class PremiumFeature(
    val featureId: String = "",
    val name: String = "",
    val description: String = "",
    val icon: String = ""
)

enum class PremiumFeatureType(val displayName: String, val icon: String) {
    NO_ADS("Reklamsız Deneyim", "🚫"),
    UNLIMITED_LIKES("Sınırsız Beğeni", "❤️"),
    WHO_LIKED_ME("Beni Beğenenleri Gör", "👀"),
    ADVANCED_FILTER("Gelişmiş Filtre", "🔍"),
    PRIORITY_SUPPORT("Öncelikli Destek", "⭐"),
    PROFILE_BOOST("Profil Boost", "🚀"),
    SUPER_LIKE("Süper Beğeni", "💎"),
    UNDO_PASS("Geri Al", "↩️"),
    INCOGNITO("Gizli Mod", "👻"),
    READ_RECEIPTS("Okundu Bilgisi", "✓✓"),
    UNLIMITED_SWIPES("Sınırsız Kaydırma", "♾️"),
    VIDEO_CALLS("Video Görüşmeler", "📹"),
    LIVE_STREAMING("Canlı Yayın", "📡"),
    PREMIUM_GIFTS("Premium Hediyeler", "🎁")
}

data class UserSubscription(
    val userId: String = "",
    val planId: String = "",
    val planName: String = "",
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val isActive: Boolean = false,
    val autoRenew: Boolean = true,
    val paymentMethod: String = "",
    val purchaseToken: String = "",
    val orderId: String = ""
)

data class PremiumState(
    val isPremium: Boolean = false,
    val plan: PremiumPlan? = null,
    val subscription: UserSubscription? = null,
    val daysRemaining: Int = 0,
    val features: List<PremiumFeatureType> = emptyList()
)
