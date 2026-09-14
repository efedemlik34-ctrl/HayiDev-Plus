package com.hayidev.app.ui.screens.live

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.hayidev.app.HayiDevApplication

class LiveForegroundService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra(ACTION_KEY)

        when (action) {
            ACTION_START -> startForegroundNotification()
            ACTION_STOP -> stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun startForegroundNotification() {
        val notification = NotificationCompat.Builder(this, HayiDevApplication.CHANNEL_LIVE)
            .setContentTitle("Canlı Yayın")
            .setContentText("Canlı yayındasınız")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    companion object {
        const val ACTION_KEY = "action"
        const val ACTION_START = "start"
        const val ACTION_STOP = "stop"
        const val NOTIFICATION_ID = 1001
    }
}
