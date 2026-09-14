package com.hayidev.app.ui.screens.premium

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PremiumPlan(
    val name: String,
    val price: String,
    val originalPrice: String? = null,
    val discount: String? = null,
    val features: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
    onBack: () -> Unit = {},
    onPurchase: (String) -> Unit = {},
    onRestorePurchases: () -> Unit = {}
) {
    var selectedPlan by remember { mutableIntStateOf(1) }
    var selectedPaymentMethod by remember { mutableIntStateOf(0) }

    val plans = listOf(
        PremiumPlan(
            name = "Aylık",
            price = "₺49.99/ay",
            features = listAylıkFeatures()
        ),
        PremiumPlan(
            name = "Yıllık",
            price = "₺299.99/yıl",
            originalPrice = "₺599.98",
            discount = "%50 Tasarruf",
            features = listYillikFeatures()
        ),
        PremiumPlan(
            name = "Ömür Boyu",
            price = "₺499.99",
            features = listOmurBoyFeatures()
        )
    )

    val benefits = listOf(
        Triple(Icons.Filled.FavoriteBorder, "Sınırsız Beğeni", "Beş dakikada bir beğeni hakkınız olsun"),
        Triple(Icons.Filled.Visibility, "Kim Beğendiğini Gör", "Sizi beğenen tüm kullanıcıları görün"),
        Triple(Icons.Filled.VisibilityOff, "Gizli Mod", "Profilinizi gizleyerek keşfedin"),
        Triple(Icons.Filled.Undo, "Geri Al", "Yanlış swipe'larınızı geri alın"),
        Triple(Icons.Filled.MonetizationOn, "500 Coin/Ay", "Her ay 500 coin hediye"),
        Triple(Icons.Filled.Headset, "Öncelikli Destek", "7/24 öncelikli müşteri desteği")
    )

    val paymentMethods = listOf(
        Pair("Kredi Kartı", Icons.Filled.CreditCard),
        Pair("Apple Pay", Icons.Filled.PhoneIphone),
        Pair("Google Pay", Icons.Filled.AccountBalanceWallet),
        Pair("Banka Havalesi", Icons.Filled.AccountBalance)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HayiDev++ Premium", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF6C63FF), Color(0xFF9C27B0))
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Diamond,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Premium Üye Ol",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Tüm özelliklerin kilidini açın",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Premium Avantajları",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(benefits) { (icon, title, description) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF6C63FF).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Text(
                                text = description,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Plan Seçin",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(plans.indices.toList()) { index ->
                val plan = plans[index]
                val isSelected = selectedPlan == index

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPlan = index }
                        .then(
                            if (isSelected) Modifier.border(
                                2.dp,
                                Color(0xFF6C63FF),
                                RoundedCornerShape(16.dp)
                            ) else Modifier
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            Color(0xFF6C63FF).copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        if (plan.discount != null) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .background(
                                        Color(0xFFFF5722),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = plan.discount,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedPlan = index },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6C63FF))
                                )
                                Text(text = plan.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }

                            Row(
                                modifier = Modifier.padding(start = 48.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = plan.price,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6C63FF)
                                )
                                if (plan.originalPrice != null) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = plan.originalPrice,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Ödeme Yöntemi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(paymentMethods.indices.toList()) { index ->
                val method = paymentMethods[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPaymentMethod = index }
                        .then(
                            if (selectedPaymentMethod == index) Modifier.border(
                                2.dp,
                                Color(0xFF6C63FF),
                                RoundedCornerShape(12.dp)
                            ) else Modifier
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedPaymentMethod == index)
                            Color(0xFF6C63FF).copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = index }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = method.second,
                            contentDescription = null,
                            tint = if (selectedPaymentMethod == index) Color(0xFF6C63FF)
                            else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = method.first, modifier = Modifier.weight(1f))
                        RadioButton(
                            selected = selectedPaymentMethod == index,
                            onClick = { selectedPaymentMethod = index },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6C63FF))
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { onPurchase(plans[selectedPlan].name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Diamond, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Premium'a Geç - ${plans[selectedPlan].price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            item {
                TextButton(
                    onClick = onRestorePurchases,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Satın Alımları Geri Yükle", color = Color(0xFF6C63FF))
                }
            }

            item {
                Text(
                    text = "Satın alarak Hizmet Şartlarımızı ve Gizlilik Politikamızı kabul etmiş olursunuz. Premium üyeliğiniz otomatik olarak yenilenir.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

private fun listAylıkFeatures() = listOf("Sınırsız Beğeni", "Kim Beğendiğini Gör", "Gizli Mod")

private fun listYillikFeatures() = listOf("Sınırsız Beğeni", "Kim Beğendiğini Gör", "Gizli Mod", "500 Coin/Ay")

private fun listOmurBoyFeatures() = listOf("Sınırsız Beğeni", "Kim Beğendiğini Gör", "Gizli Mod", "500 Coin/Ay", "Öncelikli Destek")
