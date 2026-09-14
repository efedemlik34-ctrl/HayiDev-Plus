package com.hayidev.app.ui.screens.region

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Region(
    val id: String,
    val name: String,
    val nameEn: String,
    val flag: String,
    val countries: List<Country>
)

data class Country(
    val id: String,
    val name: String,
    val code: String,
    val flag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionSelectionScreen(
    onRegionSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedRegion by remember { mutableStateOf<String?>(null) }
    var selectedCountry by remember { mutableStateOf<String?>(null) }

    val regions = remember {
        listOf(
            Region("europe", "Avrupa", "Europe", "🇪🇺", listOf(
                Country("tr", "Türkiye", "TR", "🇹🇷"),
                Country("de", "Almanya", "DE", "🇩🇪"),
                Country("fr", "Fransa", "FR", "🇫🇷"),
                Country("gb", "İngiltere", "GB", "🇬🇧"),
                Country("es", "İspanya", "ES", "🇪🇸"),
                Country("it", "İtalya", "IT", "🇮🇹"),
                Country("nl", "Hollanda", "NL", "🇳🇱"),
                Country("pl", "Polonya", "PL", "🇵🇱"),
                Country("se", "İsveç", "SE", "🇸🇪"),
                Country("no", "Norveç", "NO", "🇳🇴"),
                Country("dk", "Danimarka", "DK", "🇩🇰"),
                Country("fi", "Finlandiya", "FI", "🇫🇮")
            )),
            Region("asia", "Asya", "Asia", "🌏", listOf(
                Country("jp", "Japonya", "JP", "🇯🇵"),
                Country("kr", "Güney Kore", "KR", "🇰🇷"),
                Country("cn", "Çin", "CN", "🇨🇳"),
                Country("th", "Tayland", "TH", "🇹🇭"),
                Country("vn", "Vietnam", "VN", "🇻🇳"),
                Country("id", "Endonezya", "ID", "🇮🇩"),
                Country("ph", "Filipinler", "PH", "🇵🇭"),
                Country("my", "Malezya", "MY", "🇲🇾"),
                Country("sg", "Singapur", "SG", "🇸🇬"),
                Country("in", "Hindistan", "IN", "🇮🇳")
            )),
            Region("middle_east", "Orta Doğu", "Middle East", "🌍", listOf(
                Country("ae", "Birleşik Arap Emirlikleri", "AE", "🇦🇪"),
                Country("sa", "Suudi Arabistan", "SA", "🇸🇦"),
                Country("qa", "Katar", "QA", "🇶🇦"),
                Country("kw", "Kuveyt", "KW", "🇰🇼"),
                Country("bh", "Bahreyn", "BH", "🇧🇭"),
                Country("om", "Umman", "OM", "🇴🇲"),
                Country("jo", "Ürdün", "JO", "🇯🇴"),
                Country("lb", "Lübnan", "LB", "🇱🇧")
            )),
            Region("north_america", "Kuzey Amerika", "North America", "🌎", listOf(
                Country("us", "Amerika Birleşik Devletleri", "US", "🇺🇸"),
                Country("ca", "Kanada", "CA", "🇨🇦"),
                Country("mx", "Meksika", "MX", "🇲🇽")
            )),
            Region("south_america", "Güney Amerika", "South America", "🌎", listOf(
                Country("br", "Brezilya", "BR", "🇧🇷"),
                Country("ar", "Arjantin", "AR", "🇦🇷"),
                Country("co", "Kolombiya", "CO", "🇨🇴"),
                Country("cl", "Şili", "CL", "🇨🇱"),
                Country("pe", "Peru", "PE", "🇵🇪")
            )),
            Region("africa", "Afrika", "Africa", "🌍", listOf(
                Country("za", "Güney Afrika", "ZA", "🇿🇦"),
                Country("ng", "Nijerya", "NG", "🇳🇬"),
                Country("eg", "Mısır", "EG", "🇪🇬"),
                Country("ke", "Kenya", "KE", "🇰🇪"),
                Country("ma", "Fas", "MA", "🇲🇦")
            )),
            Region("oceania", "Okyanusya", "Oceania", "🌊", listOf(
                Country("au", "Avustralya", "AU", "🇦🇺"),
                Country("nz", "Yeni Zelanda", "NZ", "🇳🇿")
            ))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Bölge Seç",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Geri")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            selectedCountry?.let { onRegionSelected(it) }
                        },
                        enabled = selectedCountry != null
                    ) {
                        Text("Onayla")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Warning Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Bölge seçimi yalnızca bir kez yapılabilir. Seçildikten sonra değiştirilemez.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Regions
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                regions.forEach { region ->
                    item {
                        Text(
                            text = "${region.flag} ${region.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(region.countries) { country ->
                        CountryItem(
                            country = country,
                            isSelected = selectedCountry == country.id,
                            onClick = {
                                selectedRegion = region.id
                                selectedCountry = country.id
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CountryItem(
    country: Country,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = country.flag,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = country.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Seçildi",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
