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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class WheelSegment(
    val label: String,
    val amount: Int,
    val color: Color,
    val isSpecial: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinWheelScreen(
    onBackClick: () -> Unit,
    onCoinsChanged: (Int) -> Unit
) {
    var coins by remember { mutableIntStateOf(1500) }
    var spinsRemaining by remember { mutableIntStateOf(5) }
    var isSpinning by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }
    var resultAmount by remember { mutableIntStateOf(0) }
    var showResultDialog by remember { mutableStateOf(false) }

    val segments = remember {
        listOf(
            WheelSegment("10", 10, Color(0xFFFF6B35)),
            WheelSegment("25", 25, Color(0xFF7C4DFF)),
            WheelSegment("50", 50, Color(0xFFFF4081)),
            WheelSegment("100", 100, Color(0xFF448AFF)),
            WheelSegment("5", 5, Color(0xFF4CAF50)),
            WheelSegment("200", 200, Color(0xFFFFD700), isSpecial = true),
            WheelSegment("15", 15, Color(0xFFE040FB)),
            WheelSegment("75", 75, Color(0xFFFF9800)),
            WheelSegment("10", 10, Color(0xFF00BCD4)),
            WheelSegment("💎", 500, Color(0xFFE91E63), isSpecial = true),
            WheelSegment("30", 30, Color(0xFF8BC34A)),
            WheelSegment("1000", 1000, Color(0xFFFF1744), isSpecial = true)
        )
    }

    val totalSegments = segments.size
    val sweepAngle = 360f / totalSegments

    var currentRotation by remember { mutableFloatStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "wheel")

    val rotation by animateFloatAsState(
        targetValue = currentRotation,
        animationSpec = tween(
            durationMillis = 5000,
            easing = FastOutSlowInEasing
        ),
        label = "rotation"
    )

    fun spinWheel() {
        if (spinsRemaining <= 0 || isSpinning) return
        isSpinning = true
        spinsRemaining--
        resultText = ""

        val winIndex = Random.nextInt(totalSegments)
        val winSegment = segments[winIndex]

        val targetAngle = 360f - (winIndex * sweepAngle + sweepAngle / 2f)
        currentRotation += 360f * 5 + targetAngle

        resultAmount = winSegment.amount
        resultText = if (winSegment.isSpecial) {
            "JACKPOT! ${winSegment.label} ${winSegment.amount} Coin!"
        } else {
            "${winSegment.amount} Coin Kazandın!"
        }

        kotlinx.coroutines.MainScope().kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).kotlinx.coroutines.launch {
            kotlinx.coroutines.delay(5200)
            coins += winSegment.amount
            onCoinsChanged(winSegment.amount)
            showResultDialog = true
            isSpinning = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("🎡 Şans Çarkı", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Geri")
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .background(GoldCoin.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.MonetizationOn, null, tint = GoldCoin, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("$coins", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1a1a2e), Color(0xFF16213e), Color(0xFF0f3460))
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Daily limit
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Refresh, null, tint = AccentBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Kalan Çevirme: $spinsRemaining",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Wheel
            Box(
                modifier = Modifier.size(320.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(300.dp)
                        .rotate(rotation)
                ) {
                    val canvasSize = size.minDimension
                    val radius = canvasSize / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)

                    segments.forEachIndexed { index, segment ->
                        val startAngle = index * sweepAngle - 90f

                        drawArc(
                            color = segment.color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true,
                            topLeft = Offset.Zero,
                            size = Size(canvasSize, canvasSize)
                        )

                        // Draw segment lines
                        val lineAngle = Math.toRadians((startAngle + sweepAngle).toDouble())
                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = center,
                            end = Offset(
                                center.x + radius * cos(lineAngle).toFloat(),
                                center.y + radius * sin(lineAngle).toFloat()
                            ),
                            strokeWidth = 2f
                        )

                        // Draw text
                        drawContext.canvas.nativeCanvas.apply {
                            val textAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                            val textX = center.x + (radius * 0.65f) * cos(textAngle).toFloat()
                            val textY = center.y + (radius * 0.65f) * sin(textAngle).toFloat()

                            val paint = android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textSize = 28f
                                textAlign = android.graphics.Paint.Align.CENTER
                                isFakeBoldText = true
                                isAntiAlias = true
                            }
                            drawText(segment.label, textX, textY + 10f, paint)
                        }
                    }

                    // Center circle
                    drawCircle(
                        color = Color(0xFF1a1a2e),
                        radius = 40f,
                        center = center
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 36f,
                        center = center,
                        style = Stroke(width = 4f)
                    )
                }

                // Pointer
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-4).dp)
                ) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Spin button
            Button(
                onClick = { spinWheel() },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp),
                enabled = spinsRemaining > 0 && !isSpinning,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (spinsRemaining > 0) PrimaryLight else Color.Gray
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                if (isSpinning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                } else {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (spinsRemaining > 0) "ÇEVİR" else "Bitti",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Premium upsell
            if (spinsRemaining == 0) {
                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldCoin),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Star, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Premium ile Sınırsız Çevirme!")
                }
            }

            // Prize list
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ödüller", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    segments.chunked(4).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { segment ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(segment.color.copy(alpha = 0.3f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            if (segment.amount >= 100) "💎" else "${segment.amount}",
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { showResultDialog = false },
            title = {
                Text(
                    if (resultAmount >= 100) "🎉 TEBRİKLER!" else "🎉 Güzel!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(resultText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("+ $resultAmount Coin", color = GoldCoin, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(onClick = { showResultDialog = false }) {
                    Text("Devam")
                }
            }
        )
    }
}
