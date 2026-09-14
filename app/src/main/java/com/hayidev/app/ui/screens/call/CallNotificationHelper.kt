package com.hayidev.app.ui.screens.call

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.hayidev.app.HayiDevApplication
import com.hayidev.app.MainActivity

object CallNotificationHelper {

    fun showIncomingCallNotification(
        context: Context,
        callerId: String,
        callerName: String,
        callerPhotoUrl: String,
        isVideoCall: Boolean
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // PendingIntent for answering the call
        val answerIntent = Intent(context, MainActivity::class.java).apply {
            action = "ANSWER_CALL"
            putExtra("callerId", callerId)
            putExtra("isVideoCall", isVideoCall)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val answerPendingIntent = PendingIntent.getActivity(
            context,
            callerId.hashCode(),
            answerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // PendingIntent for declining the call
        val declineIntent = Intent(context, MainActivity::class.java).apply {
            action = "DECLINE_CALL"
            putExtra("callerId", callerId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val declinePendingIntent = PendingIntent.getActivity(
            context,
            callerId.hashCode() + 1,
            declineIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, HayiDevApplication.CHANNEL_MESSAGES)
            .setSmallIcon(android.R.drawable.ic_call)
            .setContentTitle(if (isVideoCall) "Video Görüşme" else "Sesli Görüşme")
            .setContentText("$callerName arıyor...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(answerPendingIntent, true)
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_call_accept,
                "Cevapla",
                answerPendingIntent
            )
            .addAction(
                android.R.drawable.ic_call_end,
                "Reddet",
                declinePendingIntent
            )
            .build()

        notificationManager.notify(callerId.hashCode(), notification)
    }

    fun showMissedCallNotification(
        context: Context,
        callerId: String,
        callerName: String
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, HayiDevApplication.CHANNEL_MESSAGES)
            .setSmallIcon(android.R.drawable.ic_call_missed)
            .setContentTitle("Cevapsız Arama")
            .setContentText("$callerName seni aradı")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(callerId.hashCode() + 2, notification)
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    fun cancelAllNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
}
