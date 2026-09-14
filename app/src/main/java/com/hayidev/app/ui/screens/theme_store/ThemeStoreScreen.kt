package com.hayidev.app.ui.screens.theme_store

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChatTheme(
    val id: String,
    val name: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val isPremium: Boolean,
    val isPurchased: Boolean,
    val price: Int,
    val previewMessage: String
)

data class Wallpaper(
    val id: String,
    val name: String,
    val gradientColors: List<Color>,
    val isPremium: Boolean,
    val isPurchased: Boolean,
    val price: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeStoreScreen(
    onBack: () -> Unit = {},
    onBuyTheme: (String) -> Unit = {},
    onBuyCoins: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var userCoins by remember { mutableIntStateOf(1250) }
    var selectedTheme by remember { mutableStateOf("default") }

    val themes = listOf(
        ChatTheme("default", "Varsayılan", Color(0xFF6C63FF), Color(0xFFE8E0FF), false, true, 0, "Merhaba! Nasılsın?"),
        ChatTheme("ocean", "Okyanus", Color(0xFF00BCD4), Color(0xFFE0F7FA), false, false, 100, "Merhaba! Nasılsın?"),
        ChatTheme("sunset", "Gün Batımı", Color(0xFFFF7043), Color(0xFFFBE9E7), false, false, 150, "Merhaba! Nasılsın?"),
        ChatTheme("forest", "Orman", Color(0xFF4CAF50), Color(0xFFE8F5E9), true, false, 200, "Merhaba! Nasılsın?"),
        ChatTheme("midnight", "Gece Yarısı", Color(0xFF311B92), Color(0xFF1A1A2E), true, false, 250, "Merhaba! Nasılsın?"),
        ChatTheme("candy", "Şeker", Color(0xFFE91E63), Color(0xFFFCE4EC), true, false, 300, "Merhaba! Nasılsın?")
    )

    val wallpapers = listOf(
        Wallpaper("1", "Düz Renk", listOf(Color(0xFF6C63FF)), false, true, 0),
        Wallpaper("2", "Gök Kuşağı", listOf(Color(0xFFE91E63), Color(0xFFFF9800), Color(0xFF4CAF50), Color(0xFF2196F3)), false, false, 50),
        Wallpaper("3", "Gece Gökyüzü", listOf(Color(0xFF0D47A1), Color(0xFF1A237E)), true, false, 150),
        Wallpaper("4", "Gül", listOf(Color(0xFFE91E63), Color(0xFF880E4F)), true, false, 200),
        Wallpaper("5", "Dağ", listOf(Color(0xFF4CAF50), Color(0xFF1B5E20)), false, false, 100),
        Wallpaper("6", "Çöl", listOf(Color(0xFFFF9800), Color(0xFFE65100)), true, false, 250)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tema Mağazası", fontWeight = FontWeight.Bold) },
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
                        Text("$userCoins", fontWeight = FontWeight.Bold, color = Color.White)
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
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF6C63FF),
                contentColor = Color.White
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text("Sohbet Temaları", modifier = Modifier.padding(12.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text("Duvar Kağıtları", modifier = Modifier.padding(12.dp))
                }
            }

            when (selectedTab) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(themes) { theme ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedTheme = theme.id },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedTheme == theme.id)
                                        theme.primaryColor.copy(alpha = 0.1f)
                                    else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Theme preview
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    Brush.linearGradient(
                                                        colors = listOf(theme.primaryColor, theme.secondaryColor)
                                                    )
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color.White.copy(alpha = 0.9f))
                                                    .padding(6.dp)
                                            ) {
                                                Text(
                                                    "Merhaba",
                                                    fontSize = 8.sp,
                                                    color = theme.primaryColor,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(theme.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                if (theme.isPremium) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Icon(
                                                        Icons.Filled.Diamond,
                                                        contentDescription = "Premium",
                                                        tint = Color(0xFFFFD700),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                            if (theme.isPremium && !theme.isPurchased) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        Icons.Filled.MonetizationOn,
                                                        contentDescription = null,
                                                        tint = Color(0xFFFFD700),
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Text(
                                                        "${theme.price} Coin",
                                                        fontSize = 12.sp,
                                                        color = Color(0xFFFFD700)
                                                    )
                                                }
                                            }
                                        }

                                        if (theme.isPurchased) {
                                            Icon(
                                                Icons.Filled.CheckCircle,
                                                contentDescription = "Satın Alındı",
                                                tint = Color(0xFF4CAF50),
                                                modifier = Modifier.size(28.dp)
                                            )
                                        } else {
                                            Button(
                                                onClick = {
                                                    if (theme.isPremium && !theme.isPurchased) {
                                                        if (userCoins >= theme.price) {
                                                            userCoins -= theme.price
                                                            onBuyTheme(theme.id)
                                                        }
                                                    } else {
                                                        selectedTheme = theme.id
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (theme.isPurchased || !theme.isPremium || userCoins >= theme.price)
                                                        Color(0xFF6C63FF) else Color.Gray
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.height(36.dp)
                                            ) {
                                                Text(
                                                    when {
                                                        theme.isPurchased -> "Uygula"
                                                        !theme.isPremium -> "Seç"
                                                        else -> "Al"
                                                    },
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(wallpapers) { wallpaper ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.7f)
                                    .clickable { },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(wallpaper.gradientColors)
                                        ),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Black.copy(alpha = 0.3f))
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            wallpaper.name,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (wallpaper.isPremium && !wallpaper.isPurchased) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Filled.MonetizationOn,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFFD700),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Text(
                                                    "${wallpaper.price}",
                                                    color = Color.White,
                                                    fontSize = 11.sp
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
        }
    }
}
