package com.hayidev.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class AppTheme(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val author: String = "",
    val authorId: String = "",
    val previewUrl: String = "",
    val thumbnailUrl: String = "",
    val colors: ThemeColors = ThemeColors(),
    val typography: ThemeTypography = ThemeTypography(),
    val assets: ThemeAssets = ThemeAssets(),
    val price: Int = 0,
    val isPremium: Boolean = false,
    val isFree: Boolean = true,
    val isDefault: Boolean = false,
    val isFeatured: Boolean = false,
    val isNew: Boolean = false,
    val category: ThemeCategory = ThemeCategory.DEFAULT,
    val tags: List<String> = emptyList(),
    val downloadCount: Long = 0,
    val rating: Float = 0f,
    val ratingCount: Int = 0,
    val compatibleVersions: List<String> = emptyList(),
    val isAvailable: Boolean = true,
    val version: Int = 1,
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
            "previewUrl" to previewUrl,
            "thumbnailUrl" to thumbnailUrl,
            "price" to price,
            "isPremium" to isPremium,
            "isFree" to isFree,
            "isDefault" to isDefault,
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
        const val COLLECTION_NAME = "app_themes"
        const val FIELD_CATEGORY = "category"
        const val FIELD_IS_FREE = "isFree"
        const val FIELD_IS_FEATURED = "isFeatured"
        const val FIELD_IS_DEFAULT = "isDefault"
        const val FIELD_PRICE = "price"
    }
}

data class ThemeColors(
    val primary: String = "#6200EE",
    val primaryVariant: String = "#3700B3",
    val secondary: String = "#03DAC6",
    val secondaryVariant: String = "#018786",
    val background: String = "#FFFFFF",
    val surface: String = "#FFFFFF",
    val error: String = "#B00020",
    val onPrimary: String = "#FFFFFF",
    val onSecondary: String = "#000000",
    val onBackground: String = "#000000",
    val onSurface: String = "#000000",
    val onError: String = "#FFFFFF",
    val isLight: Boolean = true,
    val statusBarColor: String = "#6200EE",
    val navigationBarColor: String = "#FFFFFF",
    val cardColor: String = "#FFFFFF",
    val dividerColor: String = "#BDBDBD",
    val accentColor: String = "#FF6D00",
    val successColor: String = "#4CAF50",
    val warningColor: String = "#FFC107",
    val infoColor: String = "#2196F3"
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "primary" to primary,
            "primaryVariant" to primaryVariant,
            "secondary" to secondary,
            "secondaryVariant" to secondaryVariant,
            "background" to background,
            "surface" to surface,
            "error" to error,
            "onPrimary" to onPrimary,
            "onSecondary" to onSecondary,
            "onBackground" to onBackground,
            "onSurface" to onSurface,
            "onError" to onError,
            "isLight" to isLight,
            "statusBarColor" to statusBarColor,
            "navigationBarColor" to navigationBarColor,
            "cardColor" to cardColor,
            "dividerColor" to dividerColor,
            "accentColor" to accentColor,
            "successColor" to successColor,
            "warningColor" to warningColor,
            "infoColor" to infoColor
        )
    }
}

data class ThemeTypography(
    val fontFamily: String = "Roboto",
    val titleFontFamily: String = "Roboto",
    val bodyFontFamily: String = "Roboto",
    val headlineSize: Float = 24f,
    val titleSize: Float = 20f,
    val subtitleSize: Float = 16f,
    val bodySize: Float = 14f,
    val captionSize: Float = 12f,
    val buttonSize: Float = 14f,
    val overlineSize: Float = 10f,
    val useCustomFont: Boolean = false,
    val fontUrl: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "fontFamily" to fontFamily,
            "titleFontFamily" to titleFontFamily,
            "bodyFontFamily" to bodyFontFamily,
            "headlineSize" to headlineSize,
            "titleSize" to titleSize,
            "subtitleSize" to subtitleSize,
            "bodySize" to bodySize,
            "captionSize" to captionSize,
            "buttonSize" to buttonSize,
            "overlineSize" to overlineSize,
            "useCustomFont" to useCustomFont,
            "fontUrl" to fontUrl
        )
    }
}

data class ThemeAssets(
    val logoUrl: String = "",
    val splashScreenUrl: String = "",
    val headerImageUrl: String = "",
    val backgroundImageUrl: String = "",
    val profileFrameUrl: String = "",
    val chatBackgroundUrl: String = "",
    val iconPackUrl: String = "",
    val soundEffects: Map<String, String> = emptyMap(),
    val animations: Map<String, String> = emptyMap()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "logoUrl" to logoUrl,
            "splashScreenUrl" to splashScreenUrl,
            "headerImageUrl" to headerImageUrl,
            "backgroundImageUrl" to backgroundImageUrl,
            "profileFrameUrl" to profileFrameUrl,
            "chatBackgroundUrl" to chatBackgroundUrl,
            "iconPackUrl" to iconPackUrl,
            "soundEffects" to soundEffects,
            "animations" to animations
        )
    }
}

enum class ThemeCategory(val displayName: String) {
    DEFAULT("Default"),
    DARK("Dark"),
    LIGHT("Light"),
    COLORFUL("Colorful"),
    MINIMAL("Minimal"),
    NEON("Neon"),
    PASTEL("Pastel"),
    NATURE("Nature"),
    SPACE("Space"),
    RETRO("Retro"),
    GAMING("Gaming"),
    ELEGANT("Elegant"),
    CUTE("Cute"),
    SPORTY("Sporty"),
    PREMIUM("Premium"),
    SEASONAL("Seasonal"),
    CUSTOM("Custom")
}

data class UserTheme(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val themeId: String = "",
    val themeName: String = "",
    val isActive: Boolean = false,
    val isPurchased: Boolean = false,
    val purchasedAt: Timestamp? = null,
    @ServerTimestamp
    val appliedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "themeId" to themeId,
            "themeName" to themeName,
            "isActive" to isActive,
            "isPurchased" to isPurchased,
            "purchasedAt" to (purchasedAt ?: Timestamp.now()),
            "appliedAt" to (appliedAt ?: Timestamp.now())
        )
    }

    companion object {
        const val COLLECTION_NAME = "user_themes"
        const val FIELD_USER_ID = "userId"
        const val FIELD_THEME_ID = "themeId"
        const val FIELD_IS_ACTIVE = "isActive"
    }
}

data class ThemeRating(
    @DocumentId
    val id: String = "",
    val themeId: String = "",
    val userId: String = "",
    val rating: Int = 0,
    val review: String = "",
    val isHelpful: Boolean = false,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "themeId" to themeId,
            "userId" to userId,
            "rating" to rating,
            "review" to review,
            "isHelpful" to isHelpful,
            "createdAt" to (createdAt ?: Timestamp.now())
        )
    }
}

sealed class ThemeResult {
    data class Success(val themes: List<AppTheme>) : ThemeResult()
    data class ThemePurchased(val theme: AppTheme) : ThemeResult()
    data class ThemeApplied(val userTheme: UserTheme) : ThemeResult()
    data class UserThemesLoaded(val themes: List<UserTheme>) : ThemeResult()
    data class Error(val message: String, val exception: Exception? = null) : ThemeResult()
    object Loading : ThemeResult()
}
