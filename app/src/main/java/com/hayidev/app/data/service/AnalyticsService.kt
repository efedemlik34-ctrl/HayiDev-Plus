package com.hayidev.app.data.service

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsService @Inject constructor() {

    private var analytics: FirebaseAnalytics = Firebase.analytics

    fun logEvent(name: String, params: Bundle? = null) {
        analytics.logEvent(name, params)
    }

    fun logLogin(method: String) {
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(FirebaseAnalytics.Param.METHOD, method)
        }
    }

    fun logSignUp(method: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
            param(FirebaseAnalytics.Param.METHOD, method)
        }
    }

    fun logPurchase(itemId: String, price: Double, currency: String = "TRY") {
        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE) {
            param(FirebaseAnalytics.Param.ITEM_ID, itemId)
            param(FirebaseAnalytics.Param.PRICE, price)
            param(FirebaseAnalytics.Param.CURRENCY, currency)
        }
    }

    fun logGiftSent(giftId: String, receiverId: String, price: Int) {
        analytics.logEvent("gift_sent") {
            param("gift_id", giftId)
            param("receiver_id", receiverId)
            param("price", price.toLong())
        }
    }

    fun logGiftReceived(giftId: String, senderId: String, price: Int) {
        analytics.logEvent("gift_received") {
            param("gift_id", giftId)
            param("sender_id", senderId)
            param("price", price.toLong())
        }
    }

    fun logLiveRoomCreated(roomId: String, roomType: String) {
        analytics.logEvent("live_room_created") {
            param("room_id", roomId)
            param("room_type", roomType)
        }
    }

    fun logLiveRoomJoined(roomId: String) {
        analytics.logEvent("live_room_joined") {
            param("room_id", roomId)
        }
    }

    fun logGamePlayed(gameType: String, betAmount: Int, winAmount: Int) {
        analytics.logEvent("game_played") {
            param("game_type", gameType)
            param("bet_amount", betAmount.toLong())
            param("win_amount", winAmount.toLong())
        }
    }

    fun logVideoCallStarted(duration: Long) {
        analytics.logEvent("video_call_started") {
            param("duration", duration)
        }
    }

    fun logScreenView(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
    }

    fun logShare(contentType: String, itemId: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SHARE) {
            param(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
            param(FirebaseAnalytics.Param.ITEM_ID, itemId)
        }
    }

    fun setUserId(userId: String?) {
        analytics.setUserId(userId)
    }

    fun setUserProperty(name: String, value: String?) {
        analytics.setUserProperty(name, value)
    }
}
