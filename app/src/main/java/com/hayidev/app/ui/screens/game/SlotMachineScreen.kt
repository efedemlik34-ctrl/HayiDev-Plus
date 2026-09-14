package com.hayidev.app.ui.screens.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

@Composable
fun SlotMachineScreen(coins: Int, onPlay: (Int) -> Unit) {
    var selectedBet by remember { mutableIntStateOf(50) }
    var reels by remember { mutableStateOf(listOf("🍒", "🍋", "🍇")) }
    var isSpinning by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }
    var spinCount by remember { mutableIntStateOf(0) }

    val symbols = listOf("🍒", "🍋", "🍊", "🍇", "💎", "7️⃣", "🔔", "⭐")
    val bets = listOf(10, 25, 50, 100, 200)

    fun spin() {
        if (coins < selectedBet || isSpinning) return
        isSpinning = true
        spinCount++

        kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
            // Reel animation
            for (step in 0..15) {
                reels = listOf(symbols.random(), symbols.random(), symbols.random())
                kotlinx.coroutines.delay(100L + step * 50)
            }

            // Final result
            val r1 = symbols.random(); val r2 = symbols.random(); val r3 = symbols.random()
            reels = listOf(r1, r2, r3)

            // Calculate win
            winAmount = when {
                r1 == r2 && r2 == r3 -> when (r1) {
                    "💎" -> selectedBet * 50
                    "7️⃣" -> selectedBet * 30
                    "🔔" -> selectedBet * 20
                    else -> selectedBet * 10
                }
                r1 == r2 || r2 == r3 || r1 == r3 -> selectedBet * 3
                else -> 0
            }

            isSpinning = false
            showResult = true
            onPlay(selectedBet)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0d0d2b)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("🎰 Slot Makinesi", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("🪙 $coins", color = GoldCoin, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Slot display
        Card(modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1e1e3f))) {
            Row(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                reels.forEach { symbol ->
                    Surface(modifier = Modifier.size(80.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFF2a2a4a)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(symbol, fontSize = 40.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Paytable
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Kazanç Tablosu", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("💎💎💎 = x50  |  7️⃣7️⃣7️⃣ = x30  |  🔔🔔🔔 = x20", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                Text("Aynı 3'lü = x10  |  2'li eşleşme = x3", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            bets.forEach { b ->
                FilterChip(selected = selectedBet == b, onClick = { selectedBet = b }, label = { Text("$b", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GoldCoin.copy(alpha = 0.2f), selectedLabelColor = GoldCoin))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = { spin() }, modifier = Modifier.fillMaxWidth().height(50.dp), enabled = coins >= selectedBet && !isSpinning,
            colors = ButtonDefaults.buttonColors(containerColor = GoldCoin), shape = RoundedCornerShape(25.dp)) {
            if (isSpinning) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
            else Text("🎰 ÇEVİR ($selectedBet)", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }

    if (showResult) AlertDialog(onDismissRequest = { showResult = false }, containerColor = Color(0xFF1e1e3f),
        title = { Text(if (winAmount > 0) "🎉 JACKPOT!" else "😔 Tekrar Dene", color = Color.White) },
        text = { Text(if (winAmount > 0) "+$winAmount Coin" else "Şansını tekrar dene!", color = if (winAmount > 0) GoldCoin else Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        confirmButton = { Button(onClick = { showResult = false }, colors = ButtonDefaults.buttonColors(containerColor = GoldCoin)) { Text("Tamam", color = Color.Black) } })
}
