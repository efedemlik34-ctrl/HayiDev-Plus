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
import com.hayidev.app.ui.screens.login.LoginScreen
import com.hayidev.app.ui.screens.profile.OtherProfileScreen
import com.hayidev.app.ui.screens.profile.ProfileScreen
import com.hayidev.app.ui.screens.settings.SettingsScreen
import com.hayidev.app.ui.screens.video.VideoCallScreen
import com.hayidev.app.ui.screens.region.RegionSelectionScreen
import com.hayidev.app.ui.screens.report.ReportScreen

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Discover : Screen("discover")
    data object ChatList : Screen("chat_list")
    data object Live : Screen("live")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object GiftStore : Screen("gift_store")
    data object CreateLiveRoom : Screen("create_live_room")
    data object RegionSelection : Screen("region_selection")

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
fun HayiDevNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

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
            startDestination = Screen.Discover.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Discover.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
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
                com.hayidev.app.ui.screens.live.LiveScreen(
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
                    onGiftClick = { /* Show gift dialog */ },
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
        }
    }
}
