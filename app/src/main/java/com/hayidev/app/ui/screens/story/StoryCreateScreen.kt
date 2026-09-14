package com.hayidev.app.ui.screens.story

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StoryTemplate(
    val id: String,
    val name: String,
    val gradientColors: List<Color>,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryCreateScreen(
    onBack: () -> Unit = {},
    onPostStory: (Uri?) -> Unit = {},
    onCameraClick: () -> Unit = {}
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var storyText by remember { mutableStateOf("") }
    var selectedMusic by remember { mutableStateOf<String?>(null) }
    var showMusicPicker by remember { mutableStateOf(false) }
    var audience by remember { mutableIntStateOf(0) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val templates = listOf(
        StoryTemplate("1", "Normal", listOf(Color(0xFF6C63FF), Color(0xFF9C27B0)), Icons.Filled.Photo),
        StoryTemplate("2", "Yazılı", listOf(Color(0xFFFF6B6B), Color(0xFFEE5A24)), Icons.Filled.TextFields),
        StoryTemplate("3", "Müzik", listOf(Color(0xFF1DB954), Color(0xFF191414)), Icons.Filled.MusicNote),
        StoryTemplate("4", "Anket", listOf(Color(0xFF3498DB), Color(0xFF2980B9)), Icons.Filled.Poll),
        StoryTemplate("5", "Soru", listOf(Color(0xFFE74C3C), Color(0xFFC0392B)), Icons.Filled.Help),
        StoryTemplate("6", "Sayaç", listOf(Color(0xFFF39C12), Color(0xFFE67E22)), Icons.Filled.Timer)
    )

    val musicTracks = listOf(
        Triple("1", "Yaz Gecesi", "Tarkan"),
        Triple("2", "Dünya", "Son Feci Bisiklet"),
        Triple("3", "Giderli", "İrem Derici"),
        Triple("4", "Bu Gece", "MaNga"),
        Triple("5", "Aşk Kaç Beden Giyer", "Sıla")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hikaye Oluştur") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    TextButton(onClick = { onPostStory(selectedImageUri) }) {
                        Text("Paylaş", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6C63FF)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            // Preview area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF6C63FF).copy(alpha = 0.3f),
                                        Color(0xFF9C27B0).copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Fotoğraf veya video seçin",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 16.sp
                        )
                    }
                }

                if (storyText.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                            .background(
                                Color.Black.copy(alpha = 0.5f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = storyText,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Text input
            OutlinedTextField(
                value = storyText,
                onValueChange = { storyText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Metin ekle...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF6C63FF),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    cursorColor = Color.White
                ),
                maxLines = 2
            )

            // Music selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { showMusicPicker = !showMusicPicker },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF).copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.MusicNote, contentDescription = null, tint = Color(0xFF6C63FF))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = selectedMusic ?: "Müzik ekle",
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        if (showMusicPicker) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            if (showMusicPicker) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(musicTracks) { (id, title, artist) ->
                        Card(
                            modifier = Modifier.clickable {
                                selectedMusic = "$title - $artist"
                                showMusicPicker = false
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedMusic == "$title - $artist")
                                    Color(0xFF6C63FF) else Color.White.copy(alpha = 0.1f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(artist, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Template selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                templates.forEach { template ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(template.gradientColors)
                            )
                            .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = template.icon,
                            contentDescription = template.name,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Bottom actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingActionButton(
                        onClick = onCameraClick,
                        containerColor = Color(0xFF6C63FF),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = "Kamera", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Kamera", color = Color.White, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingActionButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        containerColor = Color(0xFF9C27B0),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = "Galeri", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Galeri", color = Color.White, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = Color(0xFFFF6B6B),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Filled.VideoCameraFront, contentDescription = "Canlı", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Canlı", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}
