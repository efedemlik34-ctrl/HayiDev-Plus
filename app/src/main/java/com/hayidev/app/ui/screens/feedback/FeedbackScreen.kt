package com.hayidev.app.ui.screens.feedback

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    onBack: () -> Unit = {},
    onSubmitFeedback: (String, Int, String) -> Unit = {},
    onRateApp: (Int) -> Unit = {}
) {
    var feedbackType by remember { mutableIntStateOf(0) }
    var rating by remember { mutableIntStateOf(0) }
    var feedbackText by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var includeLogs by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    val feedbackTypes = listOf(
        "Hata Bildirimi",
        "Öneri",
        "Şikayet",
        "Övgü",
        "Diğer"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Geri Bildirim", fontWeight = FontWeight.Bold) },
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
            // Rating
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF).copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Uygulamamızı Nasıl Değerlendirirsiniz?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (i in 1..5) {
                                IconButton(
                                    onClick = {
                                        rating = i
                                        onRateApp(i)
                                    },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = "$i Yıldız",
                                        tint = if (i <= rating) Color(0xFFFFD700) else Color(0xFFBDBDBD),
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }
                        if (rating > 0) {
                            Text(
                                when (rating) {
                                    1 -> "Çok Kötü"
                                    2 -> "Kötü"
                                    3 -> "Orta"
                                    4 -> "İyi"
                                    5 -> "Mükemmel"
                                    else -> ""
                                },
                                fontSize = 14.sp,
                                color = Color(0xFF6C63FF),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Feedback type
            item {
                Text("Geri Bildirim Türü", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    feedbackTypes.forEachIndexed { index, type ->
                        FilterChip(
                            selected = feedbackType == index,
                            onClick = { feedbackType = index },
                            label = { Text(type, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF6C63FF),
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Feedback text
            item {
                Text("Mesajınız", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            item {
                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                    placeholder = { Text("Geri bildiriminizi buraya yazın...") },
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 10
                )
            }

            // Email
            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("E-posta (isteğe bağlı)") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Include logs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { includeLogs = !includeLogs }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includeLogs,
                        onCheckedChange = { includeLogs = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6C63FF))
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sistem loglarını dahil et", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Sorunları daha hızlı çözmemize yardımcı olur",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Submit
            item {
                Button(
                    onClick = {
                        isSubmitting = true
                        onSubmitFeedback(feedbackTypes[feedbackType], rating, feedbackText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = feedbackText.isNotBlank() && !isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(Icons.Filled.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gönder", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            // Info
            item {
                Text(
                    text = "Geri bildiriminiz bizim için çok değerli. İyileştirmeler yapmamıza yardımcı oluyorsunuz!",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
