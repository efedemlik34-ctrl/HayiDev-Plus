package com.hayidev.app.ui.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.ui.theme.*

@Composable
fun VideoCallScreen(
    roomId: String,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isVideoEnabled by remember { mutableStateOf(true) }
    var isSpeakerEnabled by remember { mutableStateOf(false) }
    var callDuration by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            callDuration++
        }
    }

    fun formatDuration(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Remote Video (Full Screen)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1a1a2e),
                            Color(0xFF16213e)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.White.copy(alpha = 0.3f)
            )
        }

        // Local Video (Small)
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(120.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color.White.copy(alpha = 0.5f)
            )
        }

        // Top Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Elifcan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatDuration(callDuration),
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        // Bottom Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mute Button
            VideoControlButton(
                icon = if (isMuted) Icons.Filled.MicOff else Icons.Filled.Mic,
                isActive = !isMuted,
                onClick = { isMuted = !isMuted }
            )

            // Video Button
            VideoControlButton(
                icon = if (isVideoEnabled) Icons.Filled.Videocam else Icons.Filled.VideocamOff,
                isActive = isVideoEnabled,
                onClick = { isVideoEnabled = !isVideoEnabled }
            )

            // End Call Button
            IconButton(
                onClick = onEndCall,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Error)
            ) {
                Icon(
                    Icons.Filled.CallEnd,
                    contentDescription = "Aramayı Bitir",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Speaker Button
            VideoControlButton(
                icon = if (isSpeakerEnabled) Icons.Filled.Speaker else Icons.Filled.SpeakerOff,
                isActive = isSpeakerEnabled,
                onClick = { isSpeakerEnabled = !isSpeakerEnabled }
            )

            // More Options
            VideoControlButton(
                icon = Icons.Filled.MoreVert,
                isActive = false,
                onClick = { }
            )
        }
    }
}

@Composable
fun VideoControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(
                if (isActive) Color.White.copy(alpha = 0.2f)
                else Color.White.copy(alpha = 0.1f)
            )
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}
