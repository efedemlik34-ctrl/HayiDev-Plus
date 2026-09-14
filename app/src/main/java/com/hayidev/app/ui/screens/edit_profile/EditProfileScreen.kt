package com.hayidev.app.ui.screens.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ProfilePhoto(
    val id: String,
    val isMain: Boolean
)

data class InterestTag(
    val id: String,
    val name: String,
    val isSelected: Boolean
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onSaveChanges: () -> Unit = {},
    onAddPhoto: () -> Unit = {},
    onRemovePhoto: (String) -> Unit = {}
) {
    var name by remember { mutableStateOf("Ahmet Yılmaz") }
    var bio by remember { mutableStateOf("Full-stackdeveloper | Flutter & Kotlin") }
    var age by remember { mutableStateOf("25") }
    var location by remember { mutableStateOf("İstanbul, Türkiye") }
    var website by remember { mutableStateOf("https://ahmet.dev") }
    var showPhotoOptions by remember { mutableStateOf(false) }
    var isPrivateAccount by remember { mutableStateOf(false) }
    var showOnlineStatus by remember { mutableStateOf(true) }
    var showReadReceipts by remember { mutableStateOf(true) }

    var photos by remember {
        mutableStateOf(
            listOf(
                ProfilePhoto("1", true),
                ProfilePhoto("2", false),
                ProfilePhoto("3", false),
                ProfilePhoto("4", false),
                ProfilePhoto("5", false),
                ProfilePhoto("6", false)
            )
        )
    }

    var interests by remember {
        mutableStateOf(
            listOf(
                InterestTag("1", "Teknoloji", true),
                InterestTag("2", "Müzik", true),
                InterestTag("3", "Spor", false),
                InterestTag("4", "Seyahat", true),
                InterestTag("5", "Fotoğrafçılık", false),
                InterestTag("6", "Yemek", true),
                InterestTag("7", "Sinema", false),
                InterestTag("8", "Oyun", true),
                InterestTag("9", "Sanat", false),
                InterestTag("10", "Bilim", true)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profili Düzenle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    TextButton(onClick = onSaveChanges) {
                        Text("Kaydet", color = Color(0xFF6C63FF), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Photo
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF6C63FF), Color(0xFF9C27B0))
                                )
                            )
                            .clickable { showPhotoOptions = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Fotoğraf değiştir",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = null,
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-20).dp, y = (-4).dp)
                    )
                }
            }

            // Name
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ad Soyad") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Bio
            item {
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Biyografi") },
                    leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Age
            item {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Yaş") },
                    leadingIcon = { Icon(Icons.Filled.Cake, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Location
            item {
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Konum") },
                    leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Website
            item {
                OutlinedTextField(
                    value = website,
                    onValueChange = { website = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Web Sitesi") },
                    leadingIcon = { Icon(Icons.Filled.Language, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Photos Grid
            item {
                Text("Fotoğraflarım", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.heightIn(max = 300.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(photos) { photo ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = if (photo.isMain)
                                            listOf(Color(0xFF6C63FF), Color(0xFF9C27B0))
                                        else
                                            listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD))
                                    )
                                )
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            if (photo.isMain) {
                                Text("Ana", color = Color.White, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            if (!photo.isMain) {
                                IconButton(
                                    onClick = { onRemovePhoto(photo.id) },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Kaldır",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Interests
            item {
                Text("İlgi Alanları", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    interests.forEach { interest ->
                        FilterChip(
                            selected = interest.isSelected,
                            onClick = {
                                interests = interests.map {
                                    if (it.id == interest.id) it.copy(isSelected = !it.isSelected) else it
                                }
                            },
                            label = { Text(interest.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF6C63FF),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Privacy Settings
            item {
                Text("Gizlilik Ayarları", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isPrivateAccount = !isPrivateAccount }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF6C63FF))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Gizli Hesap", fontWeight = FontWeight.SemiBold)
                                Text(
                                    "Yalnızca onaylanmış takipçileriniz görebilir",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = isPrivateAccount,
                                onCheckedChange = { isPrivateAccount = it },
                                colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF6C63FF))
                            )
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showOnlineStatus = !showOnlineStatus }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Visibility, contentDescription = null, tint = Color(0xFF6C63FF))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Çevrimiçi Durumu Göster", fontWeight = FontWeight.SemiBold)
                                Text(
                                    "Diğer kullanıcılar çevrimiçi olduğunuzu görebilir",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = showOnlineStatus,
                                onCheckedChange = { showOnlineStatus = it },
                                colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF6C63FF))
                            )
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showReadReceipts = !showReadReceipts }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.DoneAll, contentDescription = null, tint = Color(0xFF6C63FF))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Okundu Bilgisi Göster", fontWeight = FontWeight.SemiBold)
                                Text(
                                    "Mesajlarınızın okundu bilgisini gösterir",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = showReadReceipts,
                                onCheckedChange = { showReadReceipts = it },
                                colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF6C63FF))
                            )
                        }
                    }
                }
            }

            // Save button
            item {
                Button(
                    onClick = onSaveChanges,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Değişiklikleri Kaydet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
