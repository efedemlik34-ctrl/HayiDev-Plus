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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

data class MemoryCard(
    val id: Int,
    val symbol: String,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var betAmount by remember { mutableIntStateOf(50) }
    var moves by remember { mutableIntStateOf(0) }
    var matches by remember { mutableIntStateOf(0) }
    var selectedCards by remember { mutableStateOf<List<Int>>(emptyList()) }
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }
    var timer by remember { mutableIntStateOf(0) }
    var isGameActive by remember { mutableStateOf(false) }

    val symbols = listOf("🍒", "🍋", "🍊", "🍇", "💎", "7️⃣", "🔔", "⭐")

    var cards by remember {
        mutableStateOf(
            (symbols + symbols).shuffled().mapIndexed { index, symbol ->
                MemoryCard(id = index, symbol = symbol)
            }
        )
    }

    LaunchedEffect(isGameActive) {
        if (isGameActive) {
            while (isGameActive) {
                kotlinx.coroutines.delay(1000)
                timer++
            }
        }
    }

    fun flipCard(index: Int) {
        if (selectedCards.size >= 2 || cards[index].isRevealed || cards[index].isMatched || !isGameActive) return

        cards = cards.toMutableList().apply {
            this[index] = this[index].copy(isRevealed = true)
        }
        selectedCards = selectedCards + index

        if (selectedCards.size == 2) {
            moves++
            val (first, second) = selectedCards

            if (cards[first].symbol == cards[second].symbol) {
                // Eşleşme bulundu
                cards = cards.toMutableList().apply {
                    this[first] = this[first].copy(isMatched = true)
                    this[second] = this[second].copy(isMatched = true)
                }
                matches++

                if (matches == symbols.size) {
                    // Oyun bitti
                    isGameActive = false
                    val scoreBonus = when {
                        moves <= 12 -> 5
                        moves <= 16 -> 3
                        moves <= 20 -> 2
                        else -> 1
                    }
                    val timeBonus = if (timer < 60) 2 else 1
                    winAmount = betAmount * scoreBonus * timeBonus
                    coins += winAmount
                    showResult = true
                }
                selectedCards = emptyList()
            } else {
                // Eşleşme yok
                kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
                    kotlinx.coroutines.delay(800)
                    cards = cards.toMutableList().apply {
                        this[first] = this[first].copy(isRevealed = false)
                        this[second] = this[second].copy(isRevealed = false)
                    }
                    selectedCards = emptyList()
                }
            }
        }
    }

    fun startGame() {
        if (coins < betAmount) return
        coins -= betAmount
        cards = (symbols + symbols).shuffled().mapIndexed { index, symbol ->
            MemoryCard(id = index, symbol = symbol)
        }
        moves = 0
        matches = 0
        timer = 0
        selectedCards = emptyList()
        isGameActive = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🧩 Hafıza Oyunu", fontWeight = FontWeight.Bold) },
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
                .background(Brush.verticalGradient(listOf(Color(0xFF0d0d2b), Color(0xFF1a1a3e), Color(0xFF0d0d2b)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("⏱️", formatTime(timer))
                StatItem("🔄", "$moves Vuruş")
                StatItem("✅", "$matches/8 Eşleşme")
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!isGameActive && matches < 8) {
                // Start screen
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🧩", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Hafızanı Test Et!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Kartları eşleştir, en az vuruşla kazan!", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf(25, 50, 100, 200)) { bet ->
                            FilterChip(
                                selected = betAmount == bet,
                                onClick = { betAmount = bet },
                                label = { Text("$bet") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AccentPurple.copy(alpha = 0.3f),
                                    selectedLabelColor = AccentPurple
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { startGame() },
                        modifier = Modifier.width(200.dp).height(50.dp),
                        enabled = coins >= betAmount,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("🎮 BAŞLA ($betAmount)", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Card Grid (4x4)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .aspectRatio(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(cards.size) { index ->
                        val card = cards[index]
                        Card(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { flipCard(index) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    card.isMatched -> Success.copy(alpha = 0.3f)
                                    card.isRevealed -> Color(0xFF2d1b4e)
                                    else -> Color(0xFF7B1FA2)
                                }
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when {
                                        card.isMatched -> card.symbol
                                        card.isRevealed -> card.symbol
                                        else -> "❓"
                                    },
                                    fontSize = 28.sp
                                )
                            }
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
            title = { Text("🎉 TEBRİKLER!", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tüm kartları eşleştirdin!", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$moves vuruş, ${formatTime(timer)}", color = Color.White.copy(alpha = 0.7f))
                    Text("+$winAmount Coin", color = GoldCoin, fontSize = 28.sp, fontWeight = FontWeight.Bold)
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
fun StatItem(icon: String, value: String) {
    Row(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
