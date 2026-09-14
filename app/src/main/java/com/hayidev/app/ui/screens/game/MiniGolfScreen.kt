package com.hayidev.app.ui.screens.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGolfScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var betAmount by remember { mutableIntStateOf(50) }
    var strokes by remember { mutableIntStateOf(0) }
    var maxStrokes by remember { mutableIntStateOf(3) }
    var ballPosition by remember { mutableStateOf(Offset(0.5f, 0.85f)) }
    var holePosition by remember { mutableStateOf(Offset(0.5f, 0.15f)) }
    var isAnimating by remember { mutableStateOf(false) }
    var gamePhase by remember { mutableStateOf("aim") } // aim, shoot, result
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }
    var holeNumber by remember { mutableIntStateOf(1) }
    var totalScore by remember { mutableIntStateOf(0) }

    val betOptions = listOf(25, 50, 100, 200)

    // Obstacles
    val obstacles = remember {
        listOf(
            Offset(0.3f, 0.5f) to Offset(0.7f, 0.5f), // horizontal wall
            Offset(0.5f, 0.3f) to Offset(0.5f, 0.45f)  // vertical wall
        )
    }

    fun resetHole() {
        ballPosition = Offset(0.5f, 0.85f)
        holePosition = Offset(
            0.2f + Random.nextFloat() * 0.6f,
            0.1f + Random.nextFloat() * 0.2f
        )
        strokes = 0
        gamePhase = "aim"
    }

    fun shootBall(power: Float, angle: Float) {
        if (isAnimating) return
        isAnimating = true
        strokes++

        val dx = power * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * 0.3f
        val dy = -power * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * 0.3f

        val targetX = (ballPosition.x + dx).coerceIn(0.05f, 0.95f)
        val targetY = (ballPosition.y + dy).coerceIn(0.05f, 0.95f)

        ballPosition = Offset(targetX, targetY)

        kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
            kotlinx.coroutines.delay(500)

            // Deliğe ulaştı mı?
            val distance = kotlin.math.sqrt(
                (ballPosition.x - holePosition.x).let { it * it } +
                (ballPosition.y - holePosition.y).let { it * it }
            )

            if (distance < 0.08f) {
                // HOLE IN ONE veya较好 sonuç
                val scoreBonus = when (strokes) {
                    1 -> 10 // Hole in one
                    2 -> 5  // Birdie
                    3 -> 2  // Par
                    else -> 0
                }
                totalScore += scoreBonus
                winAmount = betAmount * scoreBonus
                if (winAmount > 0) coins += winAmount
                showResult = true
            } else if (strokes >= maxStrokes) {
                // Limit doldu
                winAmount = 0
                showResult = true
            }

            isAnimating = false
            gamePhase = if (strokes >= maxStrokes || distance < 0.08f) "result" else "aim"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⛳ Mini Golf", fontWeight = FontWeight.Bold) },
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
                .background(Brush.verticalGradient(listOf(Color(0xFF1a472a), Color(0xFF2d5a3f), Color(0xFF1a472a)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Score Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoBadge("Delik", "#$holeNumber")
                InfoBadge("Vuruş", "$strokes/$maxStrokes")
                InfoBadge("Toplam", "$totalScore")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Golf Course
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .aspectRatio(0.7f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2d7a3f))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Grass texture effect
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Green background
                        drawRect(
                            Brush.verticalGradient(
                                listOf(Color(0xFF3a8a4f), Color(0xFF2d7a3f))
                            )
                        )

                        // Obstacles
                        obstacles.forEach { (start, end) ->
                            drawLine(
                                color = Color(0xFF8B4513),
                                start = Offset(start.x * size.width, start.y * size.height),
                                end = Offset(end.x * size.width, end.y * size.height),
                                strokeWidth = 8f,
                                cap = StrokeCap.Round
                            )
                        }

                        // Hole
                        drawCircle(
                            color = Color.Black,
                            radius = 20f,
                            center = Offset(holePosition.x * size.width, holePosition.y * size.height)
                        )

                        // Ball
                        drawCircle(
                            color = Color.White,
                            radius = 14f,
                            center = Offset(ballPosition.x * size.width, ballPosition.y * size.height)
                        )
                    }

                    // Aim controls
                    if (gamePhase == "aim" && !isAnimating) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { shootBall(0.7f, 45f) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("↗️ Güçlü", fontSize = 12.sp)
                            }
                            Button(
                                onClick = { shootBall(0.5f, 60f) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("⬆️ Orta", fontSize = 12.sp)
                            }
                            Button(
                                onClick = { shootBall(0.3f, 75f) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("↗️ Hafif", fontSize = 12.sp)
                            }
                        }
                    }

                    if (isAnimating) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bet Selection
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
                            selectedContainerColor = Success.copy(alpha = 0.3f),
                            selectedLabelColor = Success
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score table
            Card(
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ScoreItem("Hole in One", "x10", Color(0xFFFFD700))
                    ScoreItem("Birdie", "x5", Color(0xFFC0C0C0))
                    ScoreItem("Par", "x2", Color(0xFFCD7F32))
                }
            }
        }
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = {
                showResult = false
                holeNumber++
                resetHole()
            },
            containerColor = Color(0xFF1e3f1e),
            title = {
                Text(
                    if (winAmount > 0) "🎉 GOL!" else "🏌️ Kaçırdın!",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$strokes vuruş", color = Color.White, fontSize = 16.sp)
                    if (winAmount > 0) {
                        Text("+$winAmount Coin", color = GoldCoin, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResult = false
                        holeNumber++
                        resetHole()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Success),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sonraki Delik", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun InfoBadge(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
    }
}

@Composable
fun ScoreItem(label: String, multiplier: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(multiplier, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
    }
}
