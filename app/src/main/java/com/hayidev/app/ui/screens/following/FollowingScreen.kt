package com.hayidev.app.ui.screens.following

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

data class FollowingUser(
    val id: String,
    val username: String,
    val fullName: String,
    val avatarUrl: String,
    val isVerified: Boolean,
    val isCloseFriend: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowingScreen(
    onBack: () -> Unit = {},
    onUserClick: (String) -> Unit = {},
    userId: String = ""
) {
    var searchQuery by remember { mutableStateOf("") }
    var following by remember {
        mutableStateOf(
            listOf(
                FollowingUser("1", "ahmet_dev", "Ahmet Yılmaz", "https://i.pravatar.cc/150?img=1", true, true),
                FollowingUser("2", "elif_dsgn", "Elif Demir", "https://i.pravatar.cc/150?img=2", true, false),
                FollowingUser("3", "mehmet_app", "Mehmet Kaya", "https://i.pravatar.cc/150?img=3", false, true),
                FollowingUser("4", "zeynep_photo", "Zeynep Çelik", "https://i.pravatar.cc/150?img=4", false, false),
                FollowingUser("5", "ali_code", "Ali Öztürk", "https://i.pravatar.cc/150?img=5", true, false),
                FollowingUser("6", "sude_writes", "Sude Arslan", "https://i.pravatar.cc/150?img=6", false, true),
                FollowingUser("7", "berk_fitness", "Berk Yıldız", "https://i.pravatar.cc/150?img=7", false, false),
                FollowingUser("8", "ayşe_art", "Ayşe Korkmaz", "https://i.pravatar.cc/150?img=8", true, false),
                FollowingUser("9", "can_music", "Can Demirtaş", "https://i.pravatar.cc/150?img=9", false, false),
                FollowingUser("10", "deniz_tech", "Deniz Aydın", "https://i.pravatar.cc/150?img=10", true, true)
            )
        )
    }

    var unfollowDialogUser by remember { mutableStateOf<FollowingUser?>(null) }

    val filteredFollowing = following.filter {
        it.username.contains(searchQuery, ignoreCase = true) ||
                it.fullName.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Takip Edilenler", fontWeight = FontWeight.Bold)
                        Text(
                            "${following.size} takip edilen",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6C63FF)
                )
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
                placeholder = { Text("Takip edilen ara...") },
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredFollowing) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onUserClick(user.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            colors = if (user.isCloseFriend)
                                                listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
                                            else
                                                listOf(Color(0xFF6C63FF), Color(0xFF9C27B0))
                                        )
                                    )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = user.username,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    if (user.isVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Filled.Verified,
                                            contentDescription = "Doğrulanmış",
                                            tint = Color(0xFF6C63FF),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = user.fullName,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            OutlinedButton(
                                onClick = { unfollowDialogUser = user },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.height(36.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFE53935)
                                )
                            ) {
                                Text("Takipten Çıkar", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Unfollow confirmation dialog
    if (unfollowDialogUser != null) {
        AlertDialog(
            onDismissRequest = { unfollowDialogUser = null },
            title = { Text("Takipten Çıkar") },
            text = {
                Text("${unfollowDialogUser!!.fullName} kullanıcısını takip etmeyi bırakmak istediğinize emin misiniz?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        following = following.filter { it.id != unfollowDialogUser!!.id }
                        unfollowDialogUser = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFE53935))
                ) {
                    Text("Takipten Çıkar")
                }
            },
            dismissButton = {
                TextButton(onClick = { unfollowDialogUser = null }) {
                    Text("İptal")
                }
            }
        )
    }
}
