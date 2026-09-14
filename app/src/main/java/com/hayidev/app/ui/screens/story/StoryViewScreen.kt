package com.hayidev.app.ui.screens.story

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay

data class StoryItem(
    val id: String,
    val username: String,
    val userAvatar: String,
    val imageUrl: String,
    val timestamp: String,
    val hasMusic: Boolean = false
)

@Composable
fun StoryViewScreen(
    onBack: () -> Unit = {},
    onUserClick: (String) -> Unit = {},
    onReply: (String, String) -> Unit = {},
    storyIndex: Int = 0
) {
    var currentIndex by remember { mutableIntStateOf(storyIndex) }
    var isPaused by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var replyText by remember { mutableStateOf("") }

    val stories = remember {
        listOf(
            StoryItem("1", "ahmet_dev", "https://i.pravatar.cc/150?img=1", "https://picsum.photos/800/1400?random=1", "2 saat önce", true),
            StoryItem("2", "elif_dsgn", "https://i.pravatar.cc/150?img=2", "https://picsum.photos/800/1400?random=2", "4 saat önce"),
            StoryItem("3", "mehmet_app", "https://i.pravatar.cc/150?img=3", "https://picsum.photos/800/1400?random=3", "6 saat önce", true),
            StoryItem("4", "zeynep_photo", "https://i.pravatar.cc/150?img=4", "https://picsum.photos/800/1400?random=4", "1 saat önce"),
            StoryItem("5", "ali_code", "https://i.pravatar.cc/150?img=5", "https://picsum.photos/800/1400?random=5", "3 saat önce")
        )
    }

    val storyDuration = 5000L
    val progressAnim = remember { Animatable(0f) }

    LaunchedEffect(currentIndex, isPaused) {
        if (!isPaused) {
            progressAnim.snapTo(0f)
            progressAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = (storyDuration * (1f - progressAnim.value)).toInt(),
                    easing = LinearEasing
                )
            )
            if (currentIndex < stories.size - 1) {
                currentIndex++
                progress = 0f
            } else {
                onBack()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = { onBack() },
                    onDrag = { _, _ -> }
                )
            }
    ) {
        // Story content placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6C63FF).copy(alpha = 0.8f),
                            Color(0xFF9C27B0).copy(alpha = 0.8f)
                        )
                    )
                )
        )

        // Progress bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            stories.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(
                                fraction = when {
                                    index < currentIndex -> 1f
                                    index == currentIndex -> progressAnim.value
                                    else -> 0f
                                }
                            )
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White)
                    )
                }
            }
        }

        // User info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .statusBarsPadding()
                .offset(y = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stories[currentIndex].username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = stories[currentIndex].timestamp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Daha fazla", tint = Color.White)
            }
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.Close, contentDescription = "Kapat", tint = Color.White)
            }
        }

        // Center tap zones
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 100.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (currentIndex > 0) {
                                    currentIndex--
                                    progress = 0f
                                }
                            }
                        )
                    }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (currentIndex < stories.size - 1) {
                                    currentIndex++
                                    progress = 0f
                                } else {
                                    onBack()
                                }
                            },
                            onPress = {
                                isPaused = true
                                tryAwaitRelease()
                                isPaused = false
                            }
                        )
                    }
            )
        }

        // Bottom bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                placeholder = { Text("Yanıt yaz...", fontSize = 14.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { if (replyText.isNotBlank()) onReply(stories[currentIndex].id, replyText) }) {
                Icon(Icons.Filled.Send, contentDescription = "Gönder", tint = Color.White)
            }
        }
    }
}
