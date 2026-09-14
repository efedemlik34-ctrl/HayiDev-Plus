package com.hayidev.app.ui.screens.discover

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hayidev.app.data.model.User
import com.hayidev.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onMatch: (String) -> Unit,
    onProfileClick: (String) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var swipeOffset by remember { mutableFloatStateOf(0f) }

    val sampleUsers = remember {
        listOf(
            User(uid = "1", displayName = "Elif", age = 24, photoUrl = "", bio = "Gezmeyi severim 🌍", country = "Türkiye"),
            User(uid = "2", displayName = "Selin", age = 22, photoUrl = "", bio = "Müzik hayatım 🎵", country = "Türkiye"),
            User(uid = "3", displayName = "Merve", age = 25, photoUrl = "", bio = "Fotoğrafçı 📸", country = "Türkiye"),
            User(uid = "4", displayName = "Zeynep", age = 23, photoUrl = "", bio = "Spor salonu 🏋️", country = "Türkiye"),
            User(uid = "5", displayName = "Ayşe", age = 21, photoUrl = "", bio = "Kahve tutkunu ☕", country = "Türkiye")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "HayiDev++",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.FilterList, "Filtre")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (currentIndex < sampleUsers.size) {
                val user = sampleUsers[currentIndex]

                // Swipeable Card Stack
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = swipeOffset.dp)
                        .detectHorizontalDragGestures(
                            onDragEnd = {
                                if (swipeOffset > 100) {
                                    // Swipe Right - Like
                                    currentIndex++
                                    swipeOffset = 0f
                                } else if (swipeOffset < -100) {
                                    // Swipe Left - Pass
                                    currentIndex++
                                    swipeOffset = 0f
                                } else {
                                    swipeOffset = 0f
                                }
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                swipeOffset += dragAmount
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Profile Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.7f),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Background Gradient
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                PrimaryLight.copy(alpha = 0.3f),
                                                PrimaryDark.copy(alpha = 0.5f)
                                            )
                                        )
                                    )
                            )

                            // Profile Image Placeholder
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(120.dp),
                                    tint = Color.White.copy(alpha = 0.5f)
                                )
                            }

                            // User Info Overlay
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.8f)
                                            )
                                        )
                                    )
                                    .padding(24.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = user.displayName,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${user.age}",
                                        fontSize = 24.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                    if (user.isVerified) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            Icons.Filled.Verified,
                                            contentDescription = "Doğrulanmış",
                                            tint = AccentBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Filled.LocationOn,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${user.country}",
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = user.bio,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                // Swipe Indicators
                AnimatedVisibility(
                    visible = swipeOffset > 50,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Success.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Beğen",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = swipeOffset < -50,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Error.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Geç",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Undo Button
                    FilledTonalIconButton(
                        onClick = { },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Icon(
                            Icons.Filled.Replay,
                            contentDescription = "Geri Al",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Pass Button
                    FilledIconButton(
                        onClick = {
                            currentIndex++
                        },
                        modifier = Modifier.size(64.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Error
                        )
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Geç",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }

                    // Super Like Button
                    FilledIconButton(
                        onClick = { },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = AccentBlue
                        )
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Süper Beğen",
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                    }

                    // Like Button
                    FilledIconButton(
                        onClick = {
                            currentIndex++
                        },
                        modifier = Modifier.size(64.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Success
                        )
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Beğen",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }

                    // Boost Button
                    FilledTonalIconButton(
                        onClick = { },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Icon(
                            Icons.Filled.Bolt,
                            contentDescription = "Boost",
                            modifier = Modifier.size(28.dp),
                            tint = AccentPurple
                        )
                    }
                }
            } else {
                // No more profiles
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Bugün için profiles bitti!",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Yarın tekrar kontrol et veya Premium'a yükselt",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
