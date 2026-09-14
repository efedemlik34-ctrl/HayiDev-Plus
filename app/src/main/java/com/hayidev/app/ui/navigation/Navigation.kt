package com.hayidev.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.hayidev.app.ui.screens.chat.ChatListScreen
import com.hayidev.app.ui.screens.chat.ChatScreen
import com.hayidev.app.ui.screens.discover.DiscoverScreen
import com.hayidev.app.ui.screens.gift.GiftStoreScreen
import com.hayidev.app.ui.screens.live.CreateLiveRoomScreen
import com.hayidev.app.ui.screens.live.LiveRoomScreen
import com.hayidev.app.ui.screens.live.LiveScreen
import com.hayidev.app.ui.screens.login.LoginScreen
import com.hayidev.app.ui.screens.profile.OtherProfileScreen
import com.hayidev.app.ui.screens.profile.ProfileScreen
import com.hayidev.app.ui.screens.settings.SettingsScreen
import com.hayidev.app.ui.screens.video.VideoCallScreen
import com.hayidev.app.ui.screens.region.RegionSelectionScreen
import com.hayidev.app.ui.screens.report.ReportScreen
import com.hayidev.app.ui.screens.game.GamesHubScreen
import com.hayidev.app.ui.screens.game.LuckyGameScreen
import com.hayidev.app.ui.screens.game.LuckyGiftScreen
import com.hayidev.app.ui.screens.game.JackpotScreen
import com.hayidev.app.ui.screens.game.RocketGameScreen
import com.hayidev.app.ui.screens.game.DiceGameScreen
import com.hayidev.app.ui.screens.game.CoinFlipScreen
import com.hayidev.app.ui.screens.game.ScratchCardScreen
import com.hayidev.app.ui.screens.game.LuckyBoxScreen
import com.hayidev.app.ui.screens.game.MiniGolfScreen
import com.hayidev.app.ui.screens.game.QuizGameScreen
import com.hayidev.app.ui.screens.game.MemoryGameScreen
import com.hayidev.app.ui.screens.onboarding.OnboardingScreen
import com.hayidev.app.ui.screens.splash.SplashScreen
import com.hayidev.app.ui.screens.premium.PremiumScreen
import com.hayidev.app.ui.screens.wallet.WalletScreen
import com.hayidev.app.ui.screens.notification.NotificationScreen
import com.hayidev.app.ui.screens.search.SearchScreen
import com.hayidev.app.ui.screens.story.StoryViewScreen
import com.hayidev.app.ui.screens.story.StoryCreateScreen
import com.hayidev.app.ui.screens.edit_profile.EditProfileScreen
import com.hayidev.app.ui.screens.followers.FollowersScreen
import com.hayidev.app.ui.screens.following.FollowingScreen
import com.hayidev.app.ui.screens.sticker.StickerStoreScreen
import com.hayidev.app.ui.screens.theme_store.ThemeStoreScreen
import com.hayidev.app.ui.screens.achievement.AchievementScreen
import com.hayidev.app.ui.screens.leaderboard.LeaderboardScreen
import com.hayidev.app.ui.screens.referral.ReferralScreen
import com.hayidev.app.ui.screens.verification.VerificationScreen
import com.hayidev.app.ui.screens.privacy.PrivacyScreen
import com.hayidev.app.ui.screens.help.HelpScreen
import com.hayidev.app.ui.screens.about.AboutScreen
import com.hayidev.app.ui.screens.feedback.FeedbackScreen
import com.hayidev.app.ui.screens.language.LanguageScreen
import com.hayidev.app.ui.screens.blocked.BlockedUsersScreen
import com.hayidev.app.ui.screens.agreement.AgreementScreen
import com.hayidev.app.ui.screens.photo_viewer.PhotoViewerScreen
import com.hayidev.app.ui.screens.video_player.VideoPlayerScreen
import android.net.Uri

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object RegionSelection : Screen("region_selection")
    data object Discover : Screen("discover")
    data object ChatList : Screen("chat_list")
    data object Live : Screen("live")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object GiftStore : Screen("gift_store")
    data object Premium : Screen("premium")
    data object Wallet : Screen("wallet")
    data object Notification : Screen("notification")
    data object Search : Screen("search")
    data object GamesHub : Screen("games_hub")
    data object CreateLiveRoom : Screen("create_live_room")
    data object StoryCreate : Screen("story_create")
    data object EditProfile : Screen("edit_profile")
    data object StickerStore : Screen("sticker_store")
    data object ThemeStore : Screen("theme_store")
    data object Achievement : Screen("achievement")
    data object Leaderboard : Screen("leaderboard")
    data object Referral : Screen("referral")
    data object Verification : Screen("verification")
    data object Privacy : Screen("privacy")
    data object Help : Screen("help")
    data object About : Screen("about")
    data object Feedback : Screen("feedback")
    data object Language : Screen("language")
    data object BlockedUsers : Screen("blocked_users")

    data object LuckyGame : Screen("lucky_game/{roomId}") {
        fun createRoute(roomId: String) = "lucky_game/$roomId"
    }
    data object LuckyGift : Screen("lucky_gift")
    data object Jackpot : Screen("jackpot")
    data object Rocket : Screen("rocket")
    data object Dice : Screen("dice")
    data object CoinFlip : Screen("coinflip")
    data object Scratch : Screen("scratch")
    data object LuckyBox : Screen("lucky_box")
    data object MiniGolf : Screen("mini_golf")
    data object Quiz : Screen("quiz")
    data object Memory : Screen("memory")

    data object Chat : Screen("chat/{roomId}") {
        fun createRoute(roomId: String) = "chat/$roomId"
    }

    data object LiveRoom : Screen("live_room/{roomId}") {
        fun createRoute(roomId: String) = "live_room/$roomId"
    }

    data object VideoCall : Screen("video_call/{roomId}") {
        fun createRoute(roomId: String) = "video_call/$roomId"
    }

    data object OtherProfile : Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }

    data object Report : Screen("report/{userId}") {
        fun createRoute(userId: String) = "report/$userId"
    }

    data object StoryView : Screen("story/{userId}") {
        fun createRoute(userId: String) = "story/$userId"
    }

    data object Followers : Screen("followers/{userId}") {
        fun createRoute(userId: String) = "followers/$userId"
    }

    data object Following : Screen("following/{userId}") {
        fun createRoute(userId: String) = "following/$userId"
    }

    data object Agreement : Screen("agreement/{type}") {
        fun createRoute(type: String) = "agreement/$type"
    }

    data object PhotoViewer : Screen("photo/{url}") {
        fun createRoute(url: String) = "photo/${Uri.encode(url)}"
    }

    data object VideoPlayer : Screen("video/{url}") {
        fun createRoute(url: String) = "video/${Uri.encode(url)}"
    }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Discover, "Keşfet", Icons.Filled.Explore, Icons.Outlined.Explore),
    BottomNavItem(Screen.ChatList, "Sohbet", Icons.Filled.Chat, Icons.Outlined.Chat),
    BottomNavItem(Screen.Live, "Canlı", Icons.Filled.Videocam, Icons.Outlined.Videocam),
    BottomNavItem(Screen.Profile, "Profil", Icons.Filled.Person, Icons.Outlined.Person),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HayiDevNavHost(
    initialDeepLink: Uri? = null
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(initialDeepLink) {
        initialDeepLink?.let { uri ->
            val path = uri.pathSegments.firstOrNull()
            when (path) {
                "chat" -> {
                    val roomId = uri.lastPathSegment
                    roomId?.let { navController.navigate(Screen.Chat.createRoute(it)) }
                }
                "live" -> {
                    val roomId = uri.lastPathSegment
                    roomId?.let { navController.navigate(Screen.LiveRoom.createRoute(it)) }
                }
                "profile" -> {
                    val userId = uri.lastPathSegment
                    userId?.let { navController.navigate(Screen.OtherProfile.createRoute(it)) }
                }
            }
        }
    }

    val showBottomBar = currentDestination?.hierarchy?.any { dest ->
        bottomNavItems.any { it.screen.route == dest.route }
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.screen.route
                        } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = { isLoggedIn ->
                        if (isLoggedIn) {
                            navController.navigate(Screen.Discover.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onOnboardingFinished = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.RegionSelection.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.RegionSelection.route) {
                RegionSelectionScreen(
                    onRegionSelected = { region ->
                        navController.navigate(Screen.Discover.route) {
                            popUpTo(Screen.RegionSelection.route) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Discover.route) {
                DiscoverScreen(
                    onMatch = { userId ->
                        navController.navigate(Screen.Chat.createRoute(userId))
                    },
                    onProfileClick = { userId ->
                        navController.navigate(Screen.OtherProfile.createRoute(userId))
                    }
                )
            }

            composable(Screen.ChatList.route) {
                ChatListScreen(
                    onChatClick = { roomId ->
                        navController.navigate(Screen.Chat.createRoute(roomId))
                    }
                )
            }

            composable(Screen.Live.route) {
                LiveScreen(
                    onRoomClick = { roomId ->
                        navController.navigate(Screen.LiveRoom.createRoute(roomId))
                    },
                    onCreateRoom = {
                        navController.navigate(Screen.CreateLiveRoom.route)
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onSettingsClick = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onGiftStoreClick = {
                        navController.navigate(Screen.GiftStore.route)
                    },
                    onEditProfileClick = {
                        navController.navigate(Screen.EditProfile.route)
                    },
                    onPremiumClick = {
                        navController.navigate(Screen.Premium.route)
                    },
                    onAchievementsClick = {
                        navController.navigate(Screen.Achievement.route)
                    },
                    onLeaderboardClick = {
                        navController.navigate(Screen.Leaderboard.route)
                    },
                    onReferralClick = {
                        navController.navigate(Screen.Referral.route)
                    },
                    onVerificationClick = {
                        navController.navigate(Screen.Verification.route)
                    },
                    onStickerStoreClick = {
                        navController.navigate(Screen.StickerStore.route)
                    },
                    onThemeStoreClick = {
                        navController.navigate(Screen.ThemeStore.route)
                    },
                    onLanguageClick = {
                        navController.navigate(Screen.Language.route)
                    },
                    onPrivacyClick = {
                        navController.navigate(Screen.Privacy.route)
                    },
                    onHelpClick = {
                        navController.navigate(Screen.Help.route)
                    },
                    onAboutClick = {
                        navController.navigate(Screen.About.route)
                    },
                    onFeedbackClick = {
                        navController.navigate(Screen.Feedback.route)
                    },
                    onBlockedUsersClick = {
                        navController.navigate(Screen.BlockedUsers.route)
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.GiftStore.route) {
                GiftStoreScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Premium.route) {
                PremiumScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Wallet.route) {
                WalletScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Notification.route) {
                NotificationScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Search.route) {
                SearchScreen(
                    onBack = { navController.popBackStack() },
                    onUserClick = { user ->
                        navController.navigate(Screen.OtherProfile.createRoute(user.id))
                    }
                )
            }

            composable(Screen.CreateLiveRoom.route) {
                CreateLiveRoomScreen(
                    onBackClick = { navController.popBackStack() },
                    onRoomCreated = { roomId ->
                        navController.navigate(Screen.LiveRoom.createRoute(roomId)) {
                            popUpTo(Screen.Live.route)
                        }
                    }
                )
            }

            composable(
                route = Screen.Chat.route,
                arguments = listOf(navArgument("roomId") { type = NavType.StringType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                ChatScreen(
                    roomId = roomId,
                    onBackClick = { navController.popBackStack() },
                    onVideoCall = { navController.navigate(Screen.VideoCall.createRoute(roomId)) }
                )
            }

            composable(
                route = Screen.LiveRoom.route,
                arguments = listOf(navArgument("roomId") { type = NavType.StringType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                LiveRoomScreen(
                    roomId = roomId,
                    onBackClick = { navController.popBackStack() },
                    onGiftClick = { },
                    onProfileClick = { userId ->
                        navController.navigate(Screen.OtherProfile.createRoute(userId))
                    }
                )
            }

            composable(
                route = Screen.VideoCall.route,
                arguments = listOf(navArgument("roomId") { type = NavType.StringType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                VideoCallScreen(
                    roomId = roomId,
                    onEndCall = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.OtherProfile.route,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                OtherProfileScreen(
                    userId = userId,
                    onBackClick = { navController.popBackStack() },
                    onMessageClick = { roomId ->
                        navController.navigate(Screen.Chat.createRoute(roomId))
                    },
                    onReportClick = {
                        navController.navigate(Screen.Report.createRoute(userId))
                    }
                )
            }

            composable(
                route = Screen.Report.route,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                ReportScreen(
                    userId = userId,
                    userName = "Kullanıcı",
                    onReportSubmitted = { navController.popBackStack() },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.GamesHub.route) {
                GamesHubScreen(
                    onGameClick = { route -> navController.navigate(route) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.LuckyGame.route,
                arguments = listOf(navArgument("roomId") { type = NavType.StringType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                LuckyGameScreen(
                    roomId = roomId,
                    onBackClick = { navController.popBackStack() },
                    onBetPlaced = { }
                )
            }

            composable(Screen.LuckyGift.route) {
                LuckyGiftScreen(
                    onBackClick = { navController.popBackStack() },
                    onGiftSent = { _, _ -> }
                )
            }

            composable(Screen.Jackpot.route) {
                JackpotScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.Rocket.route) {
                RocketGameScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.Dice.route) {
                DiceGameScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.CoinFlip.route) {
                CoinFlipScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.Scratch.route) {
                ScratchCardScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.LuckyBox.route) {
                LuckyBoxScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.MiniGolf.route) {
                MiniGolfScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.Quiz.route) {
                QuizGameScreen(onBackClick = { navController.popBackStack() })
            }

            composable(Screen.Memory.route) {
                MemoryGameScreen(onBackClick = { navController.popBackStack() })
            }

            // Story screens
            composable(
                route = Screen.StoryView.route,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                StoryViewScreen(
                    onBack = { navController.popBackStack() },
                    onUserClick = { uid ->
                        navController.navigate(Screen.OtherProfile.createRoute(uid))
                    },
                    onReply = { uid, _ ->
                        navController.navigate(Screen.Chat.createRoute(uid))
                    },
                    storyIndex = 0
                )
            }

            composable(Screen.StoryCreate.route) {
                StoryCreateScreen(
                    onBack = { navController.popBackStack() },
                    onPostStory = { navController.popBackStack() },
                    onCameraClick = { }
                )
            }

            // Edit Profile
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSaveChanges = { navController.popBackStack() },
                    onAddPhoto = { },
                    onRemovePhoto = { }
                )
            }

            // Followers / Following
            composable(
                route = Screen.Followers.route,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                FollowersScreen(
                    onBack = { navController.popBackStack() },
                    onUserClick = { uid ->
                        navController.navigate(Screen.OtherProfile.createRoute(uid))
                    },
                    userId = userId
                )
            }

            composable(
                route = Screen.Following.route,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                FollowingScreen(
                    onBack = { navController.popBackStack() },
                    onUserClick = { uid ->
                        navController.navigate(Screen.OtherProfile.createRoute(uid))
                    },
                    userId = userId
                )
            }

            // Sticker Store
            composable(Screen.StickerStore.route) {
                StickerStoreScreen(
                    onBack = { navController.popBackStack() },
                    onBuyStickerPack = { },
                    onBuyCoins = { navController.navigate(Screen.Wallet.route) }
                )
            }

            // Theme Store
            composable(Screen.ThemeStore.route) {
                ThemeStoreScreen(
                    onBack = { navController.popBackStack() },
                    onBuyTheme = { },
                    onBuyCoins = { navController.navigate(Screen.Wallet.route) }
                )
            }

            // Achievements
            composable(Screen.Achievement.route) {
                AchievementScreen(
                    onBack = { navController.popBackStack() },
                    onShareAchievement = { },
                    onClaimReward = { }
                )
            }

            // Leaderboard
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(
                    onBack = { navController.popBackStack() },
                    onUserClick = { uid ->
                        navController.navigate(Screen.OtherProfile.createRoute(uid))
                    },
                    onInviteFriends = { navController.navigate(Screen.Referral.route) }
                )
            }

            // Referral
            composable(Screen.Referral.route) {
                ReferralScreen(
                    onBack = { navController.popBackStack() },
                    onShareCode = { },
                    onCopyCode = { },
                    onLearnMore = { }
                )
            }

            // Verification
            composable(Screen.Verification.route) {
                VerificationScreen(
                    onBack = { navController.popBackStack() },
                    onUploadSelfie = { },
                    onUploadID = { },
                    onSubmit = { },
                    onContactSupport = { }
                )
            }

            // Privacy
            composable(Screen.Privacy.route) {
                PrivacyScreen(
                    onBack = { navController.popBackStack() },
                    onBlockedUsers = {
                        navController.navigate(Screen.BlockedUsers.route)
                    },
                    onMutedUsers = { },
                    onDataDownload = { },
                    onDeleteAccount = { },
                    onPrivacyPolicy = {
                        navController.navigate(Screen.Agreement.createRoute("privacy"))
                    }
                )
            }

            // Help
            composable(Screen.Help.route) {
                HelpScreen(
                    onBack = { navController.popBackStack() },
                    onContactSupport = { },
                    onReportBug = { },
                    onFeatureRequest = { }
                )
            }

            // About
            composable(Screen.About.route) {
                AboutScreen(
                    onBack = { navController.popBackStack() },
                    onOpenUrl = { },
                    onRateApp = { },
                    onShareApp = { },
                    onOpenLicenses = { }
                )
            }

            // Feedback
            composable(Screen.Feedback.route) {
                FeedbackScreen(
                    onBack = { navController.popBackStack() },
                    onSubmitFeedback = { _, _, _ -> },
                    onRateApp = { }
                )
            }

            // Language
            composable(Screen.Language.route) {
                LanguageScreen(
                    onBack = { navController.popBackStack() },
                    onLanguageSelected = { }
                )
            }

            // Blocked Users
            composable(Screen.BlockedUsers.route) {
                BlockedUsersScreen(
                    onBack = { navController.popBackStack() },
                    onUnblockUser = { },
                    onAddBlockedUser = { }
                )
            }

            // Agreement (Terms / Privacy Policy)
            composable(
                route = Screen.Agreement.route,
                arguments = listOf(navArgument("type") { type = NavType.StringType })
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: "terms"
                AgreementScreen(
                    onBack = { navController.popBackStack() },
                    type = type
                )
            }

            // Photo Viewer
            composable(
                route = Screen.PhotoViewer.route,
                arguments = listOf(navArgument("url") { type = NavType.StringType })
            ) { backStackEntry ->
                val url = Uri.decode(backStackEntry.arguments?.getString("url") ?: "")
                PhotoViewerScreen(
                    onBack = { navController.popBackStack() },
                    onShare = { },
                    onDownload = { },
                    onReport = { },
                    photoUrl = url
                )
            }

            // Video Player
            composable(
                route = Screen.VideoPlayer.route,
                arguments = listOf(navArgument("url") { type = NavType.StringType })
            ) { backStackEntry ->
                val url = Uri.decode(backStackEntry.arguments?.getString("url") ?: "")
                VideoPlayerScreen(
                    onBack = { navController.popBackStack() },
                    onShare = { },
                    onLike = { },
                    onComment = { },
                    videoUrl = url
                )
            }
        }
    }
}
