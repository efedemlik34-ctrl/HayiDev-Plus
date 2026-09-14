package com.hayidev.app.ui.screens.sticker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StickerPack(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val isFree: Boolean,
    val isPurchased: Boolean,
    val stickerCount: Int,
    val category: String,
    val previewStickers: List<String>
)

data class StickerCategory(
    val id: String,
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerStoreScreen(
    onBack: () -> Unit = {},
    onBuyStickerPack: (String) -> Unit = {},
    onBuyCoins: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("all") }
    var userCoins by remember { mutableIntStateOf(1250) }

    val categories = listOf(
        StickerCategory("all", "Tümü", Icons.Filled.Apps),
        StickerCategory("popular", "Popüler", Icons.Filled.TrendingUp),
        StickerCategory("love", "Aşk", Icons.Filled.Favorite),
        StickerCategory("funny", "Komik", Icons.Filled.EmojiEmotions),
        StickerCategory("nature", "Doğa", Icons.Filled.Nature),
        StickerCategory("food", "Yemek", Icons.Filled.Restaurant),
        StickerCategory("animal", "Hayvan", Icons.Filled.Pets),
        StickerCategory("sports", "Spor", Icons.Filled.SportsSoccer)
    )

    val stickerPacks = remember {
        listOf(
            StickerPack("1", "Sevimli Hayvanlar", "Kawaii hayvan stickerları", 0, true, true, 24, "animal", listOf("🐶", "🐱", "🐰", "🐻")),
            StickerPack("2", "Aşk Kalpleri", "Romantik kalp stickerları", 50, false, false, 30, "love", listOf("❤️", "💕", "💗", "💖")),
            StickerPack("3", "Gülme Emoji", "Eğlenceli yüz stickerları", 0, true, true, 20, "funny", listOf("😂", "🤣", "😜", "🤪")),
            StickerPack("4", "Doğa Manzara", "Güzel doğa stickerları", 100, false, false, 18, "nature", listOf("🌸", "🌊", "🌅", "🍃")),
            StickerPack("5", "Lezzetli Yemek", "Yemek stickerları", 75, false, false, 25, "food", listOf("🍕", "🍔", "🍣", "🌮")),
            StickerPack("6", "Spor Aktivite", "Spor stickerları", 0, true, true, 15, "sports", listOf("⚽", "🏀", "🎾", "🏋️")),
            StickerPack("7", "Noel Özel", "Noel temalı stickerlar", 200, false, false, 40, "popular", listOf("🎄", "🎁", "❄️", "🎅")),
            StickerPack("8", "Doga Emojiler", "Emoji stickerları", 150, false, false, 35, "popular", listOf("🔥", "⭐", "🌈", "💎"))
        )
    }

    val filteredPacks = if (selectedCategory == "all") stickerPacks
    else stickerPacks.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sticker Mağazası", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .clickable { onBuyCoins() }
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$userCoins",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6C63FF))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Categories
            LazyRow(
                modifier = Modifier.padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category.id,
                        onClick = { selectedCategory = category.id },
                        label = { Text(category.name) },
                        leadingIcon = {
                            Icon(
                                category.icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6C63FF),
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPacks) { pack ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Sticker preview grid
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    modifier = Modifier
                                        .size(80.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    items(pack.previewStickers) { sticker ->
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFF5F5F5)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(sticker, fontSize = 16.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(pack.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(
                                        "${pack.stickerCount} sticker",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        pack.description,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (pack.isPurchased) {
                                    Icon(
                                        Icons.Filled.CheckCircle,
                                        contentDescription = "Satın Alındı",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(32.dp)
                                    )
                                } else if (pack.isFree) {
                                    Button(
                                        onClick = { onBuyStickerPack(pack.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Text("Ücretsiz", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            if (userCoins >= pack.price) {
                                                userCoins -= pack.price
                                                onBuyStickerPack(pack.id)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (userCoins >= pack.price)
                                                Color(0xFF6C63FF) else Color.Gray
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.MonetizationOn,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${pack.price}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
