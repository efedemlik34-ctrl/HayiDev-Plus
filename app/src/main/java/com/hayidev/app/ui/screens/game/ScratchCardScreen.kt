package com.hayidev.app.ui.screens.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
fun ScratchCardScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var cardsRemaining by remember { mutableIntStateOf(3) }
    var purchased by remember { mutableStateOf(false) }
    var grid by remember { mutableStateOf(List(9) { ScratchCell("", false) }) }
    var revealedCount by remember { mutableIntStateOf(0) }
    var totalWin by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var cardPrizes by remember { mutableStateOf(emptyList<Int>()) }

    val cardCost = 50

    fun purchaseCard() {
        if (coins < cardCost || cardsRemaining <= 0) return
        coins -= cardCost
        cardsRemaining--
        revealedCount = 0
        totalWin = 0
        purchased = true

        // Rastgele ödüller oluştur (3 sütunlu kazı kazan)
        cardPrizes = List(9) {
            when (Random.nextInt(100)) {
                in 0..30 -> 0           // %31 boş
                in 31..55 -> 10         // %25 - 10 coin
                in 56..72 -> 25         // %17 - 25 coin
                in 73..84 -> 50         // %12 - 50 coin
                in 85..92 -> 100        // %8 - 100 coin
                in 93..97 -> 250        // %5 - 250 coin
                else -> 500             // %2 - 500 coin
            }
        }

        grid = List(9) { ScratchCell(getScratchIcon(cardPrizes[it]), false) }
    }

    fun revealCell(index: Int) {
        if (grid[index].revealed || !purchased) return
        grid = grid.toMutableList().apply { this[index] = grid[index].copy(revealed = true) }
        revealedCount++

        val prize = cardPrizes[index]
        totalWin += prize

        if (revealedCount >= 9) {
            coins += totalWin
            showResult = true
            purchased = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎫 Kazı Kazan", fontWeight = FontWeight.Bold) },
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
                .background(Brush.verticalGradient(listOf(Color(0xFF1a0a2e), Color(0xFF2d1b4e), Color(0xFF1a0a2e)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Remaining cards
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎫 Kalan Kart: ", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                    Text("$cardsRemaining", color = AccentPurple, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scratch Grid (3x3)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2d1b4e))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (row in 0..2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (col in 0..2) {
                                val index = row * 3 + col
                                val cell = grid[index]

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (cell.revealed) {
                                                if (cardPrizes[index] > 0) Success.copy(alpha = 0.2f)
                                                else Color.White.copy(alpha = 0.05f)
                                            } else {
                                                Brush.verticalGradient(
                                                    listOf(Color(0xFF9C27B0), Color(0xFF7B1FA2))
                                                ).let { _ ->
                                                    Color(0xFF7B1FA2)
                                                }
                                            }
                                        )
                                        .clickable { revealCell(index) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (cell.revealed) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(cell.icon, fontSize = 28.sp)
                                            if (cardPrizes[index] > 0) {
                                                Text(
                                                    "+${cardPrizes[index]}",
                                                    fontSize = 10.sp,
                                                    color = GoldCoin,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    } else {
                                        Text("?", fontSize = 28.sp, color = Color.White.copy(alpha = 0.5f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total Win
            if (revealedCount > 0) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Success.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Toplam: +$totalWin Coin ($revealedCount/9 açık)",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        color = Success,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Buy Button
            if (!purchased) {
                Button(
                    onClick = { purchaseCard() },
                    modifier = Modifier.width(240.dp).height(56.dp),
                    enabled = coins >= cardCost && cardsRemaining > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (coins >= cardCost && cardsRemaining > 0) AccentPurple else Color.Gray
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("🎫 KAZI ($cardCost Coin)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Text(
                    "Kazıyı tamamlamak için tüm kareleri tıkla!",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            containerColor = Color(0xFF1e1e3f),
            title = {
                Text(
                    if (totalWin > 0) "🎉 Kazı Kazan!" else "😔 Boş Çıktı",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (totalWin > 0) {
                        Text("+$totalWin Coin", color = GoldCoin, fontSize = 32.sp, fontWeight = FontWeight.Bold)
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

data class ScratchCell(val icon: String, val revealed: Boolean)

private fun getScratchIcon(prize: Int): String {
    return when (prize) {
        0 -> "💨"
        10 -> "🪙"
        25 -> "💰"
        50 -> "💎"
        100 -> "👑"
        250 -> "🏆"
        500 -> "🎰"
        else -> "⭐"
    }
}
