package com.hayidev.app.ui.screens.help

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FAQItem(
    val id: String,
    val question: String,
    val answer: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onBack: () -> Unit = {},
    onContactSupport: () -> Unit = {},
    onReportBug: () -> Unit = {},
    onFeatureRequest: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedId by remember { mutableStateOf<String?>(null) }

    val faqItems = listOf(
        FAQItem("1", "Hesap nasıl oluşturulur?", "Uygulamamızı indirin ve 'Kayıt Ol' butonuna tıklayın. E-posta adresiniz veya telefon numaranız ile hızlıca kayıt olabilirsiniz.", "Genel"),
        FAQItem("2", "Şifremi unuttum ne yapmalıyım?", "Giriş ekranında 'Şifremi Unuttum' bağlantısına tıklayın. E-posta adresinize sıfırlama bağlantısı gönderilecektir.", "Hesap"),
        FAQItem("3", "Premium üyelik nedir?", "Premium üyelik, sınırsız beğeni, gizli mod, kimin beğendiğini görme ve daha birçok özellik sunan ücretli üyelik türüdür.", "Premium"),
        FAQItem("4", "Hesabım nasıl doğrulanır?", "Profil ayarlarından 'Hesap Doğrulama' seçeneğine tıklayın ve gerekli belgeleri yükleyin.", "Hesap"),
        FAQItem("5", "Bir kullanıcıyı nasıl engellerim?", "Kullanıcının profil sayfasında三点 nokta menüsüne tıklayın ve 'Engelle' seçeneğini seçin.", "Güvenlik"),
        FAQItem("6", "Gönderim nasıl paylaşılır?", "Ana sayfadaki '+' butonuna tıklayın, fotoğraf veya video seçin ve 'Paylaş' butonuna tıklayın.", "Genel"),
        FAQItem("7", "Hikaye nasıl oluşturulur?", "Ana sayfada sağ üst köşedeki 'Hikaye' butonuna tıklayın. Fotoğraf çekin veya galeriden seçin.", "Genel"),
        FAQItem("8", "Mesajlarım gözüküyor mu?", "Hayır, özel mesajlarınız yalnızca sizin ve karşınızdaki kişinin görebileceği şekilde şifrelenmiştir.", "Güvenlik"),
        FAQItem("9", "Coin nedir ve nasıl kazanılır?", "Coin, uygulama içi para birimidir. Gönderi paylaşarak, başkalarını davet ederek ve görevleri tamamlayarak coin kazanabilirsiniz.", "Genel"),
        FAQItem("10", "Hesabımı nasıl sileririm?", "Profil > Ayarlar > Gizlilik > Hesabı Sil yolunu izleyin. Bu işlem geri alınamaz.", "Hesap")
    )

    val filteredFAQs = faqItems.filter {
        it.question.contains(searchQuery, ignoreCase = true) ||
                it.answer.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yardım Merkezi", fontWeight = FontWeight.Bold) },
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
            // Search
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Yardım ara...") },
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
            }

            // Quick actions
            item {
                Text("Hızlı İşlemler", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onContactSupport),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF).copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.Headset,
                                contentDescription = null,
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Destek", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onReportBug),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE53935).copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.BugReport,
                                contentDescription = null,
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Hata Bildir", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onFeatureRequest),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.Lightbulb,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Öneri", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // FAQ
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Sıkça Sorulan Sorular", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(filteredFAQs) { faq ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedId = if (expandedId == faq.id) null else faq.id
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Help,
                                contentDescription = null,
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                faq.question,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                if (expandedId == faq.id) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (expandedId == faq.id) {
                            HorizontalDivider()
                            Text(
                                text = faq.answer,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
