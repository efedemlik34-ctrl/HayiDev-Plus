package com.hayidev.app.ui.screens.notification

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppNotification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val description: String,
    val time: String,
    val isRead: Boolean,
    val imageUrl: String? = null
)

enum class NotificationType(val icon: ImageVector, val color: Color) {
    MESSAGE(Icons.Filled.Chat, Color(0xFF2196F3)),
    MATCH(Icons.Filled.Favorite, Color(0xFFE91E63)),
    GIFT(Icons.Filled.CardGiftcard, Color(0xFFFF9800)),
    LIVE(Icons.Filled.Videocam, Color(0xFF9C27B0)),
    SYSTEM(Icons.Filled.Info, Color(0xFF607D8B))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onBack: () -> Unit = {},
    onNotificationClick: (AppNotification) -> Unit = {}
) {
    var notifications by remember {
        mutableStateOf(
            listOf(
                AppNotification("1", NotificationType.MESSAGE, "Yeni Mesaj", "Ayşe size mesaj gönderdi", "2 dk önce", false),
                AppNotification("2", NotificationType.MATCH, "Yeni Eşleşme!", "Mehmet ile eşleştiniz!", "15 dk önce", false),
                AppNotification("3", NotificationType.GIFT, "Hediye Aldınız!", "Zeynep size çiçek gönderdi", "1 saat önce", true),
                AppNotification("4", NotificationType.LIVE, "Canlı Yayın", "Ali şimdi canlı yayında", "2 saat önce", true),
                AppNotification("5", NotificationType.SYSTEM, "Sistem Bildirimi", "Profiliniz onaylandı!", "3 saat önce", true),
                AppNotification("6", NotificationType.MESSAGE, "Yeni Mesaj", "Fatma size mesaj gönderdi", "5 saat önce", true),
                AppNotification("7", NotificationType.MATCH, "Yeni Eşleşme!", "Hasan ile eşleştiniz!", "1 gün önce", true),
                AppNotification("8", NotificationType.GIFT, "Hediye Aldınız!", "Kemal size kalp gönderdi", "1 gün önce", true)
            )
        )
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tümü", "Mesajlar", "Eşleşmeler", "Hediyeler", "Canlı", "Sistem")

    val filteredNotifications = when (selectedTab) {
        0 -> notifications
        1 -> notifications.filter { it.type == NotificationType.MESSAGE }
        2 -> notifications.filter { it.type == NotificationType.MATCH }
        3 -> notifications.filter { it.type == NotificationType.GIFT }
        4 -> notifications.filter { it.type == NotificationType.LIVE }
        5 -> notifications.filter { it.type == NotificationType.SYSTEM }
        else -> notifications
    }

    val unreadCount = notifications.count { !it.isRead }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Bildirimler", fontWeight = FontWeight.Bold)
                        if (unreadCount > 0) {
                            Text(
                                text = "$unreadCount okunmamış",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    if (unreadCount > 0) {
                        IconButton(onClick = {
                            notifications = notifications.map { it.copy(isRead = true) }
                        }) {
                            Icon(Icons.Filled.DoneAll, contentDescription = "Tümünü Okundu İşaretle")
                        }
                    }
                    IconButton(onClick = {
                        notifications = emptyList()
                    }) {
                        Icon(Icons.Filled.DeleteSweep, contentDescription = "Tümünü Temizle")
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
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                edgePadding = 8.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(title)
                                if (index > 0) {
                                    val count = when (index) {
                                        1 -> notifications.count { it.type == NotificationType.MESSAGE && !it.isRead }
                                        2 -> notifications.count { it.type == NotificationType.MATCH && !it.isRead }
                                        3 -> notifications.count { it.type == NotificationType.GIFT && !it.isRead }
                                        4 -> notifications.count { it.type == NotificationType.LIVE && !it.isRead }
                                        5 -> notifications.count { it.type == NotificationType.SYSTEM && !it.isRead }
                                        else -> 0
                                    }
                                    if (count > 0) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Badge(
                                            containerColor = Color(0xFFE91E63),
                                            content = {
                                                Text("$count", fontSize = 10.sp, color = Color.White)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            if (filteredNotifications.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.NotificationsOff,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Bildirim bulunmuyor",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredNotifications, key = { it.id }) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = {
                                onNotificationClick(notification)
                                notifications = notifications.map {
                                    if (it.id == notification.id) it.copy(isRead = true) else it
                                }
                            },
                            onDelete = {
                                notifications = notifications.filter { it.id != notification.id }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead)
                MaterialTheme.colorScheme.surface
            else Color(0xFF6C63FF).copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(notification.type.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = notification.type.icon,
                    contentDescription = null,
                    tint = notification.type.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.title,
                        fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    if (!notification.isRead) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE91E63))
                        )
                    }
                }
                Text(
                    text = notification.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = notification.time,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Sil",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
