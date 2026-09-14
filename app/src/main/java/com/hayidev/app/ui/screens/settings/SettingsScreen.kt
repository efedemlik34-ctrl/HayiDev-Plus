package com.hayidev.app.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Ayarlar",
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
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // Account Section
            Text(
                text = "Hesap",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            SettingsItem(
                icon = Icons.Filled.Person,
                title = "Profili Düzenle",
                subtitle = "Ad, bio, ilgi alanları",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Filled.PhotoCamera,
                title = "Fotoğraflarım",
                subtitle = "Profil fotoğraflarını yönet",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Filled.LocationOn,
                title = "Konum",
                subtitle = "Konum bilgilerini ayarla",
                onClick = { }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Preferences Section
            Text(
                text = "Tercihler",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            SettingsItem(
                icon = Icons.Filled.Language,
                title = "Dil",
                subtitle = "Türkçe",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Filled.DarkMode,
                title = "Tema",
                subtitle = "Sistem ayarı",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Filled.Notifications,
                title = "Bildirimler",
                subtitle = "Bildirim tercihlerini yönet",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Filled.LocationOff,
                title = "Gizli Konum",
                subtitle = "Konumumu gizle",
                onClick = { },
                isToggle = true,
                isChecked = false
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Privacy Section
            Text(
                text = "Gizlilik ve Güvenlik",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            SettingsItem(
                icon = Icons.Filled.Block,
                title = "Engellenen Kullanıcılar",
                subtitle = "Engeli kaldır",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Filled.VisibilityOff,
                title = "Görünürlük",
                subtitle = "Profilimi gizle",
                onClick = { },
                isToggle = true,
                isChecked = false
            )

            SettingsItem(
                icon = Icons.Filled.Security,
                title = "Hesap Güvenliği",
                subtitle = "Şifre, 2FA",
                onClick = { }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Support Section
            Text(
                text = "Destek",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            SettingsItem(
                icon = Icons.Filled.Help,
                title = "Yardım Merkezi",
                subtitle = "SSS ve destek",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Filled.Feedback,
                title = "Geri Bildirim",
                subtitle = "Öneri ve şikayetler",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Filled.Description,
                title = "Kullanım Şartları",
                subtitle = "Yasal bilgiler",
                onClick = { }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Logout
            SettingsItem(
                icon = Icons.Filled.Logout,
                title = "Çıkış Yap",
                subtitle = "",
                onClick = { },
                titleColor = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(32.dp))

            // App Version
            Text(
                text = "HayiDev++ v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    isToggle: Boolean = false,
    isChecked: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = titleColor
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        if (isToggle) {
            Switch(
                checked = isChecked,
                onCheckedChange = { }
            )
        } else {
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}
