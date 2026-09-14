package com.hayidev.app.ui.screens.followers

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

data class FollowerUser(
    val id: String,
    val username: String,
    val fullName: String,
    val avatarUrl: String,
    val isFollowing: Boolean,
    val isVerified: Boolean,
    val mutualFriends: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowersScreen(
    onBack: () -> Unit = {},
    onUserClick: (String) -> Unit = {},
    userId: String = ""
) {
    var searchQuery by remember { mutableStateOf("") }
    var followers by remember {
        mutableStateOf(
            listOf(
                FollowerUser("1", "elif_dsgn", "Elif Demir", "https://i.pravatar.cc/150?img=1", true, true, 5),
                FollowerUser("2", "mehmet_app", "Mehmet Kaya", "https://i.pravatar.cc/150?img=2", false, false, 12),
                FollowerUser("3", "zeynep_photo", "Zeynep Çelik", "https://i.pravatar.cc/150?img=3", true, false, 3),
                FollowerUser("4", "ali_code", "Ali Öztürk", "https://i.pravatar.cc/150?img=4", false, true, 8),
                FollowerUser("5", "sude_writes", "Sude Arslan", "https://i.pravatar.cc/150?img=5", true, false, 1),
                FollowerUser("6", "berk_fitness", "Berk Yıldız", "https://i.pravatar.cc/150?img=6", false, false, 15),
                FollowerUser("7", "ayşe_art", "Ayşe Korkmaz", "https://i.pravatar.cc/150?img=7", true, false, 7),
                FollowerUser("8", "can_music", "Can Demirtaş", "https://i.pravatar.cc/150?img=8", false, true, 2)
            )
        )
    }

    val filteredFollowers = followers.filter {
        it.username.contains(searchQuery, ignoreCase = true) ||
                it.fullName.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Takipçiler", fontWeight = FontWeight.Bold)
                        Text(
                            "${followers.size} takipçi",
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
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Takipçi ara...") },
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
                items(filteredFollowers) { user ->
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
                                            colors = listOf(Color(0xFF6C63FF), Color(0xFF9C27B0))
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
                                if (user.mutualFriends > 0) {
                                    Text(
                                        text = "${user.mutualFriends} ortak arkadaş",
                                        fontSize = 11.sp,
                                        color = Color(0xFF6C63FF)
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    followers = followers.map {
                                        if (it.id == user.id) it.copy(isFollowing = !it.isFollowing) else it
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (user.isFollowing)
                                        Color(0xFF6C63FF).copy(alpha = 0.1f) else Color(0xFF6C63FF)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(
                                    text = if (user.isFollowing) "Takip Ediliyor" else "Takip Et",
                                    fontSize = 13.sp,
                                    color = if (user.isFollowing) Color(0xFF6C63FF) else Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
