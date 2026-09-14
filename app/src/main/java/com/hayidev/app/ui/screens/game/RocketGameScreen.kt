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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketGameScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var betAmount by remember { mutableIntStateOf(50) }
    var multiplier by remember { mutableFloatStateOf(1.0f) }
    var isFlying by remember { mutableStateOf(false) }
    var hasCashedOut by remember { mutableStateOf(false) }
    var cashOutMultiplier by remember { mutableFloatStateOf(0f) }
    var crashPoint by remember { mutableFloatStateOf(0f) }
    var winAmount by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var rocketY by remember { mutableFloatStateOf(0f) }
    var history by remember { mutableStateOf(listOf<Float>()) }

    val betOptions = listOf(10, 25, 50, 100, 200, 500)

    val infiniteTransition = rememberInfiniteTransition(label = "rocket")

    fun startGame() {
        if (coins < betAmount) return
        coins -= betAmount
        isFlying = true
        hasCashedOut = false
        cashOutMultiplier = 0f
        winAmount = 0
        multiplier = 1.0f

        // Crash noktasını belirle (rastgele)
        crashPoint = 1.0f + Random.nextFloat() * 9.0f // 1.0x - 10.0x arası

        // Multiplier artışı
        kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
            while (isFlying && multiplier < crashPoint) {
                kotlinx.coroutines.delay(50)
                multiplier += 0.02f + (multiplier * 0.01f)
                rocketY = (multiplier - 1f) / (crashPoint - 1f)

                if (multiplier >= crashPoint) {
                    isFlying = false
                    if (!hasCashedOut) {
                        // Çökme
                        showResult = true
                    }
                    history = listOf(crashPoint) + history.take(19)
                }
            }
        }
    }

    fun cashOut() {
        if (!isFlying || hasCashedOut) return
        hasCashedOut = true
        cashOutMultiplier = multiplier
        winAmount = (betAmount * multiplier).toInt()
        coins += winAmount
        isFlying = false
        showResult = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🚀 Roket Oyunu", fontWeight = FontWeight.Bold) },
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
                        Text("🪙 $coins", color = GoldCoin, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(Color(0xFF0a0a1a), Color(0xFF1a0a2e), Color(0xFF0a0a1a)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Multiplier Display
            Card(
                modifier = Modifier.fillMaxWidth(0.92f).height(200.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                if (isFlying && !hasCashedOut) listOf(Color(0xFF1a3a1a), Color(0xFF0d2b0d))
                                else if (hasCashedOut) listOf(Color(0xFF1a3a1a), Color(0xFF0d2b0d))
                                else listOf(Color(0xFF2b0d0d), Color(0xFF1a0a0a))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isFlying) {
                            Text("🚀", fontSize = (40 + multiplier * 5).toInt().coerceAtMost(80).sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "x${String.format("%.2f", multiplier)}",
                                color = if (hasCashedOut) GoldCoin else Success,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (hasCashedOut) {
                                Text("KASıldı! +$winAmount", color = GoldCoin, fontSize = 16.sp)
                            }
                        } else if (showResult && !hasCashedOut) {
                            Text("💥", fontSize = 64.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "x${String.format("%.2f", crashPoint)}'te Çöktü!",
                                color = Error,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text("🚀", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Roketi Fırlat!", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // History Row
            if (history.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                ) {
                    history.take(8).forEach { crash ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (crash >= 3) Success.copy(alpha = 0.2f) else Error.copy(alpha = 0.2f)
                        ) {
                            Text(
                                "x${String.format("%.1f", crash)}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = if (crash >= 3) Success else Error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bet Selection
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(betOptions.size) { index ->
                    val bet = betOptions[index]
                    FilterChip(
                        selected = betAmount == bet,
                        onClick = { if (!isFlying) betAmount = bet },
                        label = { Text("$bet") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentBlue.copy(alpha = 0.3f),
                            selectedLabelColor = AccentBlue
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            if (!isFlying && !showResult) {
                Button(
                    onClick = { startGame() },
                    modifier = Modifier.width(240.dp).height(56.dp),
                    enabled = coins >= betAmount,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("🚀 UÇUR ($betAmount)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else if (isFlying && !hasCashedOut) {
                Button(
                    onClick = { cashOut() },
                    modifier = Modifier.width(240.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Success),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        "💰 KASIL x${String.format("%.2f", multiplier)}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else if (showResult) {
                Button(
                    onClick = { showResult = false; multiplier = 1.0f },
                    modifier = Modifier.width(240.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldCoin),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        if (hasCashedOut) "💰 +$winAmount" else "Tekrar Dene",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info
            Card(
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("📋 Nasıl Oynanır", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("• Bahisini seç ve roketi fırlat", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    Text("• Multiplier artarken kasıl", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    Text("• Çökmeden önce kasılmazsan kaybedersin", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    Text("• Rastgele crash noktası: 1.0x - 10.0x", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                }
            }
        }
    }
}
