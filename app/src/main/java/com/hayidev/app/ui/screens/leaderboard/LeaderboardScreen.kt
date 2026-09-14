package com.hayidev.app.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LeaderboardUser(
    val id: String,
    val rank: Int,
    val username: String,
    val avatarUrl: String,
    val score: Int,
    val country: String,
    val isFriend: Boolean,
    val change: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onBack: () -> Unit = {},
    onUserClick: (String) -> Unit = {},
    onInviteFriends: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedPeriod by remember { mutableIntStateOf(0) }

    val tabs = listOf("Global", "Ülke", "Arkadaşlar")
    val periods = listOf("Haftalık", "Aylık", "Tüm Zamanlar")

    val leaderboard = listOf(
        LeaderboardUser("1", 1, "ahmet_dev", "https://i.pravatar.cc/150?img=1", 15420, "TR", false, 0),
        LeaderboardUser("2", 2, "elif_dsgn", "https://i.pravatar.cc/150?img=2", 14350, "TR", true, 2),
        LeaderboardUser("3", 3, "mehmet_app", "https://i.pravatar.cc/150?img=3", 12890, "DE", false, -1),
        LeaderboardUser("4", 4, "zeynep_photo", "https://i.pravatar.cc/150?img=4", 11200, "TR", true, 5),
        LeaderboardUser("5", 5, "ali_code", "https://i.pravatar.cc/150?img=5", 10500, "US", false, 3),
        LeaderboardUser("6", 6, "sude_writes", "https://i.pravatar.cc/150?img=6", 9800, "TR", true, -2),
        LeaderboardUser("7", 7, "berk_fitness", "https://i.pravatar.cc/150?img=7", 9200, "UK", false, 1),
        LeaderboardUser("8", 8, "ayşe_art", "https://i.pravatar.cc/150?img=8", 8500, "TR", false, 0),
        LeaderboardUser("9", 9, "can_music", "https://i.pravatar.cc/150?img=9", 7800, "DE", true, 4),
        LeaderboardUser("10", 10, "deniz_tech", "https://i.pravatar.cc/150?img=10", 7200, "TR", false, -3)
    )

    val myRank = LeaderboardUser("me", 23, "kullanıcı_adı", "https://i.pravatar.cc/150?img=11", 3200, "TR", false, 8)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Liderlik Tablosu", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = onInviteFriends) {
                        Icon(Icons.Filled.PersonAdd, contentDescription = "Arkadaş Davet Et")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6C63FF))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Period selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                periods.forEachIndexed { index, period ->
                    FilterChip(
                        selected = selectedPeriod == index,
                        onClick = { selectedPeriod = index },
                        label = { Text(period, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6C63FF),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(tab) }
                    )
                }
            }

            // Top 3 podium
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // 2nd place
                PodiumItem(
                    user = leaderboard[1],
                    height = 80.dp,
                    medalColor = Color(0xFFC0C0C0),
                    onClick = { onUserClick(leaderboard[1].id) }
                )

                // 1st place
                PodiumItem(
                    user = leaderboard[0],
                    height = 100.dp,
                    medalColor = Color(0xFFFFD700),
                    onClick = { onUserClick(leaderboard[0].id) }
                )

                // 3rd place
                PodiumItem(
                    user = leaderboard[2],
                    height = 60.dp,
                    medalColor = Color(0xFFCD7F32),
                    onClick = { onUserClick(leaderboard[2].id) }
                )
            }

            // Leaderboard list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(leaderboard.drop(3)) { index, user ->
                    LeaderboardItem(
                        user = user,
                        onClick = { onUserClick(user.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PodiumItem(
    user: LeaderboardUser,
    height: androidx.compose.ui.unit.Dp,
    medalColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(medalColor, medalColor.copy(alpha = 0.7f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${user.rank}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user.username,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = "${user.score}",
            fontSize = 11.sp,
            color = Color(0xFF6C63FF)
        )
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(medalColor.copy(alpha = 0.3f), medalColor.copy(alpha = 0.1f))
                    )
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "${user.rank}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = medalColor,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun LeaderboardItem(
    user: LeaderboardUser,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (user.isFriend)
                Color(0xFF6C63FF).copy(alpha = 0.05f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF6C63FF).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${user.rank}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF6C63FF)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF6C63FF), Color(0xFF9C27B0))
                        )
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Username & country
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(user.username, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    if (user.isFriend) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Filled.Group,
                            contentDescription = "Arkadaş",
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text("${user.country}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // Score
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${user.score}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF6C63FF)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    when {
                        user.change > 0 -> {
                            Icon(
                                Icons.Filled.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(12.dp)
                            )
                            Text("+${user.change}", fontSize = 10.sp, color = Color(0xFF4CAF50))
                        }
                        user.change < 0 -> {
                            Icon(
                                Icons.Filled.TrendingDown,
                                contentDescription = null,
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(12.dp)
                            )
                            Text("${user.change}", fontSize = 10.sp, color = Color(0xFFE53935))
                        }
                        else -> {
                            Text("-", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
