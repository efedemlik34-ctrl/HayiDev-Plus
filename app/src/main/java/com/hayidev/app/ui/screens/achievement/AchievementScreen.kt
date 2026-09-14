package com.hayidev.app.ui.screens.achievement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val progress: Float,
    val maxProgress: Int,
    val currentProgress: Int,
    val isUnlocked: Boolean,
    val unlockedDate: String?,
    val reward: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementScreen(
    onBack: () -> Unit = {},
    onShareAchievement: (String) -> Unit = {},
    onClaimReward: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val categories = listOf("Tümü", "Sosyal", "İçerik", "Aktivite", "Özel")

    val achievements = listOf(
        Achievement("1", "İlk Adım", "İlk profil fotoğrafını yükle", Icons.Filled.Person, 1f, 1, 1, true, "12 Oca 2024", "10 Coin", "Aktivite"),
        Achievement("2", "Sosyal Kelebek", "100 takipçiye ulaş", Icons.Filled.People, 0.73f, 100, 73, false, null, "50 Coin", "Sosyal"),
        Achievement("3", "İçerik Kralı", "50 gönderi paylaş", Icons.Filled.Article, 0.6f, 50, 30, false, null, "100 Coin", "İçerik"),
        Achievement("4", "Popüler", "500 beğeni al", Icons.Filled.Favorite, 0.45f, 500, 225, false, null, "200 Coin", "Sosyal"),
        Achievement("5", "Yorum Ustası", "100 yorum yap", Icons.Filled.Comment, 0.85f, 100, 85, false, null, "75 Coin", "İçerik"),
        Achievement("6", "Günlük Giriş", "7 gün üst üste giriş yap", Icons.Filled.Event, 0.57f, 7, 4, false, null, "30 Coin", "Aktivite"),
        Achievement("7", "Premium Üye", "Premium üyelik satın al", Icons.Filled.Diamond, 0f, 1, 0, false, null, "Özel Rozet", "Özel"),
        Achievement("8", "Doğrulanmış", "Hesabını doğrula", Icons.Filled.Verified, 1f, 1, 1, true, "15 Oca 2024", "50 Coin", "Özel"),
        Achievement("9", "İlk Hikaye", "İlk hikayeni paylaş", Icons.Filled.PhotoLibrary, 1f, 1, 1, true, "13 Oca 2024", "15 Coin", "İçerik"),
        Achievement("10", "Eğlence Sever", "10 farklı sticker kullan", Icons.Filled.EmojiEmotions, 0.4f, 10, 4, false, null, "25 Coin", "Aktivite"),
        Achievement("11", "Bağlantı Kur", "10 kişiyle mesajlaş", Icons.Filled.Chat, 0.9f, 10, 9, false, null, "40 Coin", "Sosyal"),
        Achievement("12", "Trendsetter", "1 viral paylaşım yap", Icons.Filled.TrendingUp, 0f, 1, 0, false, null, "500 Coin", "İçerik")
    )

    val filteredAchievements = when (selectedTab) {
        0 -> achievements
        1 -> achievements.filter { it.category == "Sosyal" }
        2 -> achievements.filter { it.category == "İçerik" }
        3 -> achievements.filter { it.category == "Aktivite" }
        4 -> achievements.filter { it.category == "Özel" }
        else -> achievements
    }

    val unlockedCount = achievements.count { it.isUnlocked }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Başarımlarım", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
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
            // Stats header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF).copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$unlockedCount",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6C63FF)
                        )
                        Text("Açıldı", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${achievements.size - unlockedCount}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800)
                        )
                        Text("Kilitli", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${achievements.size}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        Text("Toplam", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Category tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                edgePadding = 16.dp
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(category) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredAchievements) { achievement ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (achievement.isUnlocked)
                                Color(0xFF4CAF50).copy(alpha = 0.05f)
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (achievement.isUnlocked)
                                            Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA000)))
                                        else
                                            Brush.linearGradient(listOf(Color(0xFF9E9E9E), Color(0xFF616161)))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    achievement.icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(achievement.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    if (achievement.isUnlocked) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            Icons.Filled.CheckCircle,
                                            contentDescription = "Açıldı",
                                            tint = Color(0xFF4CAF50),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    achievement.description,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (!achievement.isUnlocked) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LinearProgressIndicator(
                                        progress = { achievement.progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = Color(0xFF6C63FF),
                                        trackColor = Color(0xFFE0E0E0)
                                    )
                                    Text(
                                        "${achievement.currentProgress}/${achievement.maxProgress}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                if (achievement.unlockedDate != null) {
                                    Text(
                                        "Açıldı: ${achievement.unlockedDate}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF4CAF50),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.MonetizationOn,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(achievement.reward, fontSize = 11.sp, color = Color(0xFFFFD700))
                                }

                                if (achievement.isUnlocked) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    IconButton(
                                        onClick = { onShareAchievement(achievement.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Share,
                                            contentDescription = "Paylaş",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
