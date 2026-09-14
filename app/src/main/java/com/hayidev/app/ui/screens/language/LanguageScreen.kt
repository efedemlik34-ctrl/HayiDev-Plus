package com.hayidev.app.ui.screens.language

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    onBack: () -> Unit = {},
    onLanguageSelected: (String) -> Unit = {}
) {
    var selectedLanguage by remember { mutableStateOf("tr") }
    var searchQuery by remember { mutableStateOf("") }

    val languages = listOf(
        Language("tr", "Türkçe", "Türkçe", "🇹🇷"),
        Language("en", "İngilizce", "English", "🇬🇧"),
        Language("de", "Almanca", "Deutsch", "🇩🇪"),
        Language("fr", "Fransızca", "Français", "🇫🇷"),
        Language("es", "İspanyolca", "Español", "🇪🇸"),
        Language("it", "İtalyanca", "Italiano", "🇮🇹"),
        Language("pt", "Portekizce", "Português", "🇵🇹"),
        Language("ru", "Rusça", "Русский", "🇷🇺"),
        Language("ar", "Arapça", "العربية", "🇸🇦"),
        Language("fa", "Farsça", "فارسی", "🇮🇷"),
        Language("hi", "Hintçe", "हिन्दी", "🇮🇳"),
        Language("ur", "Urduca", "اردو", "🇵🇰"),
        Language("zh", "Çince", "中文", "🇨🇳"),
        Language("ja", "Japonca", "日本語", "🇯🇵"),
        Language("ko", "Korece", "한국어", "🇰🇷"),
        Language("nl", "Felemenkçe", "Nederlands", "🇳🇱"),
        Language("pl", "Lehçe", "Polski", "🇵🇱"),
        Language("sv", "İsveççe", "Svenska", "🇸🇪"),
        Language("no", "Norveççe", "Norsk", "🇳🇴"),
        Language("da", "Danca", "Dansk", "🇩🇰"),
        Language("fi", "Fince", "Suomi", "🇫🇮"),
        Language("el", "Yunanca", "Ελληνικά", "🇬🇷"),
        Language("cs", "Çekçe", "Čeština", "🇨🇿"),
        Language("sk", "Slovakça", "Slovenčina", "🇸🇰"),
        Language("hu", "Macarca", "Magyar", "🇭🇺"),
        Language("ro", "Rumence", "Română", "🇷🇴"),
        Language("bg", "Bulgarca", "Български", "🇧🇬"),
        Language("hr", "Hırvatça", "Hrvatski", "🇭🇷"),
        Language("sr", "Sırpça", "Српски", "🇷🇸"),
        Language("uk", "Ukraynaca", "Українська", "🇺🇦"),
        Language("th", "Tayca", "ไทย", "🇹🇭"),
        Language("vi", "Vietnamca", "Tiếng Việt", "🇻🇳"),
        Language("id", "Endonezce", "Bahasa Indonesia", "🇮🇩"),
        Language("ms", "Malayca", "Bahasa Melayu", "🇲🇾"),
        Language("tl", "Filipince", "Filipino", "🇵🇭"),
        Language("bn", "Bengalce", "বাংলা", "🇧🇩"),
        Language("ta", "Tamilce", "தமிழ்", "🇮🇳"),
        Language("te", "Teluguca", "తెలుగు", "🇮🇳"),
        Language("ml", "Malayalamca", "മലയാളം", "🇮🇳"),
        Language("kn", "Kannadaca", "ಕನ್ನಡ", "🇮🇳"),
        Language("gu", "Gujaratça", "ગુજરાતી", "🇮🇳"),
        Language("mr", "Marathi", "मराठी", "🇮🇳"),
        Language("pa", "Pencapça", "ਪੰਜਾਬੀ", "🇮🇳"),
        Language("sw", "Svahili", "Kiswahili", "🇰🇪"),
        Language("am", "Amharca", "አማርኛ", "🇪🇹"),
        Language("he", "İbranice", "עברית", "🇮🇱"),
        Language("yi", "Yidiş", "ייִדיש", "🇮🇱"),
        Language("et", "Estonca", "Eesti", "🇪🇪"),
        Language("lv", "Letonca", "Latviešu", "🇱🇻"),
        Language("lt", "Litvanca", "Lietuvių", "🇱🇹"),
        Language("sl", "Slovence", "Slovenščina", "🇸🇮"),
        Language("bs", "Boşnakça", "Bosanski", "🇧🇦"),
        Language("sq", "Arnavutça", "Shqip", "🇦🇱"),
        Language("mk", "Makedonca", "Македонски", "🇲🇰"),
        Language("ka", "Gürcüce", "ქართული", "🇬🇪"),
        Language("hy", "Ermenice", "Հայերեն", "🇦🇲"),
        Language("az", "Azerice", "Azərbaycan", "🇦🇿"),
        Language("uz", "Özbekçe", "O'zbek", "🇺🇿"),
        Language("kk", "Kazakça", "Қазақ", "🇰🇿"),
        Language("ky", "Kırgızca", "Кыргыз", "🇰🇬"),
        Language("mn", "Moğolca", "Монгол", "🇲🇳"),
        Language("ne", "Nepalce", "नेपाली", "🇳🇵"),
        Language("si", "Sinhala", "සිංහල", "🇱🇰"),
        Language("my", "Birmanca", "မြန်မာ", "🇲🇲"),
        Language("km", "Kmerce", "ខ្មែរ", "🇰🇭"),
        Language("lo", "Laoce", "ລາວ", "🇱🇦"),
        Language("ka-ge", "Gürcüce (Gürcistan)", "ქართული", "🇬🇪"),
        Language("eu", "Baskça", "Euskara", "🇪🇸"),
        Language("ca", "Katalanca", "Català", "🇪🇸"),
        Language("gl", "Galiçyaca", "Galego", "🇪🇸"),
        Language("cy", "Galce", "Cymru", "🇬🇧"),
        Language("ga", "İrlandaca", "Gaeilge", "🇮🇪"),
        Language("mt", "Maltaca", "Malti", "🇲🇹"),
        Language("is", "İzlandaca", "Íslenska", "🇮🇸"),
        Language("lb", "Lüksemburgca", "Lëtzebuergesch", "🇱🇺"),
        Language("af", "Afrikanca", "Afrikaans", "🇿🇦"),
        Language("zu", "Zuluca", "isiZulu", "🇿🇦"),
        Language("yo", "Yorubaca", "Yorùbá", "🇳🇬"),
        Language("ig", "İgboaca", "Igbo", "🇳🇬"),
        Language("ha", "Hausaca", "Hausa", "🇳🇬"),
        Language("so", "Somalice", "Soomaali", "🇸🇴"),
        Language("mg", "Malgaşça", "Malagasy", "🇲🇬"),
        Language("rw", "Kinyarwandaca", "Ikinyarwanda", "🇷🇼"),
        Language("sn", "Shonaca", "chiShona", "🇿🇼"),
        Language("st", "Sesothoca", "Sesotho", "🇱🇸"),
        Language("tn", "Tswanaca", "Setswana", "🇧🇼"),
        Language("ss", "Svatıca", "siSwati", "🇸🇿"),
        Language("ve", "Vendaca", "Tshivenḓa", "🇿🇦"),
        Language("ts", "Tsongaca", "Xitsonga", "🇿🇦")
    )

    val filteredLanguages = languages.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.nativeName.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dil Seçimi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
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
                placeholder = { Text("Dil ara...") },
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
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredLanguages) { language ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedLanguage = language.code
                                onLanguageSelected(language.code)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedLanguage == language.code)
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
                            Text(language.flag, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(language.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                Text(
                                    language.nativeName,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (selectedLanguage == language.code) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = "Seçili",
                                    tint = Color(0xFF6C63FF),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
