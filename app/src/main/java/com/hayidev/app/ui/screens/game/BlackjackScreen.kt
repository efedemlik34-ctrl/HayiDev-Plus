package com.hayidev.app.ui.screens.game

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

data class PlayingCard(val suit: String, val rank: String, val value: Int)

@Composable
fun BlackjackScreen(coins: Int, onPlay: (Int) -> Unit) {
    var bet by remember { mutableIntStateOf(50) }
    var playerHand by remember { mutableStateOf(listOf<PlayingCard>()) }
    var dealerHand by remember { mutableStateOf(listOf<PlayingCard>()) }
    var gamePhase by remember { mutableStateOf("betting") } // betting, playing, dealerTurn, result
    var winAmount by remember { mutableIntStateOf(0) }
    var resultMsg by remember { mutableStateOf("") }

    val suits = listOf("♠", "♥", "♦", "♣")
    val ranks = listOf("A","2","3","4","5","6","7","8","9","10","J","Q","K")

    fun createDeck(): MutableList<PlayingCard> {
        val deck = mutableListOf<PlayingCard>()
        for (s in suits) for (r in ranks) {
            val v = when(r) { "A"->11; "J","Q","K"->10; else->r.toInt() }
            deck.add(PlayingCard(s, r, v))
        }
        deck.shuffle(); return deck
    }

    fun handValue(hand: List<PlayingCard>): Int {
        var v = hand.sumOf { it.value }; var aces = hand.count { it.rank == "A" }
        while (v > 21 && aces > 0) { v -= 10; aces-- }; return v
    }

    fun startGame() {
        if (coins < bet) return
        val deck = createDeck()
        playerHand = listOf(deck.removeAt(0), deck.removeAt(0))
        dealerHand = listOf(deck.removeAt(0), deck.removeAt(0))
        gamePhase = "playing"; winAmount = 0; resultMsg = ""
        onPlay(bet)
    }

    fun hit(deck: MutableList<PlayingCard>) {
        playerHand = playerHand + deck.removeAt(0)
        if (handValue(playerHand) > 21) { gamePhase = "result"; resultMsg = "Bust! Kaybettin 😔"; winAmount = -bet }
    }

    fun stand(deck: MutableList<PlayingCard>) {
        var dh = dealerHand
        while (handValue(dh) < 17) dh = dh + deck.removeAt(0)
        dealerHand = dh
        val pv = handValue(playerHand); val dv = handValue(dealerHand)
        gamePhase = when {
            pv == 21 && dealerHand.size == 2 && handValue(dealerHand) != 21 -> { resultMsg = "Blackjack! 🎉"; winAmount = bet * 2; "result" }
            dv > 21 -> { resultMsg = "Krupiye bust! Kazandın 🎉"; winAmount = bet; "result" }
            pv > dv -> { resultMsg = "Kazandın! 🎉"; winAmount = bet; "result" }
            pv < dv -> { resultMsg = "Kaybettin 😔"; winAmount = -bet; "result" }
            else -> { resultMsg = "Berabere!"; winAmount = 0; "result" }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0d4d2b)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("🃏 21 (Blackjack)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("🪙 $coins", color = GoldCoin, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (gamePhase == "betting") {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Bahis Seç", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(25, 50, 100, 200, 500).forEach { b ->
                    Button(onClick = { bet = b }, colors = ButtonDefaults.buttonColors(containerColor = if (bet == b) GoldCoin else Color(0xFF1e3a1e)),
                        shape = RoundedCornerShape(12.dp)) { Text("$b", color = if (bet == b) Color.Black else Color.White) }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { startGame() }, enabled = coins >= bet, modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldCoin), shape = RoundedCornerShape(25.dp)) {
                Text("OYUNA BAŞLA ($bet 🪙)", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        } else {
            // Dealer hand
            Text("Krupiye", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                dealerHand.forEachIndexed { i, card ->
                    val show = i == 0 || gamePhase == "result" || gamePhase == "dealerTurn"
                    Surface(modifier = Modifier.size(50.dp, 70.dp), shape = RoundedCornerShape(6.dp),
                        color = if (show) Color.White else Color(0xFF4a4a4a)) {
                        Box(contentAlignment = Alignment.Center) {
                            if (show) Text("${card.suit}${card.rank}", fontSize = 14.sp, color = if (card.suit in listOf("♥","♦")) Color.Red else Color.Black)
                            else Text("?", fontSize = 20.sp, color = Color.White)
                        }
                    }
                }
                if (gamePhase == "result") Text("= ${handValue(deilerHand)}", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterVertically))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Player hand
            Text("Senin Elin", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                playerHand.forEach { card ->
                    Surface(modifier = Modifier.size(50.dp, 70.dp), shape = RoundedCornerShape(6.dp), color = Color.White) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("${card.suit}${card.rank}", fontSize = 14.sp, color = if (card.suit in listOf("♥","♦")) Color.Red else Color.Black)
                        }
                    }
                }
                Text("= ${handValue(playerHand)}", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterVertically))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (gamePhase == "playing") {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { hit(createDeck()) }, colors = ButtonDefaults.buttonColors(containerColor = Success), shape = RoundedCornerShape(12.dp)) { Text("KART ÇEK", color = Color.White, fontWeight = FontWeight.Bold) }
                    Button(onClick = { stand(createDeck()) }, colors = ButtonDefaults.buttonColors(containerColor = Error), shape = RoundedCornerShape(12.dp)) { Text("DUR", color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }

            if (gamePhase == "result") {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1e3a1e))) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(resultMsg, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        if (winAmount != 0) Text("${if (winAmount > 0) "+" else ""}$winAmount 🪙", color = if (winAmount > 0) GoldCoin else Error, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { gamePhase = "betting"; playerHand = emptyList(); dealerHand = emptyList() },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldCoin)) { Text("YENİ EL", color = Color.Black) }
                    }
                }
            }
        }
    }
}
