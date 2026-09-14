package com.hayidev.app.ui.screens.blocked

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BlockedUser(
    val id: String,
    val username: String,
    val fullName: String,
    val avatarUrl: String,
    val blockedDate: String,
    val reason: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedUsersScreen(
    onBack: () -> Unit = {},
    onUnblockUser: (String) -> Unit = {},
    onAddBlockedUser: () -> Unit = {}
) {
    var blockedUsers by remember {
        mutableStateOf(
            listOf(
                BlockedUser("1", "spam_user1", "Spam Hesap 1", "https://i.pravatar.cc/150?img=1", "12 Oca 2024", "Spam"),
                BlockedUser("2", "troll_kullanıcı", "Troll Kullanıcı", "https://i.pravatar.cc/150?img=2", "15 Oca 2024", "Taciz"),
                BlockedUser("3", "bot_account", "Bot Hesap", "https://i.pravatar.cc/150?img=3", "18 Oca 2024", "Sahte hesap"),
                BlockedUser("4", "istenmeyen_kişi", "İstenmeyen Kişi", "https://i.pravatar.cc/150?img=4", "20 Oca 2024", "İstenmeyen iletişim"),
                BlockedUser("5", "reklam_bot", "Reklam Bot", "https://i.pravatar.cc/150?img=5", "22 Oca 2024", "Reklam")
            )
        )
    }

    var unblockDialogUser by remember { mutableStateOf<BlockedUser?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = blockedUsers.filter {
        it.username.contains(searchQuery, ignoreCase = true) ||
                it.fullName.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Engellenen Kullanıcılar", fontWeight = FontWeight.Bold)
                        Text(
                            "${blockedUsers.size} engellenen",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = onAddBlockedUser) {
                        Icon(Icons.Filled.PersonAdd, contentDescription = "Kullanıcı Engelle")
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Engellenen kullanıcı ara...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Temizle")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            if (blockedUsers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.Block,
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Engellenen kullanıcı yok",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9E9E9E)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Engellediğiniz kullanıcılar burada görünecek",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredUsers) { user ->
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
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(Color(0xFF9E9E9E), Color(0xFF616161))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.Person,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(user.username, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(
                                        user.fullName,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "Engellendi: ${user.blockedDate}",
                                            fontSize = 11.sp,
                                            color = Color(0xFFE53935)
                                        )
                                        Text(" • ", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(
                                            user.reason,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = { unblockDialogUser = user },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(32.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF4CAF50)
                                    )
                                ) {
                                    Icon(
                                        Icons.Filled.LockOpen,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Kaldır", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Unblock confirmation dialog
    if (unblockDialogUser != null) {
        AlertDialog(
            onDismissRequest = { unblockDialogUser = null },
            title = { Text("Engeli Kaldır") },
            text = {
                Text("${unblockDialogUser!!.fullName} kullanıcısının engelini kaldırmak istediğinize emin misiniz?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onUnblockUser(unblockDialogUser!!.id)
                        blockedUsers = blockedUsers.filter { it.id != unblockDialogUser!!.id }
                        unblockDialogUser = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CAF50))
                ) {
                    Text("Engeli Kaldır")
                }
            },
            dismissButton = {
                TextButton(onClick = { unblockDialogUser = null }) {
                    Text("İptal")
                }
            }
        )
    }
}
