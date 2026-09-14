package com.hayidev.app.ui.screens.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

data class LuckyGiftItem(
    val id: String,
    val name: String,
    val icon: String,
    val price: Int,
    val luckyMultiple: List<Int>,
    val luckyProbability: Float,
    val color: Color,
    val comboEnabled: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckyGiftScreen(
    onBackClick: () -> Unit,
    onGiftSent: (String, Int) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var comboCount by remember { mutableIntStateOf(0) }
    var lastWin by remember { mutableStateOf<Pair<String, Int>?>(null) }
    var showWinAnimation by remember { mutableStateOf(false) }
    var coins by remember { mutableIntStateOf(2450) }

    val luckyGifts = remember {
        listOf(
            LuckyGiftItem("1", "Şans Kalbi", "💝", 10, listOf(1, 2, 3, 5), 0.3f, Color(0xFFFF4081)),
            LuckyGiftItem("2", "Şanslı Gül", "🌹", 25, listOf(1, 2, 3, 5, 10), 0.25f, Color(0xFFE91E63)),
            LuckyGiftItem("3", "Elmas Kutu", "💎", 50, listOf(2, 3, 5, 10), 0.2f, Color(0xFF00BCD4)),
            LuckyGiftItem("4", "Altın Portakal", "🍊", 100, listOf(2, 5, 10, 20), 0.15f, Color(0xFFFF9800)),
            LuckyGiftItem("5", "Şans Yıldızı", "⭐", 200, listOf(3, 5, 10, 20, 50), 0.1f, Color(0xFFFFD700)),
            LuckyGiftItem("6", "Kristal Top", "🔮", 500, listOf(5, 10, 20, 50, 100), 0.08f, Color(0xFF9C27B0)),
            LuckyGiftItem("7", "Roket", "🚀", 100, listOf(2, 3, 5, 10), 0.2f, Color(0xFFFF5722)),
            LuckyGiftItem("8", "Taç", "👑", 300, listOf(5, 10, 20, 50), 0.12f, Color(0xFFFFD700)),
            LuckyGiftItem("9", "Piano", "🎹", 75, listOf(2, 3, 5), 0.25f, Color(0xFF795548)),
            LuckyGiftItem("10", "Balon", "🎈", 15, listOf(1, 2, 3, 5), 0.35f, Color(0xFFE91E63)),
            LuckyGiftItem("11", "Kelebek", "🦋", 35, listOf(2, 3, 5), 0.28f, Color(0xFF2196F3)),
            LuckyGiftItem("12", "At", "🐴", 150, listOf(2, 5, 10, 20), 0.18f, Color(0xFF795548))
        )
    }

    val tabs = listOf("Şanslı Hediyeler", "Kombo", "Kazançlar")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎁 Şanslı Hediyeler", fontWeight = FontWeight.Bold) },
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
                        Text("🪙", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$coins", color = GoldCoin, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0d0d2b), Color(0xFF1a1a3e))
                    )
                )
        ) {
            // Combo Banner
            if (comboCount > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            comboCount >= 50 -> Color(0xFFFF1744).copy(alpha = 0.3f)
                            comboCount >= 20 -> Color(0xFFFF9800).copy(alpha = 0.3f)
                            else -> AccentPurple.copy(alpha = 0.2f)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("🔥 KOMBO x$comboCount", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                when {
                                    comboCount >= 50 -> "eFSANEvi Bonus! +%${comboCount}"
                                    comboCount >= 20 -> "Mega Bonus! +%${comboCount / 2}"
                                    else -> "Bonus: +%${comboCount * 2}"
                                },
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                        if (comboCount >= 10) {
                            Text("💰", fontSize = 32.sp)
                        }
                    }
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = GoldCoin
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }) {
                        Text(title, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    // Gift Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(luckyGifts) { gift ->
                            LuckyGiftCard(
                                gift = gift,
                                affordable = coins >= gift.price,
                                onClick = {
                                    if (coins >= gift.price) {
                                        coins -= gift.price
                                        comboCount++

                                        val isLucky = Random.nextFloat() < gift.luckyProbability
                                        if (isLucky) {
                                            val multiple = gift.luckyMultiple.random()
                                            val win = gift.price * multiple
                                            coins += win
                                            lastWin = gift.name to win
                                            showWinAnimation = true
                                        } else {
                                            lastWin = null
                                        }
                                        onGiftSent(gift.id, gift.price)
                                    }
                                }
                            )
                        }
                    }
                }
                1 -> {
                    // Combo Info
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Kombo Kuralları", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        val comboLevels = listOf(
                            "10 hediye" to "1.2x bonus",
                            "25 hediye" to "1.5x bonus",
                            "50 hediye" to "2x bonus",
                            "100 hediye" to "3x bonus",
                            "200 hediye" to "5x bonus"
                        )

                        comboLevels.forEach { (count, bonus) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.05f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(count, color = Color.White)
                                    Text(bonus, color = GoldCoin, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Şans Oranları", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        luckyGifts.take(6).forEach { gift ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${gift.icon} ${gift.name}", color = Color.White, fontSize = 13.sp)
                                Text(
                                    "%${(gift.luckyProbability * 100).toInt()}",
                                    color = if (gift.luckyProbability >= 0.25f) Success else Warning,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
                2 -> {
                    // Win History
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Son Kazançlar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(16.dp))

                        if (lastWin != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Success.copy(alpha = 0.2f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("🎉", fontSize = 48.sp)
                                    Text("${lastWin!!.first}", color = Color.White, fontSize = 16.sp)
                                    Text(
                                        "+${lastWin!!.second} Coin",
                                        color = GoldCoin,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            Text("Henüz kazanç yok", color = Color.White.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }

    if (showWinAnimation) {
        AlertDialog(
            onDismissRequest = { showWinAnimation = false },
            containerColor = Color(0xFF1e1e3f),
            title = { Text("🎉 ŞANSLI HEDİYE!", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tebrikler!", color = Color.White, fontSize = 16.sp)
                    Text(
                        "+${lastWin?.second} Coin",
                        color = GoldCoin,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showWinAnimation = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldCoin),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Devam", color = Color.Black)
                }
            }
        )
    }
}

@Composable
fun LuckyGiftCard(
    gift: LuckyGiftItem,
    affordable: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (affordable) gift.color.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.1f)
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(gift.icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                gift.name,
                fontSize = 11.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🪙", fontSize = 10.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    "${gift.price}",
                    fontSize = 12.sp,
                    color = GoldCoin,
                    fontWeight = FontWeight.Bold
                )
            }
            if (gift.luckyMultiple.max() > 1) {
                Text(
                    "x${gift.luckyMultiple.max()}!",
                    fontSize = 9.sp,
                    color = Success,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
