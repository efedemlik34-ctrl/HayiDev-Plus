package com.hayidev.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.SystemClock
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class HayiDevApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startTime = SystemClock.elapsedRealtime()
        
        FirebaseApp.initializeApp(this)
        createNotificationChannels()
        setupFirebaseMessaging()
        setupLanguage()
    }

    private fun setupLanguage() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString(KEY_LANGUAGE, null)
        
        val locale = if (!savedLanguage.isNullOrEmpty()) {
            Locale(savedLanguage)
        } else {
            val deviceLocale = resources.configuration.locales[0]
            deviceLocale
        }
        
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
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

            val gameChannel = NotificationChannel(
                CHANNEL_GAMES,
                "Oyunlar",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Oyun bildirimleri"
            }

            manager.createNotificationChannel(messageChannel)
            manager.createNotificationChannel(liveChannel)
            manager.createNotificationChannel(giftChannel)
            manager.createNotificationChannel(gameChannel)
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

    fun getHeapInfo(): String {
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        val totalMemory = runtime.totalMemory() / (1024 * 1024)
        val maxMemory = runtime.maxMemory() / (1024 * 1024)
        return "Heap: ${usedMemory}MB used / ${totalMemory}MB total / ${maxMemory}MB max"
    }

    companion object {
        const val CHANNEL_MESSAGES = "hayidev_messages"
        const val CHANNEL_LIVE = "hayidev_live"
        const val CHANNEL_GIFTS = "hayidev_gifts"
        const val CHANNEL_GAMES = "hayidev_games"
        
        const val PREFS_NAME = "hayidev_prefs"
        const val KEY_LANGUAGE = "language"
        
        var instance: HayiDevApplication? = null
            private set
        
        var startTime: Long = 0L
            private set
    }
}
