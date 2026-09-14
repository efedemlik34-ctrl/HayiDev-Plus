package com.hayidev.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HayiDevApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        createNotificationChannels()
        setupFirebaseMessaging()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)

            val messageChannel = NotificationChannel(
                CHANNEL_MESSAGES,
                "Mesajlar",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Yeni mesaj bildirimleri"
                enableVibration(true)
            }

            val liveChannel = NotificationChannel(
                CHANNEL_LIVE,
                "Canlı Yayın",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canlı yayın bildirimleri"
            }

            val giftChannel = NotificationChannel(
                CHANNEL_GIFTS,
                "Hediyeler",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Hediye bildirimleri"
            }

            manager.createNotificationChannel(messageChannel)
            manager.createNotificationChannel(liveChannel)
            manager.createNotificationChannel(giftChannel)
        }
    }

    private fun setupFirebaseMessaging() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                // Token'ı sunucuya gönder
            }
        }
    }

    companion object {
        const val CHANNEL_MESSAGES = "hayidev_messages"
        const val CHANNEL_LIVE = "hayidev_live"
        const val CHANNEL_GIFTS = "hayidev_gifts"
    }
}
