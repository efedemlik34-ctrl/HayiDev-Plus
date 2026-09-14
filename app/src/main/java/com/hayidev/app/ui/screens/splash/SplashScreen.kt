package com.hayidev.app.ui.screens.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: (Boolean) -> Unit = {}
) {
    var logoVisible by remember { mutableStateOf(false) }
    var nameVisible by remember { mutableStateOf(false) }
    var loadingVisible by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    val logoScale by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "logoAlpha"
    )

    val nameOffsetY by animateDpAsState(
        targetValue = if (nameVisible) 0.dp else 50.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "nameOffsetY"
    )

    val nameAlpha by animateFloatAsState(
        targetValue = if (nameVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "nameAlpha"
    )

    val progressAnimated by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val shimmerEffect = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by shimmerEffect.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    LaunchedEffect(Unit) {
        logoVisible = true
        delay(500)
        nameVisible = true
        delay(500)
        loadingVisible = true
        progress = 1f
        delay(2500)
        
        val context = androidx.compose.ui.platform.LocalContext.current
        val prefs = context.getSharedPreferences("hayidev_prefs", android.content.Context.MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
        onSplashFinished(isLoggedIn)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6C63FF),
                        Color(0xFF3F3D9E),
                        Color(0xFF1A1A2E)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .scale(logoScale)
                    .alpha(logoAlpha)
                    .size(160.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "HD",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF6C63FF)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .offset(y = nameOffsetY)
                    .alpha(nameAlpha),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HayiDev++",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Yeni Nesil Sosyal Platform",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = shimmerAlpha * 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            AnimatedVisibility(
                visible = loadingVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 48.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { progressAnimated },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Yükleniyor...",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Text(
            text = "v1.0.0",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}
