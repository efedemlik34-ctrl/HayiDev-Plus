package com.hayidev.app.ui.screens.referral

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ReferralTier(
    val id: String,
    val name: String,
    val requiredReferrals: Int,
    val reward: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val isUnlocked: Boolean
)

data class ReferredUser(
    val id: String,
    val username: String,
    val joinDate: String,
    val status: String,
    val rewardEarned: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralScreen(
    onBack: () -> Unit = {},
    onShareCode: (String) -> Unit = {},
    onCopyCode: (String) -> Unit = {},
    onLearnMore: () -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    var referralCode by remember { mutableStateOf("AHMET2024") }
    var totalReferrals by remember { mutableIntStateOf(12) }
    var totalEarned by remember { mutableIntStateOf(600) }

    val tiers = listOf(
        ReferralTier("1", "Bronz", 5, "50 Coin", Icons.Filled.EmojiEvents, true),
        ReferralTier("2", "Gümüş", 15, "150 Coin", Icons.Filled.EmojiEvents, true),
        ReferralTier("3", "Altın", 30, "300 Coin", Icons.Filled.EmojiEvents, false),
        ReferralTier("4", "Elmas", 50, "500 Coin + Premium 1 Ay", Icons.Filled.Diamond, false),
        ReferralTier("5", "Efsane", 100, "1000 Coin + Premium Ömür Boyu", Icons.Filled.Star, false)
    )

    val referredUsers = listOf(
        ReferredUser("1", "elif_new", "12 Oca 2024", "Katıldı", 50),
        ReferredUser("2", "mehmet_k", "15 Oca 2024", "Katıldı", 50),
        ReferredUser("3", "zeynep_x", "18 Oca 2024", "Beklemede", 0),
        ReferredUser("4", "ali_dev", "20 Oca 2024", "Katıldı", 50),
        ReferredUser("5", "sude_y", "22 Oca 2024", "Katıldı", 50)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Davet Sistemi", fontWeight = FontWeight.Bold) },
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
            // Header card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.CardGiftcard,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Arkadaşlarını Davet Et",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "Her davet için ödül kazan!",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "$totalReferrals",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text("Davet", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "$totalEarned",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                                Text("Coin Kazanıldı", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Referral code
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Davet Kodun", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = referralCode,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6C63FF),
                                letterSpacing = 4.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(referralCode))
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Kopyala")
                            }
                            Button(
                                onClick = { onShareCode(referralCode) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Paylaş")
                            }
                        }
                    }
                }
            }

            // Reward tiers
            item {
                Text("Ödül Kademeleri", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(tiers) { tier ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (tier.isUnlocked)
                            Color(0xFF4CAF50).copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surface
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
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (tier.isUnlocked)
                                        Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA000)))
                                    else
                                        Brush.linearGradient(listOf(Color(0xFF9E9E9E), Color(0xFF616161)))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                tier.icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(tier.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "${tier.requiredReferrals} davet gerekli",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                tier.reward,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (tier.isUnlocked) Color(0xFF4CAF50) else Color(0xFFFFD700)
                            )
                            if (tier.isUnlocked) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = "Açıldı",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Referred users
            item {
                Text("Davet Edilen Kullanıcılar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(referredUsers) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6C63FF).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = null,
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(user.username, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text(
                                user.joinDate,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                user.status,
                                fontSize = 12.sp,
                                color = if (user.status == "Katıldı") Color(0xFF4CAF50) else Color(0xFFFF9800)
                            )
                            if (user.rewardEarned > 0) {
                                Text(
                                    "+${user.rewardEarned} Coin",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFFD700),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item {
                TextButton(
                    onClick = onLearnMore,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Nasıl Çalışır?", color = Color(0xFF6C63FF))
                }
            }
        }
    }
}
