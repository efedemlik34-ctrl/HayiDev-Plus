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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinFlipScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var betAmount by remember { mutableIntStateOf(50) }
    var chosenSide by remember { mutableStateOf("heads") }
    var isFlipping by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }
    var resultSide by remember { mutableStateOf("heads") }
    var streak by remember { mutableIntStateOf(0) }
    var totalWins by remember { mutableIntStateOf(0) }
    var totalLosses by remember { mutableIntStateOf(0) }

    val betOptions = listOf(10, 25, 50, 100, 200, 500)

    var coinRotation by remember { mutableFloatStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = coinRotation,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "coin"
    )

    fun flipCoin() {
        if (coins < betAmount || isFlipping) return
        coins -= betAmount
        isFlipping = true

        resultSide = if (Random.nextBoolean()) "heads" else "tails"

        coinRotation += 1800f + if (resultSide == "tails") 180f else 0f

        kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
            kotlinx.coroutines.delay(1100)

            val isWin = chosenSide == resultSide
            winAmount = if (isWin) betAmount * 2 else 0
            if (isWin) {
                coins += winAmount
                streak++
                totalWins++
            } else {
                streak = 0
                totalLosses++
            }

            showResult = true
            isFlipping = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🪙 Yazı Tura", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Filled.Close, "Kapat") }
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
                .background(Brush.verticalGradient(listOf(Color(0xFF1a1a2e), Color(0xFF16213e), Color(0xFF0f3460)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBadge("Kazan", "$totalWins", Success)
                StatBadge("Seri", "x$streak", AccentPurple)
                StatBadge("Kayıp", "$totalLosses", Error)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Coin
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            if (resultSide == "heads" || !showResult) listOf(Color(0xFFFFD700), Color(0xFFFFA000))
                            else listOf(Color(0xFFC0C0C0), Color(0xFF808080))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (showResult) {
                        if (resultSide == "heads") "👑" else "🌟"
                    } else "🪙",
                    fontSize = 72.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Side Selection
            Text("Tarafını Seç", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SideButton(
                    label = "Yazı",
                    icon = "👑",
                    isSelected = chosenSide == "heads",
                    onClick = { chosenSide = "heads" },
                    enabled = !isFlipping
                )
                SideButton(
                    label = "Tura",
                    icon = "🌟",
                    isSelected = chosenSide == "tails",
                    onClick = { chosenSide = "tails" },
                    enabled = !isFlipping
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bet Amount
            Text("Bahis", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(betOptions.size) { index ->
                    val bet = betOptions[index]
                    FilterChip(
                        selected = betAmount == bet,
                        onClick = { betAmount = bet },
                        label = { Text("$bet") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldCoin.copy(alpha = 0.2f),
                            selectedLabelColor = GoldCoin
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Flip Button
            Button(
                onClick = { flipCoin() },
                modifier = Modifier.width(240.dp).height(60.dp),
                enabled = coins >= betAmount && !isFlipping,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (coins >= betAmount) GoldCoin else Color.Gray
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                if (isFlipping) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black, strokeWidth = 3.dp)
                } else {
                    Text("🪙 AT ($betAmount)", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("x2 Çarpan ile Kazan!", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
        }
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            containerColor = Color(0xFF1e1e3f),
            title = {
                Text(
                    if (winAmount > 0) "🎉 Kazandın!" else "😔 Kaybettin!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        if (resultSide == "heads") "👑 YAZI" else "🌟 TURA",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                    if (winAmount > 0) {
                        Text("+$winAmount Coin", color = GoldCoin, fontSize = 28.sp, fontWeight = FontWeight.Bold)
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

@Composable
fun StatBadge(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
    }
}

@Composable
fun SideButton(label: String, icon: String, isSelected: Boolean, onClick: () -> Unit, enabled: Boolean) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.width(120.dp).height(60.dp),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) GoldCoin.copy(alpha = 0.2f) else Color.Transparent,
            contentColor = if (isSelected) GoldCoin else Color.White
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isSelected) GoldCoin else Color.White.copy(alpha = 0.3f)
            )
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 20.sp)
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
