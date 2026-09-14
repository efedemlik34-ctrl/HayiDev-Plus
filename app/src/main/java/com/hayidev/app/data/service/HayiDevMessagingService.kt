package com.hayidev.app.data.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HayiDevMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let { notification ->
            // Bildirim göster
            val title = notification.title ?: ""
            val body = notification.body ?: ""

            when {
                title.contains("mesaj", ignoreCase = true) -> {
                    // Mesaj bildirimi
                }
                title.contains("hediye", ignoreCase = true) -> {
                    // Hediye bildirimi
                }
                title.contains("canlı", ignoreCase = true) -> {
                    // Canlı yayın bildirimi
                }
                title.contains("eşleşme", ignoreCase = true) -> {
                    // Eşleşme bildirimi
                }
                else -> {
                    // Genel bildirim
                }
            }
        }

        // Data message işleme
        message.data.isNotEmpty().let {
            val type = message.data["type"] ?: ""
            val targetId = message.data["targetId"] ?: ""

            when (type) {
                "new_message" -> {
                    // Yeni mesaj
                }
                "new_match" -> {
                    // Yeni eşleşme
                }
                "gift_received" -> {
                    // Hediye alındı
                }
                "live_started" -> {
                    // Canlı yayın başladı
                }
                "video_call" -> {
                    // Video çağrı isteği
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Token'ı sunucuya gönder
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        // Firebase Firestore'a token kaydet
    }
}
