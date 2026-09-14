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
fun DiceGameScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var betAmount by remember { mutableIntStateOf(50) }
    var dice1 by remember { mutableIntStateOf(1) }
    var dice2 by remember { mutableIntStateOf(1) }
    var isRolling by remember { mutableStateOf(false) }
    var betType by remember { mutableStateOf("high") }
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }
    var resultMessage by remember { mutableStateOf("") }

    val betOptions = listOf(10, 25, 50, 100, 200, 500)
    val betTypes = listOf("high" to "Büyük (8-12)", "low" to "Küçük (3-7)", "seven" to "Yedi", "double" to "Çift")

    fun rollDice() {
        if (coins < betAmount || isRolling) return
        coins -= betAmount
        isRolling = true

        kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
            // Zar animasyonu
            for (i in 0..15) {
                dice1 = Random.nextInt(1, 7)
                dice2 = Random.nextInt(1, 7)
                kotlinx.coroutines.delay(80)
            }

            val total = dice1 + dice2
            val isDouble = dice1 == dice2
            val isSeven = total == 7

            val isWin = when (betType) {
                "high" -> total in 8..12
                "low" -> total in 3..7
                "seven" -> isSeven
                "double" -> isDouble
                else -> false
            }

            val multiplier = when (betType) {
                "high" -> 2
                "low" -> 2
                "seven" -> 5
                "double" -> if (isDouble) 3 else 0
                else -> 0
            }

            winAmount = if (isWin) betAmount * multiplier else 0
            if (winAmount > 0) coins += winAmount

            resultMessage = when {
                isWin && betType == "seven" -> "🎯 Tam Yedi! x5!"
                isWin && betType == "double" -> "🎲 Çift! x3!"
                isWin -> "✅ Kazandın!"
                else -> "❌ Kaybettin!"
            }

            showResult = true
            isRolling = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎲 Zar Oyunu", fontWeight = FontWeight.Bold) },
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
                .background(Brush.verticalGradient(listOf(Color(0xFF0d1b2a), Color(0xFF1b2838), Color(0xFF0d1b2a)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Dice Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dice 1
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getDiceFace(dice1),
                        fontSize = 48.sp
                    )
                }

                Text("+", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                // Dice 2
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getDiceFace(dice2),
                        fontSize = 48.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Toplam: ", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
                    Text(
                        "${dice1 + dice2}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bet Type Selection
            Text("Bahis Türü", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                betTypes.forEach { (type, label) ->
                    FilterChip(
                        selected = betType == type,
                        onClick = { betType = type },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentBlue.copy(alpha = 0.3f),
                            selectedLabelColor = AccentBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bet Amount
            Text("Bahis Miktarı", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
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

            // Roll Button
            Button(
                onClick = { rollDice() },
                modifier = Modifier.width(220.dp).height(60.dp),
                enabled = coins >= betAmount && !isRolling,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (coins >= betAmount) AccentBlue else Color.Gray
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                if (isRolling) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 3.dp)
                } else {
                    Text("🎲 ZAR AT ($betAmount)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multiplier Table
            Card(
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("💰 Çarpanlar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Büyük/Küçük", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        Text("x2", color = GoldCoin, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Çift", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        Text("x3", color = GoldCoin, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Yedi", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        Text("x5", color = GoldCoin, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            containerColor = Color(0xFF1e2d3f),
            title = { Text(resultMessage, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎲 $dice1 + $dice2 = ${dice1 + dice2}", fontSize = 20.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
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

private fun getDiceFace(value: Int): String {
    return when (value) {
        1 -> "⚀"
        2 -> "⚁"
        3 -> "⚂"
        4 -> "⚃"
        5 -> "⚄"
        6 -> "⚅"
        else -> "🎲"
    }
}
