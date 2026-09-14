package com.hayidev.app.ui.screens.privacy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(
    onBack: () -> Unit = {},
    onBlockedUsers: () -> Unit = {},
    onMutedUsers: () -> Unit = {},
    onDataDownload: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    onPrivacyPolicy: () -> Unit = {}
) {
    var isPrivateAccount by remember { mutableStateOf(false) }
    var showOnlineStatus by remember { mutableStateOf(true) }
    var showReadReceipts by remember { mutableStateOf(true) }
    var allowTagging by remember { mutableStateOf(true) }
    var allowMentions by remember { mutableStateOf(true) }
    var showActivityStatus by remember { mutableStateOf(true) }
    var showStoryToCloseFriends by remember { mutableStateOf(false) }
    var restrictDMs by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gizlilik Ayarları", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Account Privacy
            item {
                Text("Hesap Gizliliği", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6C63FF))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
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
                                "Yalnızca onaylanmış takipçileriniz gönderilerinizi görebilir",
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
                }
            }

            // Activity Status
            item {
                Text("Aktivite Durumu", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6C63FF))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        PrivacyToggleItem(
                            icon = Icons.Filled.Visibility,
                            title = "Çevrimiçi Durumunu Göster",
                            description = "Diğer kullanıcılar çevrimiçi olduğunuzu görebilir",
                            checked = showOnlineStatus,
                            onCheckedChange = { showOnlineStatus = it }
                        )
                        HorizontalDivider()
                        PrivacyToggleItem(
                            icon = Icons.Filled.Schedule,
                            title = "Aktivite Durumunu Göster",
                            description = "Son giriş zamanınızı gösterir",
                            checked = showActivityStatus,
                            onCheckedChange = { showActivityStatus = it }
                        )
                    }
                }
            }

            // Messaging
            item {
                Text("Mesajlaşma", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6C63FF))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        PrivacyToggleItem(
                            icon = Icons.Filled.DoneAll,
                            title = "Okundu Bilgisi",
                            description = "Mesajlarınızın okundu bilgisini gösterir",
                            checked = showReadReceipts,
                            onCheckedChange = { showReadReceipts = it }
                        )
                        HorizontalDivider()
                        PrivacyToggleItem(
                            icon = Icons.Filled.Mail,
                            title = "Direkt Mesaj Kısıtlamaları",
                            description = "Yalnızca takip ettiklerinizden mesaj alabilirsiniz",
                            checked = restrictDMs,
                            onCheckedChange = { restrictDMs = it }
                        )
                    }
                }
            }

            // Interactions
            item {
                Text("Etkileşimler", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6C63FF))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        PrivacyToggleItem(
                            icon = Icons.Filled.Tag,
                            title = "Etiketlemelere İzin Ver",
                            description = "Diğer kullanıcılar sizi fotoğraf ve videolarda etiketleyebilir",
                            checked = allowTagging,
                            onCheckedChange = { allowTagging = it }
                        )
                        HorizontalDivider()
                        PrivacyToggleItem(
                            icon = Icons.Filled.AlternateEmail,
                            title = "Bahsetmelere İzin Ver",
                            description = "Diğer kullanıcılar sizi bahsetmelerde kullanabilir",
                            checked = allowMentions,
                            onCheckedChange = { allowMentions = it }
                        )
                        HorizontalDivider()
                        PrivacyToggleItem(
                            icon = Icons.Filled.People,
                            title = "Yakın Arkadaşlara Özel Hikaye",
                            description = "Hikayelerinizi yalnızca yakın arkadaşlarınıza gösterin",
                            checked = showStoryToCloseFriends,
                            onCheckedChange = { showStoryToCloseFriends = it }
                        )
                    }
                }
            }

            // Data & Account
            item {
                Text("Veri ve Hesap", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6C63FF))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        PrivacyActionItem(
                            icon = Icons.Filled.Block,
                            title = "Engellenen Kullanıcılar",
                            description = "Engellediğiniz kullanıcıları yönetin",
                            onClick = onBlockedUsers
                        )
                        HorizontalDivider()
                        PrivacyActionItem(
                            icon = Icons.Filled.VolumeOff,
                            title = "Sessize Alınan Kullanıcılar",
                            description = "Sessize aldığınız kullanıcıları yönetin",
                            onClick = onMutedUsers
                        )
                        HorizontalDivider()
                        PrivacyActionItem(
                            icon = Icons.Filled.Download,
                            title = "Verilerimi İndir",
                            description = "Tüm verilerinizi indirin",
                            onClick = onDataDownload
                        )
                        HorizontalDivider()
                        PrivacyActionItem(
                            icon = Icons.Filled.DeleteForever,
                            title = "Hesabı Sil",
                            description = "Hesabınızı kalıcı olarak silin",
                            onClick = onDeleteAccount,
                            isDestructive = true
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
                    PrivacyActionItem(
                        icon = Icons.Filled.Gavel,
                        title = "Gizlilik Politikası",
                        description = "Gizlilik politikamızı okuyun",
                        onClick = onPrivacyPolicy
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun PrivacyToggleItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF6C63FF))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(
                description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF6C63FF))
        )
    }
}

@Composable
private fun PrivacyActionItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isDestructive) Color(0xFFE53935) else Color(0xFF6C63FF)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                color = if (isDestructive) Color(0xFFE53935) else Color.Unspecified
            )
            Text(
                description,
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
