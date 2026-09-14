package com.hayidev.app.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_MESSAGES = "messages"
        const val CHANNEL_GIFTS = "gifts"
        const val CHANNEL_LIVE = "live"
        const val CHANNEL_CALLS = "calls"
        const val CHANNEL_GENERAL = "general"
    }

    init {
        createChannels()
    }

    private fun createChannels() {
        val channels = listOf(
            NotificationChannel(
                CHANNEL_MESSAGES,
                "Mesajlar",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Yeni mesaj bildirimleri"
                enableVibration(true)
            },
            NotificationChannel(
                CHANNEL_GIFTS,
                "Hediyeler",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Hediye bildirimleri"
            },
            NotificationChannel(
                CHANNEL_LIVE,
                "Canlı Yayın",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canlı yayın bildirimleri"
            },
            NotificationChannel(
                CHANNEL_CALLS,
                "Aramalar",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Arama bildirimleri"
                enableVibration(true)
            },
            NotificationChannel(
                CHANNEL_GENERAL,
                "Genel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Genel bildirimler"
            }
        )

        notificationManager.createNotificationChannels(channels)
    }

    fun showNotification(
        title: String,
        message: String,
        channelId: String = CHANNEL_GENERAL,
        notificationId: Int = System.currentTimeMillis().toInt(),
        intent: Intent? = null,
        isOngoing: Boolean = false
    ) {
        val pendingIntent = intent?.let {
            PendingIntent.getActivity(
                context,
                0,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(isOngoing)
            .apply {
                pendingIntent?.let { setContentIntent(it) }
            }
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun showMessageNotification(
        senderName: String,
        message: String,
        senderId: String,
        chatId: String
    ) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.putExtra("navigate_to", "chat")
        intent?.putExtra("chat_id", chatId)
        intent?.putExtra("sender_id", senderId)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        showNotification(
            title = senderName,
            message = message,
            channelId = CHANNEL_MESSAGES,
            intent = intent
        )
    }

    fun showGiftNotification(
        senderName: String,
        giftName: String,
        giftIcon: String,
        senderId: String
    ) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.putExtra("navigate_to", "gift")
        intent?.putExtra("sender_id", senderId)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        showNotification(
            title = "Hediye Aldınız!",
            message = "$senderName size $giftIcon $giftName gönderdi",
            channelId = CHANNEL_GIFTS,
            intent = intent
        )
    }

    fun showLiveNotification(
        hostName: String,
        roomId: String,
        title: String
    ) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.putExtra("navigate_to", "live")
        intent?.putExtra("room_id", roomId)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        showNotification(
            title = "$hostName canlı yayına başladı!",
            message = title,
            channelId = CHANNEL_LIVE,
            intent = intent
        )
    }

    fun showMatchNotification(
        matchedUserName: String,
        matchedUserId: String
    ) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.putExtra("navigate_to", "chat")
        intent?.putExtra("matched_user_id", matchedUserId)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        showNotification(
            title = "Yeni Eşleşme!",
            message = "$matchedUserName ile eşleştiniz!",
            channelId = CHANNEL_GENERAL,
            intent = intent
        )
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }
}
