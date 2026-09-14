package com.hayidev.app.ui.screens.live

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLiveRoomScreen(
    onBackClick: () -> Unit,
    onRoomCreated: (String) -> Unit
) {
    var roomName by remember { mutableStateOf("") }
    var roomDescription by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("public") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yayın Oluştur") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Yayın Başlığı",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = roomName,
                onValueChange = { roomName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Örn: Sohbet Zamanı 💬") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Açıklama (isteğe bağlı)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = roomDescription,
                onValueChange = { roomDescription = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Yayın hakkında bir şeyler yazın...") },
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Yayın Türü",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedType == "public",
                    onClick = { selectedType = "public" },
                    label = { Text("Herkese Açık") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedType == "followers",
                    onClick = { selectedType = "followers" },
                    label = { Text("Takipçiler") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedType == "private",
                    onClick = { selectedType = "private" },
                    label = { Text("Özel") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val roomId = "room_${System.currentTimeMillis()}"
                    onRoomCreated(roomId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = roomName.isNotBlank()
            ) {
                Text(
                    text = "Yayını Başlat",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
