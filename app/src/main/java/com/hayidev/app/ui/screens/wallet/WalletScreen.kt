package com.hayidev.app.ui.screens.wallet

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CoinPackage(
    val coins: Int,
    val price: String,
    val popular: Boolean = false,
    val bonus: String? = null
)

data class Transaction(
    val type: String,
    val amount: Int,
    val description: String,
    val date: String,
    val isPositive: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    onBack: () -> Unit = {},
    onBuyCoins: (CoinPackage) -> Unit = {},
    onClaimDailyReward: () -> Unit = {}
) {
    var coinBalance by remember { mutableIntStateOf(2450) }
    var diamondBalance by remember { mutableIntStateOf(125) }
    var dailyRewardClaimed by remember { mutableStateOf(false) }
    var showPurchaseDialog by remember { mutableStateOf(false) }
    var selectedPackage by remember { mutableStateOf<CoinPackage?>(null) }

    val coinPackages = listOf(
        CoinPackage(100, "₺9.99"),
        CoinPackage(500, "₺39.99", bonus = "+50 Bonus"),
        CoinPackage(1000, "₺69.99", popular = true, bonus = "+150 Bonus"),
        CoinPackage(5000, "₺299.99", bonus = "+1000 Bonus")
    )

    val transactions = listOf(
        Transaction("Premium Satın Alma", -500, "Aylık Premium", "14 Eyl 2026", false),
        Transaction("Günlük Hediye", +50, "Günlük giriş ödülü", "14 Eyl 2026", true),
        Transaction("Canlı Yayın Hediyesi", +200, "Ahmet'ten hediye", "13 Eyl 2026", true),
        Transaction("Coin Satın Alma", +500, "500 coin paketi", "12 Eyl 2026", true),
        Transaction("Referans Ödülü", +100, "Arkadaş daveti", "11 Eyl 2026", true),
        Transaction("Sticker Satın Alma", -50, "Özel sticker paketi", "10 Eyl 2026", false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cüzdan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6C63FF))
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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF6C63FF), Color(0xFF9C27B0))
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column {
                            Text("Bakiyeniz", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.MonetizationOn,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$coinBalance",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Coin", fontSize = 18.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Diamond,
                                    contentDescription = null,
                                    tint = Color(0xFF00BCD4),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$diamondBalance",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Elmas", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !dailyRewardClaimed) {
                            dailyRewardClaimed = true
                            coinBalance += 50
                            onClaimDailyReward()
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (dailyRewardClaimed)
                            MaterialTheme.colorScheme.surfaceVariant
                        else Color(0xFFFFF3E0)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    if (dailyRewardClaimed) Color.Gray
                                    else Color(0xFFFF9800)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.CardGiftcard,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (dailyRewardClaimed) "Bugünkü ödülünüzü aldınız!" else "Günlük Ödül",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (dailyRewardClaimed) "Yarın tekrar gelin" else "+50 Coin Kazanın",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (!dailyRewardClaimed) {
                            Button(
                                onClick = {
                                    dailyRewardClaimed = true
                                    coinBalance += 50
                                    onClaimDailyReward()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                            ) {
                                Text("Al")
                            }
                        } else {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            item {
                Text("Coin Paketleri", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(280.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(coinPackages) { pkg ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPackage = pkg
                                    showPurchaseDialog = true
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (pkg.popular) Color(0xFF6C63FF)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                if (pkg.popular) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .background(
                                                Color(0xFFFFD700),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("Popüler", fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Filled.MonetizationOn,
                                        contentDescription = null,
                                        tint = if (pkg.popular) Color(0xFFFFD700) else Color(0xFFFF9800),
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${pkg.coins} Coin",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = if (pkg.popular) Color.White else Color.Unspecified
                                    )
                                    if (pkg.bonus != null) {
                                        Text(
                                            text = pkg.bonus,
                                            fontSize = 12.sp,
                                            color = if (pkg.popular) Color(0xFFFFD700) else Color(0xFF4CAF50),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = pkg.price,
                                        fontSize = 14.sp,
                                        color = if (pkg.popular) Color.White.copy(alpha = 0.8f)
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text("İşlem Geçmişi", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            items(transactions) { transaction ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (transaction.isPositive) Color(0xFF4CAF50).copy(alpha = 0.1f)
                                    else Color(0xFFF44336).copy(alpha = 0.1f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (transaction.isPositive) Icons.Filled.TrendingUp
                                else Icons.Filled.TrendingDown,
                                contentDescription = null,
                                tint = if (transaction.isPositive) Color(0xFF4CAF50) else Color(0xFFF44336),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = transaction.type, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = transaction.description,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${if (transaction.isPositive) "+" else ""}${transaction.amount}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (transaction.isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                            Text(
                                text = transaction.date,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (showPurchaseDialog && selectedPackage != null) {
        AlertDialog(
            onDismissRequest = {
                showPurchaseDialog = false
                selectedPackage = null
            },
            title = { Text("Coin Satın Al") },
            text = {
                Text("${selectedPackage!!.coins} coin'i ${selectedPackage!!.price} fiyatıyla satın almak istediğinize emin misiniz?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPurchaseDialog = false
                        selectedPackage?.let { onBuyCoins(it) }
                        selectedPackage = null
                    }
                ) {
                    Text("Satın Al")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPurchaseDialog = false
                    selectedPackage = null
                }) {
                    Text("İptal")
                }
            }
        )
    }
}
