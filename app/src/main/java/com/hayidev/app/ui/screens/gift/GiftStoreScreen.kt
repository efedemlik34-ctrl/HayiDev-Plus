package com.hayidev.app.ui.screens.gift

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hayidev.app.data.model.Currency
import com.hayidev.app.data.model.Gift
import com.hayidev.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftStoreScreen(
    onBackClick: () -> Unit
) {
    var selectedCategory by remember { mutableIntStateOf(0) }
    var userCoins by remember { mutableIntStateOf(2450) }

    val gifts = remember {
        listOf(
            Gift("1", "Kalp", "", "", 10, Currency.COIN),
            Gift("2", "Gül", "", "", 25, Currency.COIN),
            Gift("3", "Elmas", "", "", 100, Currency.DIAMOND),
            Gift("4", "Lüks Araba", "", "", 500, Currency.COIN),
            Gift("5", "Yat", "", "", 1000, Currency.COIN),
            Gift("6", "Taç", "", "", 750, Currency.COIN),
            Gift("7", "Roket", "", "", 2000, Currency.COIN),
            Gift("8", "Pırlanta", "", "", 1500, Currency.DIAMOND),
            Gift("9", "Altın", "", "", 300, Currency.COIN),
            Gift("10", "Gümüş", "", "", 150, Currency.COIN),
            Gift("11", "Bronz", "", "", 50, Currency.COIN),
            Gift("12", "Sürpriz Kutu", "", "", 200, Currency.COIN)
        )
    }

    val categories = listOf("Tümü", "Popüler", "Yeni", "Evcil Hayvanlar", "Lüks", "Sevgililer Günü")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Hediye Mağazası",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                actions = {
                    // Coin Balance
                    Row(
                        modifier = Modifier
                            .background(GoldCoin.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.MonetizationOn,
                            contentDescription = null,
                            tint = GoldCoin,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$userCoins",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Category Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedCategory,
                containerColor = MaterialTheme.colorScheme.surface,
                edgePadding = 16.dp
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedCategory == index,
                        onClick = { selectedCategory = index },
                        text = { Text(category) }
                    )
                }
            }

            // Gift Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(gifts) { gift ->
                    GiftItem(
                        gift = gift,
                        onClick = { }
                    )
                }
            }

            // Buy Coins Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldCoin
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Coin Satın Al",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun GiftItem(
    gift: Gift,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Gift Icon Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when (gift.category) {
                            com.hayidev.app.data.model.GiftCategory.LEGENDARY -> GoldCoin.copy(alpha = 0.2f)
                            com.hayidev.app.data.model.GiftCategory.EPIC -> AccentPurple.copy(alpha = 0.2f)
                            com.hayidev.app.data.model.GiftCategory.RARE -> AccentBlue.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (gift.name) {
                        "Kalp" -> Icons.Filled.Favorite
                        "Gül" -> Icons.Filled.LocalFlorist
                        "Elmas" -> Icons.Filled.Diamond
                        "Lüks Araba" -> Icons.Filled.DirectionsCar
                        "Yat" -> Icons.Filled.Sailing
                        "Taç" -> Icons.Filled.EmojiEvents
                        "Roket" -> Icons.Filled.Rocket
                        else -> Icons.Filled.CardGiftcard
                    },
                    contentDescription = null,
                    tint = when (gift.category) {
                        com.hayidev.app.data.model.GiftCategory.LEGENDARY -> GoldCoin
                        com.hayidev.app.data.model.GiftCategory.EPIC -> AccentPurple
                        com.hayidev.app.data.model.GiftCategory.RARE -> AccentBlue
                        else -> MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = gift.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (gift.currency == Currency.COIN) Icons.Filled.MonetizationOn else Icons.Filled.Diamond,
                    contentDescription = null,
                    tint = if (gift.currency == Currency.COIN) GoldCoin else Diamond,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${gift.price}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (gift.currency == Currency.COIN) GoldCoin else Diamond
                )
            }
        }
    }
}
