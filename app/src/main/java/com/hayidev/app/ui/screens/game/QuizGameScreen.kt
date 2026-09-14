package com.hayidev.app.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.hayidev.app.ui.theme.*
import kotlin.random.Random

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizGameScreen(
    onBackClick: () -> Unit
) {
    var coins by remember { mutableIntStateOf(2450) }
    var betAmount by remember { mutableIntStateOf(50) }
    var currentQuestion by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var isAnswered by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var winAmount by remember { mutableIntStateOf(0) }
    var gameStarted by remember { mutableStateOf(false) }
    var timer by remember { mutableIntStateOf(15) }

    val questions = remember {
        listOf(
            QuizQuestion("Türkiye'nin başkenti neresidir?", listOf("İstanbul", "Ankara", "İzmir", "Bursa"), 1, "Coğrafya"),
            QuizQuestion("Güneş sisteminde kaç gezegen vardır?", listOf("7", "8", "9", "10"), 1, "Bilim"),
            QuizQuestion("Python hangi yıl oluşturulmuştur?", listOf("1989", "1991", "1995", "2000"), 1, "Teknoloji"),
            QuizQuestion("Dünya'nın en uzun nehri hangisidir?", listOf("Amazon", "Nil", "Mississippi", "Yangtze"), 1, "Coğrafya"),
            QuizQuestion("1 litre su kaç kilogramdır?", listOf("0.5", "1", "1.5", "2"), 1, "Bilim"),
            QuizQuestion("Real Madrid hangi ülkededir?", listOf("İtalya", "İspanya", "Portekiz", "Almanya"), 1, "Spor"),
            QuizQuestion("Ay hangi gezegenin etrafında döner?", listOf("Güneş", "Mars", "Dünya", "Venüs"), 2, "Bilim"),
            QuizQuestion("İstanbul Boğazı hangi iki denizi birleştirir?", listOf("Ege-Akdeniz", "Karadeniz-Marmara", "Marmara-Ege", "Akdeniz-Karadeniz"), 1, "Coğrafya"),
            QuizQuestion("Bir yılda kaç gün vardır?", listOf("360", "364", "365", "366"), 2, "Genel"),
            QuizQuestion("DNA'nın açılımı nedir?", listOf("Deoksiribo Nükleik Asit", "Dinamik Nükleer Asit", "Deo Nükleer Asit", "Dioksit Nükleik Asit"), 0, "Bilim"),
            QuizQuestion("Mars gezegeni neden kızıldır?", listOf("Volkanik", "Demir oksit", "Güneş ışığı", "Atmosfer"), 1, "Bilim"),
            QuizQuestion("İlk iPhone hangi yıl çıktı?", listOf("2005", "2006", "2007", "2008"), 2, "Teknoloji"),
            QuizQuestion("Venedik hangi ülkededir?", listOf("İspanya", "İtalya", "Yunanistan", "Portekiz"), 1, "Coğrafya"),
            QuizQuestion("Futbolda bir takım kaç kişiden oluşur?", listOf("9", "10", "11", "12"), 2, "Spor"),
            QuizQuestion("Su kaynarken kaç derecedir?", listOf("90", "95", "100", "110"), 2, "Bilim"),
            QuizQuestion("Apple'ın kurucusu kimdir?", listOf("Bill Gates", "Steve Jobs", "Mark Zuckerberg", "Jeff Bezos"), 1, "Teknoloji"),
            QuizQuestion("Mount Everest hangi ülkededir?", listOf("Çin", "Hindistan", "Nepal", "Tibet"), 2, "Coğrafya"),
            QuizQuestion("Bir üçgenin iç açıları toplamı kaçtır?", listOf("90", "180", "270", "360"), 1, "Matematik"),
            QuizQuestion("Güneş ışığı Dünya'ya kaç dakikada ulaşır?", listOf("4", "6", "8", "10"), 2, "Bilim"),
            QuizQuestion("Basketbolda bir çember kaç puan?", listOf("1", "2", "3", "4"), 1, "Spor")
        ).shuffled()
    }

    LaunchedEffect(gameStarted, currentQuestion, isAnswered) {
        if (gameStarted && !isAnswered && currentQuestion < questions.size) {
            timer = 15
            while (timer > 0 && !isAnswered) {
                kotlinx.coroutines.delay(1000)
                timer--
                if (timer == 0 && !isAnswered) {
                    isAnswered = true
                    streak = 0
                    kotlinx.coroutines.delay(1500)
                    if (currentQuestion < questions.size - 1) {
                        currentQuestion++
                        selectedAnswer = -1
                        isAnswered = false
                    } else {
                        winAmount = score * betAmount / 10
                        coins += winAmount
                        showResult = true
                        gameStarted = false
                    }
                }
            }
        }
    }

    fun startGame() {
        if (coins < betAmount) return
        coins -= betAmount
        currentQuestion = 0
        score = 0
        streak = 0
        selectedAnswer = -1
        isAnswered = false
        gameStarted = true
    }

    fun selectAnswer(index: Int) {
        if (isAnswered || selectedAnswer >= 0) return
        selectedAnswer = index
        isAnswered = true

        if (index == questions[currentQuestion].correctIndex) {
            streak++
            score += when (streak) {
                in 1..3 -> 10
                in 4..6 -> 20
                in 7..9 -> 30
                else -> 50
            }
        } else {
            streak = 0
        }

        kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
            kotlinx.coroutines.delay(1500)
            if (currentQuestion < questions.size - 1) {
                currentQuestion++
                selectedAnswer = -1
                isAnswered = false
            } else {
                winAmount = score * betAmount / 10
                coins += winAmount
                showResult = true
                gameStarted = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🧠 Bilgi Yarışması", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Filled.Close, "Kapat") }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .background(GoldCoin.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙 $coins", color = GoldCoin, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(Color(0xFF0d0d2b), Color(0xFF1a1a3e), Color(0xFF0d0d2b)))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!gameStarted) {
                // Start Screen
                Spacer(modifier = Modifier.height(48.dp))
                Text("🧠", fontSize = 72.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Bilgini Test Et!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("15 saniye süren var, streak bonusları kazan!", color = Color.White.copy(alpha = 0.6f))

                Spacer(modifier = Modifier.height(32.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf(25, 50, 100, 200)) { bet ->
                        FilterChip(
                            selected = betAmount == bet,
                            onClick = { betAmount = bet },
                            label = { Text("$bet") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentPurple.copy(alpha = 0.3f),
                                selectedLabelColor = AccentPurple
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { startGame() },
                    modifier = Modifier.width(200.dp).height(50.dp),
                    enabled = coins >= betAmount,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("🎯 BAŞLA ($betAmount)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                // Quiz in progress
                Spacer(modifier = Modifier.height(8.dp))

                // Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Soru ${currentQuestion + 1}/20", color = Color.White, fontSize = 14.sp)
                    Text("⭐ $score", color = GoldCoin, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("🔥 x$streak", color = if (streak >= 3) Color(0xFFFF6B00) else Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Timer
                LinearProgressIndicator(
                    progress = { timer / 15f },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = when {
                        timer > 10 -> Success
                        timer > 5 -> Warning
                        else -> Error
                    },
                    trackColor = Color.White.copy(alpha = 0.1f)
                )
                Text("${timer}s", color = when {
                    timer > 10 -> Success
                    timer > 5 -> Warning
                    else -> Error
                }, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Category
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AccentPurple.copy(alpha = 0.2f)
                ) {
                    Text(
                        questions[currentQuestion].category,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = AccentPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Question
                Card(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Text(
                        questions[currentQuestion].question,
                        modifier = Modifier.padding(20.dp),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Options
                questions[currentQuestion].options.forEachIndexed { index, option ->
                    val isSelected = selectedAnswer == index
                    val isCorrect = index == questions[currentQuestion].correctIndex
                    val showCorrect = isAnswered && isCorrect
                    val showWrong = isAnswered && isSelected && !isCorrect

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .padding(vertical = 4.dp)
                            .clickable { selectAnswer(index) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                showCorrect -> Success.copy(alpha = 0.3f)
                                showWrong -> Error.copy(alpha = 0.3f)
                                isSelected -> AccentPurple.copy(alpha = 0.3f)
                                else -> Color.White.copy(alpha = 0.05f)
                            }
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
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            showCorrect -> Success
                                            showWrong -> Error
                                            else -> Color.White.copy(alpha = 0.1f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    when {
                                        showCorrect -> "✓"
                                        showWrong -> "✗"
                                        else -> "${index + 1}"
                                    },
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(option, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            containerColor = Color(0xFF1e1e3f),
            title = { Text("🏆 Yarışma Bitti!", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Puan: $score", color = Color.White, fontSize = 18.sp)
                    Text("En İyi Streak: x$streak", color = AccentPurple, fontSize = 14.sp)
                    if (winAmount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("+$winAmount Coin", color = GoldCoin, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showResult = false }, colors = ButtonDefaults.buttonColors(containerColor = GoldCoin), shape = RoundedCornerShape(12.dp)) {
                    Text("Devam", color = Color.Black)
                }
            }
        )
    }
}
