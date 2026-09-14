package com.hayidev.app.ui.screens.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JackpotScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var jackpotPool by remember { mutableIntStateOf(285000) }
    var betAmount by remember { mutableIntStateOf(50) }
    var isPlaying by remember { mutableStateOf(false) }
    var reelIndex by remember { mutableIntStateOf(0) }
    var reels by remember { mutableStateOf(listOf("🍒", "🍒", "🍒")) }
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }
    var selectedTab by remember { mutableIntStateOf(0) }

    val symbols = listOf("🍒", "🍋", "🍊", "🍇", "💎", "7️⃣", "🔔", "⭐")
    val betOptions = listOf(10, 25, 50, 100, 200, 500)

    val infiniteTransition = rememberInfiniteTransition(label = "reel")
    val reelRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "reelSpin"
    )

    fun spinSlots() {
        if (coins < betAmount || isPlaying) return
        coins -= betAmount
        isPlaying = true
        jackpotPool += (betAmount * 0.1f).toInt()

        val results = List(3) { symbols.random() }
        val finalReels = mutableListOf<String>()

        // Her makara sırayla durur
        for (i in 0..2) {
            val delay = (i + 1) * 600L
            val idx = i
            kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
                kotlinx.coroutines.delay(delay)
                if (idx < results.size) {
                    finalReels.add(results[idx])
                    reels = finalReels.toList()
                }
                if (idx == 2) {
                    // Sonuç hesapla
                    val isTriple = results[0] == results[1] && results[1] == results[2]
                    val isDouble = results[0] == results[1] || results[1] == results[2] || results[0] == results[2]

                    winAmount = when {
                        isTriple && results[0] == "💎" -> betAmount * 100 // JACKPOT
                        isTriple && results[0] == "7️⃣" -> betAmount * 50
                        isTriple && results[0] == "🍒" -> betAmount * 20
                        isTriple -> betAmount * 10
                        isDouble -> betAmount * 2
                        else -> 0
                    }

                    if (winAmount > 0) {
                        coins += winAmount
                    }
                    showResult = true
                    isPlaying = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎰 Slot Makinesi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.Close, "Kapat")
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .background(GoldCoin.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙 $coins", color = GoldCoin, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(Color(0xFF1a0a2e), Color(0xFF2d1b4e), Color(0xFF1a0a2e)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Jackpot Banner
            Card(
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2d1b4e))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("💎 JACKPOT", color = GoldCoin, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "$jackpotPool",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Coin Havuzu", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Slot Machine Body
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(200.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0d0d2b))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    reels.forEachIndexed { index, symbol ->
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isPlaying) Color(0xFF2d1b4e)
                                    else Color(0xFF1a1a3e)
                                )
                                .then(
                                    if (!isPlaying && index > 0 && symbol == reels.getOrNull(index - 1))
                                        Modifier.background(Success.copy(alpha = 0.2f))
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isPlaying && reelIndex < index) "🎰" else symbol,
                                fontSize = 40.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Win Display
            if (winAmount > 0 && !isPlaying) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Success.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "🎉 +$winAmount Coin Kazandın!",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        color = Success,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bet Selection
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(betOptions.size) { index ->
                    val bet = betOptions[index]
                    FilterChip(
                        selected = betAmount == bet,
                        onClick = { betAmount = bet },
                        label = { Text("$bet", fontWeight = if (betAmount == bet) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentPurple.copy(alpha = 0.3f),
                            selectedLabelColor = AccentPurple
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Spin Button
            Button(
                onClick = { spinSlots() },
                modifier = Modifier.width(220.dp).height(60.dp),
                enabled = coins >= betAmount && !isPlaying,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (coins >= betAmount) AccentPurple else Color.Gray
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                if (isPlaying) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 3.dp)
                } else {
                    Text("🎰 ÇEVİR ($betAmount)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Paytable
            Card(
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("💰 Kazanç Tablosu", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    val paytable = listOf(
                        "💎💎💎" to "x100 JACKPOT",
                        "7️⃣7️⃣7️⃣" to "x50",
                        "🍒🍒🍒" to "x20",
                        "Aynı 3'lü" to "x10",
                        "2'li Eşleşme" to "x2"
                    )
                    paytable.forEach { (combo, reward) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(combo, fontSize = 12.sp, color = Color.White)
                            Text(reward, fontSize = 12.sp, color = GoldCoin, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            containerColor = Color(0xFF1e1e3f),
            title = { Text(if (winAmount > 0) "🎉 JACKPOT!" else "😔 Tekrar Dene", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(reels.joinToString(" "), fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (winAmount > 0) {
                        Text("+$winAmount Coin", color = GoldCoin, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text("Şansını tekrar dene!", color = Color.White.copy(alpha = 0.7f))
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showResult = false }, colors = ButtonDefaults.buttonColors(containerColor = GoldCoin), shape = RoundedCornerShape(12.dp)) {
                    Text("Devam", color = Color.Black)
                }
            }
        )
    }
}
