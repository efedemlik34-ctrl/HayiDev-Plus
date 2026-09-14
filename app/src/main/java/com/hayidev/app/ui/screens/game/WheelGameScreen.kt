package com.hayidev.app.ui.screens.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WheelGameScreen(coins: Int, onSpin: (Int) -> Unit) {
    var selectedBet by remember { mutableIntStateOf(50) }
    var isSpinning by remember { mutableStateOf(false) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }

    val bets = listOf(10, 25, 50, 100, 200, 500)
    val prizes = listOf(
        Triple("10", 10, Color(0xFF4CAF50)), Triple("25", 25, Color(0xFF2196F3)),
        Triple("50", 50, Color(0xFF9C27B0)), Triple("100", 100, Color(0xFFFF9800)),
        Triple("200", 200, Color(0xFFE91E63)), Triple("x2", 0, Color(0xFF00BCD4)),
        Triple("500", 500, Color(0xFFFFD700)), Triple("💎", 100, Color(0xFFE91E63)),
        Triple("15", 15, Color(0xFF8BC34A)), Triple("75", 75, Color(0xFFFF5722)),
        Triple("JACK", 0, Color(0xFFFF1744)), Triple("30", 30, Color(0xFF673AB7))
    )
    val sweep = 360f / prizes.size

    val rot by animateFloatAsState(targetValue = rotation, animationSpec = tween(5000, easing = FastOutSlowInEasing), label = "")

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0d0d2b)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("🎡 Şans Çarkı", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Surface(shape = RoundedCornerShape(20.dp), color = GoldCoin.copy(alpha = 0.15f)) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🪙 $coins", color = GoldCoin, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.size(280.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(260.dp).rotate(rot)) {
                val s = size.minDimension; val r = s / 2f; val c = Offset(size.width / 2f, size.height / 2f)
                prizes.forEachIndexed { i, (name, amt, color) ->
                    val start = i * sweep - 90f
                    drawArc(color, start, sweep, true, topLeft = Offset.Zero, size = Size(s, s))
                    val a = Math.toRadians((start + sweep / 2).toDouble())
                    drawContext.canvas.nativeCanvas.apply {
                        val p = android.graphics.Paint().apply { this.color = android.graphics.Color.WHITE; textSize = 22f; textAlign = android.graphics.Paint.Align.CENTER; isAntiAlias = true }
                        drawText(name, c.x + (r * 0.6f) * cos(a).toFloat(), c.y + (r * 0.6f) * sin(a).toFloat() + 8f, p)
                    }
                }
                drawCircle(Color(0xFF0d0d2b), 30f, c); drawCircle(GoldCoin, 27f, c)
            }
            Icon(Icons.Filled.ArrowDropDown, null, tint = GoldCoin, modifier = Modifier.size(32.dp).align(Alignment.TopCenter))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Bahis", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            bets.forEach { b ->
                FilterChip(selected = selectedBet == b, onClick = { selectedBet = b }, label = { Text("$b", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GoldCoin.copy(alpha = 0.2f), selectedLabelColor = GoldCoin))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (coins >= selectedBet && !isSpinning) {
                isSpinning = true; coins - selectedBet
                val idx = Random.nextInt(prizes.size); val (_, amt) = prizes[idx]
                rotation += 360f * 8 + (360f - idx * sweep - sweep / 2f)
                winAmount = if (amt > 0) amt else selectedBet * 2
                kotlinx.coroutines.MainScope().kotlinx.coroutines.launch { kotlinx.coroutines.delay(5200); isSpinning = false; showResult = true; onSpin(selectedBet) }
            }
        }, modifier = Modifier.width(180.dp).height(50.dp), enabled = coins >= selectedBet && !isSpinning,
            colors = ButtonDefaults.buttonColors(containerColor = GoldCoin), shape = RoundedCornerShape(25.dp)) {
            if (isSpinning) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
            else Text("ÇEVİR ($selectedBet)", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
    if (showResult) AlertDialog(onDismissRequest = { showResult = false }, containerColor = Color(0xFF1e1e3f),
        title = { Text("🎉 Kazandın!", color = Color.White) },
        text = { Text("+$winAmount Coin", color = GoldCoin, fontSize = 24.sp, fontWeight = FontWeight.Bold) },
        confirmButton = { Button(onClick = { showResult = false }, colors = ButtonDefaults.buttonColors(containerColor = GoldCoin)) { Text("Devam", color = Color.Black) } })
}
