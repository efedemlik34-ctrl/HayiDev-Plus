package com.hayidev.app.ui.components

import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.hayidev.app.ui.theme.Error
import com.hayidev.app.ui.theme.PrimaryLight
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecorder(
    onRecordingComplete: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val audioPermission = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    var isRecording by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableIntStateOf(0) }
    var isCancelled by remember { mutableStateOf(false) }
    var swipeOffset by remember { mutableFloatStateOf(0f) }

    val recorder = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    val audioFile = remember {
        File(
            context.cacheDir,
            "voice_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.mp4"
        )
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (recordingDuration < 60) {
                kotlinx.coroutines.delay(1000)
                recordingDuration++
            }
            // Maksimum süreye ulaşıldı
            stopRecording(recorder, audioFile)
            onRecordingComplete(audioFile.absolutePath)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (isRecording) {
                recorder.release()
            }
        }
    }

    if (!audioPermission.status.isGranted) {
        LaunchedEffect(Unit) {
            audioPermission.launchPermissionRequest()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (swipeOffset < -100) {
                            // Sola kaydır - iptal
                            isCancelled = true
                            stopRecording(recorder, audioFile)
                            onCancel()
                        } else if (swipeOffset > 100) {
                            // Sağa kaydır - gönder
                            stopRecording(recorder, audioFile)
                            onRecordingComplete(audioFile.absolutePath)
                        } else {
                            // Normal bitir
                            stopRecording(recorder, audioFile)
                            onRecordingComplete(audioFile.absolutePath)
                        }
                        isRecording = false
                        recordingDuration = 0
                        swipeOffset = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        swipeOffset += dragAmount
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (isRecording) {
            // Recording UI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel indicator
                AnimatedVisibility(visible = swipeOffset < -50) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = null,
                            tint = Error,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "İptal",
                            color = Error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                // Recording indicator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                animateColorAsState(
                                    if (recordingDuration > 0) Error else PrimaryLight,
                                    label = "recording_color"
                                ).value
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Mic,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatDuration(recordingDuration),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Sola kaydır = İptal, Bırak = Gönder",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                // Send indicator
                AnimatedVisibility(visible = swipeOffset > 50) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.Send,
                            contentDescription = null,
                            tint = PrimaryLight,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Gönder",
                            color = PrimaryLight,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        } else {
            // Start recording button
            IconButton(
                onClick = {
                    startRecording(recorder, audioFile)
                    isRecording = true
                },
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    Icons.Filled.Mic,
                    contentDescription = "Ses Kaydı Başlat",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

private fun startRecording(recorder: MediaRecorder, outputFile: File) {
    recorder.apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setAudioSamplingRate(44100)
        setAudioEncodingBitRate(128000)
        setOutputFile(outputFile.absolutePath)
        prepare()
        start()
    }
}

private fun stopRecording(recorder: MediaRecorder, outputFile: File) {
    try {
        recorder.stop()
    } catch (e: Exception) {
        // Kayıt başlatılmamış olabilir
    }
    recorder.release()
}

private fun formatDuration(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
