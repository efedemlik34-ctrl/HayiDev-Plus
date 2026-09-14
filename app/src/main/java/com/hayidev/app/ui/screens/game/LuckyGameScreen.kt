package com.hayidev.app.ui.screens.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class WheelPrize(
    val name: String,
    val amount: Int,
    val color: Color,
    val type: String = "coin",
    val isSpecial: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckyGameScreen(
    roomId: String,
    onBackClick: () -> Unit,
    onBetPlaced: (Int) -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var selectedBet by remember { mutableIntStateOf(50) }
    var isSpinning by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }
    var jackpotPool by remember { mutableIntStateOf(125000) }
    var gameHistory by remember { mutableStateOf(listOf<Pair<String, Int>>()) }

    val betOptions = listOf(10, 25, 50, 100, 200, 500)

    val prizes = remember {
        listOf(
            WheelPrize("10", 10, Color(0xFF4CAF50)),
            WheelPrize("25", 25, Color(0xFF2196F3)),
            WheelPrize("50", 50, Color(0xFF9C27B0)),
            WheelPrize("100", 100, Color(0xFFFF9800)),
            WheelPrize("200", 200, Color(0xFFE91E63)),
            WheelPrize("x2", 0, Color(0xFF00BCD4), "multiplier"),
            WheelPrize("500", 500, Color(0xFFFFD700), "coin", true),
            WheelPrize("💎", 100, Color(0xFFE91E63), "diamond", true),
            WheelPrize("15", 15, Color(0xFF8BC34A)),
            WheelPrize("75", 75, Color(0xFFFF5722)),
            WheelPrize("JACKPOT", 0, Color(0xFFFF1744), "jackpot", true),
            WheelPrize("30", 30, Color(0xFF673AB7))
        )
    }

    var currentRotation by remember { mutableFloatStateOf(0f) }
    val sweepAngle = 360f / prizes.size

    val rotation by animateFloatAsState(
        targetValue = currentRotation,
        animationSpec = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
        label = "spin"
    )

    fun spinWheel() {
        if (coins < selectedBet || isSpinning) return

        coins -= selectedBet
        isSpinning = true
        resultMessage = ""

        val winIndex = Random.nextInt(prizes.size)
        val prize = prizes[winIndex]

        val targetAngle = 360f - (winIndex * sweepAngle + sweepAngle / 2f)
        currentRotation += 360f * 8 + targetAngle

        kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
            kotlinx.coroutines.delay(5200)

            when (prize.type) {
                "coin" -> {
                    winAmount = prize.amount
                    coins += prize.amount
                }
                "multiplier" -> {
                    winAmount = selectedBet * 2
                    coins += winAmount
                }
                "diamond" -> {
                    winAmount = prize.amount
                    coins += prize.amount
                }
                "jackpot" -> {
                    winAmount = jackpotPool / 10
                    coins += winAmount
                    jackpotPool -= winAmount
                    resultMessage = "💎 JACKPOT KAZANDIN!"
                }
            }

            if (resultMessage.isEmpty()) {
                resultMessage = if (winAmount > 0) "${prize.name} Kazandın!" else "Kaybettin!"
            }

            gameHistory = listOf(prize.name to winAmount) + gameHistory.take(9)
            onBetPlaced(selectedBet)
            showResult = true
            isSpinning = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎰", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Şans Oyunu", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Oda: #$roomId", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.Close, "Kapat", tint = Color.White)
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .background(GoldCoin.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$coins", color = GoldCoin, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0d0d2b), Color(0xFF1a1a3e), Color(0xFF0d0d2b))
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Jackpot Pool Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1e1e3f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("💎 JACKPOT HAVUZU", color = GoldCoin, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "$jackpotPool Coin",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Wheel
                Box(
                    modifier = Modifier.size(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(280.dp)
                            .rotate(rotation)
                    ) {
                        val canvasSize = size.minDimension
                        val radius = canvasSize / 2f
                        val center = Offset(size.width / 2f, size.height / 2f)

                        prizes.forEachIndexed { index, prize ->
                            val startAngle = index * sweepAngle - 90f

                            drawArc(
                                color = prize.color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = true,
                                topLeft = Offset.Zero,
                                size = Size(canvasSize, canvasSize)
                            )

                            drawContext.canvas.nativeCanvas.apply {
                                val textAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                                val textX = center.x + (radius * 0.62f) * cos(textAngle).toFloat()
                                val textY = center.y + (radius * 0.62f) * sin(textAngle).toFloat()

                                val paint = android.graphics.Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textSize = if (prize.isSpecial) 26f else 24f
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    isFakeBoldText = prize.isSpecial
                                    isAntiAlias = true
                                }
                                drawText(prize.name, textX, textY + 8f, paint)
                            }

                            val lineAngle = Math.toRadians((startAngle + sweepAngle).toDouble())
                            drawLine(
                                color = Color.White.copy(alpha = 0.4f),
                                start = center,
                                end = Offset(
                                    center.x + radius * cos(lineAngle).toFloat(),
                                    center.y + radius * sin(lineAngle).toFloat()
                                ),
                                strokeWidth = 2f
                            )
                        }

                        // Center hub
                        drawCircle(color = Color(0xFF1e1e3f), radius = 35f, center = center)
                        drawCircle(color = GoldCoin, radius = 32f, center = center, style = Stroke(3f))
                    }

                    // Pointer arrow
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = 2.dp)
                    ) {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            tint = GoldCoin,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bet Selection
                Text("Bahis Seç", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(betOptions) { bet ->
                        FilterChip(
                            selected = selectedBet == bet,
                            onClick = { selectedBet = bet },
                            label = {
                                Text(
                                    "$bet",
                                    fontWeight = if (selectedBet == bet) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = if (coins >= bet) null else {
                                { Icon(Icons.Filled.Lock, null, modifier = Modifier.size(14.dp)) }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldCoin.copy(alpha = 0.2f),
                                selectedLabelColor = GoldCoin
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Spin Button
                Button(
                    onClick = { spinWheel() },
                    modifier = Modifier
                        .width(200.dp)
                        .height(56.dp),
                    enabled = coins >= selectedBet && !isSpinning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (coins >= selectedBet) GoldCoin else Color.Gray
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    if (isSpinning) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black, strokeWidth = 3.dp)
                    } else {
                        Text(
                            if (coins >= selectedBet) "🎰 ÇEVİR ($selectedBet)" else "Yetersiz Coin",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Game History
                if (gameHistory.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(gameHistory.take(5)) { (name, amount) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (amount > 0) Color(0xFF1e3a1e) else Color(0xFF3a1e1e)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(name, fontSize = 11.sp, color = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        if (amount > 0) "+$amount" else "$amount",
                                        fontSize = 11.sp,
                                        color = if (amount > 0) Success else Error,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Rules
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("📋 Kurallar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("• Her çevirmede Jackpot tetikleme şansı", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                        Text("• Bahisler Jackpot havuzuna eklenir", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                        Text("• x2 ile kazancını ikiye katla", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }
            }
        }
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            containerColor = Color(0xFF1e1e3f),
            title = {
                Text(
                    if (winAmount > 0) "🎉 TEBRİKLER!" else "😔 Tekrar Dene",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(resultMessage, fontSize = 16.sp, color = Color.White)
                    if (winAmount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "+ $winAmount Coin",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldCoin
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showResult = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldCoin),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Devam", color = Color.Black)
                }
            }
        )
    }
}
