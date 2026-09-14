package com.hayidev.app.ui.screens.verification

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class VerificationStep(
    val id: String,
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    onBack: () -> Unit = {},
    onUploadSelfie: () -> Unit = {},
    onUploadID: () -> Unit = {},
    onSubmit: () -> Unit = {},
    onContactSupport: () -> Unit = {}
) {
    var verificationStatus by remember { mutableStateOf("pending") }
    var selectedDocType by remember { mutableIntStateOf(0) }
    var hasUploadedSelfie by remember { mutableStateOf(false) }
    var hasUploadedID by remember { mutableStateOf(false) }
    var agreedToTerms by remember { mutableStateOf(false) }

    val docTypes = listOf("Kimlik Kartı", "Ehliyet", "Pasaport", "İkametgah")

    val steps = listOf(
        VerificationStep("1", "Selfie Yükle", "Yüzünüz net görünecek şekilde fotoğraf çekin", Icons.Filled.Face, hasUploadedSelfie, !hasUploadedSelfie),
        VerificationStep("2", "Kimlik Yükle", "Resmi kimlik belgenizin fotoğrafını yükleyin", Icons.Filled.CreditCard, hasUploadedID, hasUploadedSelfie && !hasUploadedID),
        VerificationStep("3", "İnceleme", "Ekibimiz belgelerinizi inceleyecek", Icons.Filled.Search, verificationStatus == "approved", hasUploadedID && verificationStatus != "approved"),
        VerificationStep("4", "Onay", "Doğrulama tamamlandı!", Icons.Filled.Verified, verificationStatus == "approved", false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hesap Doğrulama", fontWeight = FontWeight.Bold) },
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
            // Status card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when (verificationStatus) {
                            "approved" -> Color(0xFF4CAF50)
                            "pending" -> Color(0xFFFF9800)
                            "rejected" -> Color(0xFFE53935)
                            else -> Color(0xFF6C63FF)
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = when (verificationStatus) {
                                "approved" -> Icons.Filled.Verified
                                "pending" -> Icons.Filled.Schedule
                                "rejected" -> Icons.Filled.Error
                                else -> Icons.Filled.Help
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = when (verificationStatus) {
                                "approved" -> "Doğrulandı!"
                                "pending" -> "İnceleniyor"
                                "rejected" -> "Reddedildi"
                                else -> "Doğrula"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = when (verificationStatus) {
                                "approved" -> "Hesabınız doğrulanmış durumda"
                                "pending" -> "Başvurunuz 24-48 saat içinde incelenecek"
                                "rejected" -> "Lütfen belgelerinizi yeniden yükleyin"
                                else -> "Hesabınızı doğrulayarak güvenliği artırın"
                            },
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Steps
            item {
                Text("Doğrulama Adımları", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(steps) { step ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (step.isCompleted)
                            Color(0xFF4CAF50).copy(alpha = 0.1f)
                        else if (step.isCurrent)
                            Color(0xFF6C63FF).copy(alpha = 0.1f)
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
                                    when {
                                        step.isCompleted -> Brush.linearGradient(listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)))
                                        step.isCurrent -> Brush.linearGradient(listOf(Color(0xFF6C63FF), Color(0xFF9C27B0)))
                                        else -> Brush.linearGradient(listOf(Color(0xFF9E9E9E), Color(0xFF616161)))
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (step.isCompleted) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    step.id,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(step.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                step.description,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (step.isCurrent) {
                            Icon(
                                Icons.Filled.ChevronRight,
                                contentDescription = null,
                                tint = Color(0xFF6C63FF)
                            )
                        }
                    }
                }
            }

            // Selfie upload
            if (!hasUploadedSelfie) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Selfie Yükleme", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Yüzünüz net görünecek, güneş gözlüğü ve şapka olmadan fotoğraf çekin",
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    hasUploadedSelfie = true
                                    onUploadSelfie()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Filled.CameraAlt, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Selfie Çek")
                            }
                        }
                    }
                }
            }

            // ID upload
            if (hasUploadedSelfie && !hasUploadedID) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Kimlik Yükleme", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Kimlik Türü", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                docTypes.forEachIndexed { index, type ->
                                    FilterChip(
                                        selected = selectedDocType == index,
                                        onClick = { selectedDocType = index },
                                        label = { Text(type, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFF6C63FF),
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    hasUploadedID = true
                                    onUploadID()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Filled.Upload, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Kimlik Yükle")
                            }
                        }
                    }
                }
            }

            // Terms agreement
            if (hasUploadedSelfie && hasUploadedID) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { agreedToTerms = !agreedToTerms }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreedToTerms,
                            onCheckedChange = { agreedToTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6C63FF))
                        )
                        Text(
                            "Doğrulama şartlarını ve gizlilik politikasını kabul ediyorum",
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = {
                            verificationStatus = "pending"
                            onSubmit()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = agreedToTerms,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Doğrulama Başvurusunu Gönder", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Info
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF).copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = null,
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Bilgi", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "• Belgeleriniz yalnızca doğrulama amacıyla kullanılacaktır\n" +
                                    "• İnceleme süreci 24-48 saat sürmektedir\n" +
                                    "• Doğrulama rozeti profilinize eklenecektir\n" +
                                    "• Destek için bizimle iletişime geçin",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                TextButton(
                    onClick = onContactSupport,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Destek ile İletişime Geç", color = Color(0xFF6C63FF))
                }
            }
        }
    }
}
