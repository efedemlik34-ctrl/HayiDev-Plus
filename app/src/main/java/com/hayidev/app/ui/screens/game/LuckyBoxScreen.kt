package com.hayidev.app.ui.screens.game

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

data class BoxPrize(val name: String, val amount: Int, val icon: String, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckyBoxScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var boxesRemaining by remember { mutableIntStateOf(1) }
    var premiumBoxesRemaining by remember { mutableIntStateOf(0) }
    var selectedBox by remember { mutableIntStateOf(-1) }
    var isRevealing by remember { mutableStateOf(false) }
    var showPrize by remember { mutableStateOf(false) }
    var currentPrize by remember { mutableStateOf<BoxPrize?>(null) }
    var boxPhase by remember { mutableStateOf("select") } // select, shake, reveal
    var openedHistory by remember { mutableStateOf(listOf<BoxPrize>()) }

    val prizes = listOf(
        BoxPrize("10 Coin", 10, "🪙", GoldCoin),
        BoxPrize("25 Coin", 25, "💰", GoldCoin),
        BoxPrize("50 Coin", 50, "💰", GoldCoin),
        BoxPrize("100 Coin", 100, "💎", AccentBlue),
        BoxPrize("250 Coin", 250, "💎", AccentPurple),
        BoxPrize("500 Coin", 500, "👑", Color(0xFFFFD700)),
        BoxPrize("1000 Coin", 1000, "🎰", Color(0xFFFF1744)),
        BoxPrize("Elmas", 50, "💎", Diamond)
    )

    fun openBox(isPremium: Boolean) {
        if ((isPremium && premiumBoxesRemaining <= 0) || (!isPremium && boxesRemaining <= 0)) return
        if (isPremium) premiumBoxesRemaining-- else boxesRemaining--

        isRevealing = true
        boxPhase = "shake"

        kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
            kotlinx.coroutines.delay(1500) // Kutu sallanma animasyonu
            boxPhase = "reveal"

            val prize = if (isPremium) {
                // Premium kutu daha iyi ödüller verir
                prizes.filter { it.amount >= 50 }.random()
            } else {
                prizes.random()
            }

            currentPrize = prize
            coins += prize.amount
            openedHistory = listOf(prize) + openedHistory.take(9)
            showPrize = true
            isRevealing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎁 Şans Kutusu", fontWeight = FontWeight.Bold) },
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
            Spacer(modifier = Modifier.height(12.dp))

            // Remaining boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BoxBadge("🎫", "Günlük Kutu", "$boxesRemaining Kalan", AccentPurple)
                BoxBadge("💎", "Premium Kutu", "$premiumBoxesRemaining Kalan", GoldCoin)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main box display
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            if (isRevealing) listOf(Color(0xFFFFD700), Color(0xFFFFA000))
                            else listOf(Color(0xFF7B1FA2), Color(0xFF4A148C))
                        )
                    )
                    .clickable {
                        if (boxPhase == "select") {
                            openBox(false)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (boxPhase == "reveal" && currentPrize != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(currentPrize!!.icon, fontSize = 48.sp)
                        Text(currentPrize!!.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎁", fontSize = 64.sp)
                        if (isRevealing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Open Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { openBox(false) },
                    modifier = Modifier.width(140.dp).height(50.dp),
                    enabled = boxesRemaining > 0 && !isRevealing,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("🎁 Aç", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { openBox(true) },
                    modifier = Modifier.width(140.dp).height(50.dp),
                    enabled = premiumBoxesRemaining > 0 && !isRevealing,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldCoin),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("💎 Premium", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Prize Pool
            Card(
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🎁 Olası Ödüller", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.height(120.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(prizes) { prize ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = prize.color.copy(alpha = 0.1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(prize.icon, fontSize = 16.sp)
                                    Text(prize.name, fontSize = 8.sp, color = Color.White.copy(alpha = 0.7f))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // History
            if (openedHistory.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Son Kutular", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            openedHistory.take(5).forEach { prize ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(prize.icon, fontSize = 20.sp)
                                    Text("+${prize.amount}", fontSize = 8.sp, color = prize.color)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPrize && currentPrize != null) {
        AlertDialog(
            onDismissRequest = { showPrize = false; boxPhase = "select" },
            containerColor = Color(0xFF1e1e3f),
            title = { Text("🎉 TEBRİKLER!", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(currentPrize!!.icon, fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(currentPrize!!.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("+${currentPrize!!.amount} Coin", color = currentPrize!!.color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrize = false; boxPhase = "select" },
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
fun BoxBadge(icon: String, title: String, subtitle: String, color: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = color, fontSize = 10.sp)
            }
        }
    }
}
