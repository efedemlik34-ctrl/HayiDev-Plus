package com.hayidev.app.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*

data class GameInfo(
    val id: String,
    val name: String,
    val icon: String,
    val description: String,
    val gradient: List<Color>,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesHubScreen(
    onGameClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }

    val games = listOf(
        GameInfo("lucky", "Şans Oyunu", "🎰", "Çarkıfelek ile büyük ödüller kazan!", listOf(Color(0xFFFF6B35), Color(0xFFFF3366)), "lucky_game"),
        GameInfo("jackpot", "Jackpot", "💎", "Büyük ikramiyeyi kazan!", listOf(Color(0xFF7C4DFF), Color(0xFF536DFE)), "jackpot"),
        GameInfo("rocket", "Roket", "🚀", "Çökmeden önce kasıl!", listOf(Color(0xFFFF4081), Color(0xFFE91E63)), "rocket"),
        GameInfo("dice", "Zar", "🎲", "Zar at, şansını dene!", listOf(Color(0xFF2196F3), Color(0xFF1565C0)), "dice"),
        GameInfo("coinflip", "Yazı Tura", "🪙", "Taraflarını seç, x2 kazan!", listOf(Color(0xFFFFD700), Color(0xFFFFA000)), "coinflip"),
        GameInfo("scratch", "Kazı Kazan", "🎫", "Kazandıkça kazan!", listOf(Color(0xFF9C27B0), Color(0xFF7B1FA2)), "scratch"),
        GameInfo("luckybox", "Şans Kutusu", "🎁", "Sürpriz ödüller seni bekliyor!", listOf(Color(0xFFE91E63), Color(0xFFAD1457)), "lucky_box"),
        GameInfo("golf", "Mini Golf", "⛳", "En az vuruşla deliğe sok!", listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)), "mini_golf"),
        GameInfo("quiz", "Bilgi Yarışması", "🧠", "Bilgini test et, coin kazan!", listOf(Color(0xFF00BCD4), Color(0xFF00838F)), "quiz"),
        GameInfo("memory", "Hafıza", "🧩", "Kartları eşleştir!", listOf(Color(0xFFFF9800), Color(0xFFE65100)), "memory")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎮", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Oyunlar", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Geri")
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .background(GoldCoin.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙 $coins", color = GoldCoin, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Coin balance card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1a1a3e))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Toplam Bakiye", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                        Text(
                            "$coins Coin",
                            color = GoldCoin,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldCoin),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.Add, null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Satın Al", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Daily Rewards
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Success.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎁", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Günlük Ödülünü Al!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Her gün giriş yap, ödül kazan!", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Success),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Al", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Games Grid
            Text(
                "Oyunları Keşfet",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(games) { game ->
                    GameCard(
                        game = game,
                        onClick = { onGameClick(game.route) }
                    )
                }
            }
        }
    }
}

@Composable
fun GameCard(game: GameInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(game.gradient))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(game.icon, fontSize = 32.sp)

                Column {
                    Text(
                        game.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        game.description,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }
}
