package com.hayidev.app.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "incoming_calls"
        private const val NOTIFICATION_ID = 2001
    }

    init {
        createChannel()
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Gelen Aramalar",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Gelen video ve sesli arama bildirimleri"
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 500, 0, 500)
            setBypassDnd(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showIncomingCallNotification(
        callerName: String,
        callerPhotoUrl: String,
        callId: String,
        isVideoCall: Boolean = true,
        callerId: String
    ) {
        val fullScreenIntent = Intent(context, Class.forName("com.hayidev.app.MainActivity")).apply {
            action = "INCOMING_CALL"
            putExtra("call_id", callId)
            putExtra("caller_id", callerId)
            putExtra("caller_name", callerName)
            putExtra("is_video_call", isVideoCall)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NO_HISTORY
        }

        val stackBuilder = TaskStackBuilder.create(context).apply {
            addParentStack(Class.forName("com.hayidev.app.MainActivity"))
            addNextIntent(fullScreenIntent)
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val answerIntent = Intent(context, Class.forName("com.hayidev.app.MainActivity")).apply {
            action = "ANSWER_CALL"
            putExtra("call_id", callId)
            putExtra("caller_id", callerId)
            putExtra("is_video_call", isVideoCall)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val answerPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID + 1,
            answerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val declineIntent = Intent(context, Class.forName("com.hayidev.app.MainActivity")).apply {
            action = "DECLINE_CALL"
            putExtra("call_id", callId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val declinePendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID + 2,
            declineIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val callType = if (isVideoCall) "Video" else "Sesli"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_call)
            .setContentTitle("$callType Arama")
            .setContentText("$callerName sizi arıyor...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setContentIntent(fullScreenPendingIntent)
            .setAutoCancel(false)
            .setOngoing(true)
            .setTimeoutAfter(30000)
            .addAction(
                android.R.drawable.ic_call_green,
                "Cevapla",
                answerPendingIntent
            )
            .addAction(
                android.R.drawable.ic_call_end,
                "Reddet",
                declinePendingIntent
            )
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun cancelCallNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
