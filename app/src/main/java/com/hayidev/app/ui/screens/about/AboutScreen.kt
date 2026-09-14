package com.hayidev.app.ui.screens.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit = {},
    onOpenUrl: (String) -> Unit = {},
    onRateApp: () -> Unit = {},
    onShareApp: () -> Unit = {},
    onOpenLicenses: () -> Unit = {}
) {
    val context = LocalContext.current
    var showTeamDialog by remember { mutableStateOf(false) }

    val teamMembers = listOf(
        Triple("Ahmet Yılmaz", "Kurucu & Baş Geliştirici", "ahmet@hayidev.com"),
        Triple("Elif Demir", "UI/UX Tasarımcı", "elif@hayidev.com"),
        Triple("Mehmet Kaya", "Backend Geliştirici", "mehmet@hayidev.com"),
        Triple("Zeynep Çelik", "Mobil Geliştirici", "zeynep@hayidev.com")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Uygulama Hakkında", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // App logo & name
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF6C63FF), Color(0xFF9C27B0))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Code,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "HayiDev++",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6C63FF)
                        )
                        Text(
                            "Sosyal Geliştirici Platformu",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Versiyon 2.5.0 (Build 250)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Team
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTeamDialog = true },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF).copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Groups,
                            contentDescription = null,
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Ekibimiz", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "${teamMembers.size} üye",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFF6C63FF)
                        )
                    }
                }
            }

            // Links
            item {
                Text("Bağlantılar", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6C63FF))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        AboutActionItem(
                            icon = Icons.Filled.Language,
                            title = "Web Sitemiz",
                            subtitle = "www.hayidev.com",
                            onClick = { onOpenUrl("https://hayidev.com") }
                        )
                        HorizontalDivider()
                        AboutActionItem(
                            icon = Icons.Filled.Star,
                            title = "Bizi Değerlendirin",
                            subtitle = "Mağazada puan verin",
                            onClick = onRateApp
                        )
                        HorizontalDivider()
                        AboutActionItem(
                            icon = Icons.Filled.Share,
                            title = "Arkadaşlarınızla Paylaşın",
                            subtitle = "Uygulamamızı önerin",
                            onClick = onShareApp
                        )
                    }
                }
            }

            // Social media
            item {
                Text("Sosyal Medya", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6C63FF))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        AboutActionItem(
                            icon = Icons.Filled.Twitter,
                            title = "Twitter",
                            subtitle = "@hayidevapp",
                            onClick = { onOpenUrl("https://twitter.com/hayidevapp") }
                        )
                        HorizontalDivider()
                        AboutActionItem(
                            icon = Icons.Filled.Instagram,
                            title = "Instagram",
                            subtitle = "@hayidevapp",
                            onClick = { onOpenUrl("https://instagram.com/hayidevapp") }
                        )
                        HorizontalDivider()
                        AboutActionItem(
                            icon = Icons.Filled.YouTube,
                            title = "YouTube",
                            subtitle = "HayiDev Official",
                            onClick = { onOpenUrl("https://youtube.com/@hayidev") }
                        )
                    }
                }
            }

            // Legal
            item {
                Text("Yasal", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6C63FF))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        AboutActionItem(
                            icon = Icons.Filled.Description,
                            title = "Hizmet Şartları",
                            subtitle = "Kullanım koşulları",
                            onClick = { onOpenUrl("https://hayidev.com/terms") }
                        )
                        HorizontalDivider()
                        AboutActionItem(
                            icon = Icons.Filled.PrivacyTip,
                            title = "Gizlilik Politikası",
                            subtitle = "Veri koruma politikası",
                            onClick = { onOpenUrl("https://hayidev.com/privacy") }
                        )
                        HorizontalDivider()
                        AboutActionItem(
                            icon = Icons.Filled.Gavel,
                            title = "Lisanslar",
                            subtitle = "Açık kaynak lisansları",
                            onClick = onOpenLicenses
                        )
                    }
                }
            }

            // Footer
            item {
                Text(
                    text = "2024 HayiDev++. Tüm hakları saklıdır.\nMade with ❤️ in Istanbul",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }

    // Team dialog
    if (showTeamDialog) {
        AlertDialog(
            onDismissRequest = { showTeamDialog = false },
            title = { Text("Ekibimiz", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    teamMembers.forEach { (name, role, email) ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text(role, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                email,
                                fontSize = 12.sp,
                                color = Color(0xFF6C63FF),
                                modifier = Modifier.clickable {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:$email")
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        }
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTeamDialog = false }) {
                    Text("Kapat")
                }
            }
        )
    }
}

@Composable
private fun AboutActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF6C63FF))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(
                subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
