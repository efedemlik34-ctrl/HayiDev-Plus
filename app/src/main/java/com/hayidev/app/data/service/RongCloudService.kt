package com.hayidev.app.data.service

import android.content.Context
import com.hayidev.app.data.model.Message
import com.hayidev.app.data.model.MessageType
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message as RongMessage
import io.rong.imlib.model.MessageContent
import io.rong.message.TextMessage
import io.rong.message.ImageMessage
import io.rong.message.VoiceMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RongCloudService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var isConnected = false

    fun init(appKey: String) {
        RongIM.init(context, appKey)
    }

    fun connect(
        token: String,
        onSuccess: (() -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        RongIM.connect(token, object : RongIMClient.ConnectCallback() {
            override fun onSuccess(userId: String?) {
                isConnected = true
                onSuccess?.invoke()
            }

            override fun onError(error: RongIMClient.ErrorCode?) {
                isConnected = false
                onError?.invoke(Exception("Connection failed: ${error?.message}"))
            }
        })
    }

    fun disconnect() {
        RongIM.getInstance().disconnect()
        isConnected = false
    }

    fun sendMessage(
        targetId: String,
        content: MessageContent,
        onSuccess: ((Long) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        RongIM.getInstance().sendMessage(
            Conversation.ConversationType.PRIVATE,
            targetId,
            content,
            null,
            null,
            object : RongIMClient.SendMessageCallback() {
                override fun onSuccess(messageId: Long) {
                    onSuccess?.invoke(messageId)
                }

                override fun onError(messageId: Long?, error: RongIMClient.ErrorCode?) {
                    onError?.invoke(Exception("Send failed: ${error?.message}"))
                }
            }
        )
    }

    fun sendTextMessage(
        targetId: String,
        text: String,
        onSuccess: ((Long) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        val content = TextMessage.obtain(text)
        sendMessage(targetId, content, onSuccess, onError)
    }

    fun sendImageMessage(
        targetId: String,
        imageUrl: String,
        onSuccess: ((Long) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        val content = ImageMessage.obtain(imageUrl)
        sendMessage(targetId, content, onSuccess, onError)
    }

    fun sendVoiceMessage(
        targetId: String,
        voiceUri: String,
        duration: Long,
        onSuccess: ((Long) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        val content = VoiceMessage.obtain(android.net.Uri.parse(voiceUri), duration)
        sendMessage(targetId, content, onSuccess, onError)
    }

    fun getConversations(
        onSuccess: ((List<Conversation>) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        RongIM.getInstance().getConversationList(
            object : RongIMClient.ResultCallback<List<Conversation>>() {
                override fun onSuccess(data: List<Conversation>?) {
                    onSuccess?.invoke(data ?: emptyList())
                }

                override fun onError(error: RongIMClient.ErrorCode?) {
                    onError?.invoke(Exception("Get conversations failed: ${error?.message}"))
                }
            },
            Conversation.ConversationType.PRIVATE,
            Conversation.ConversationType.GROUP
        )
    }

    fun observeMessages(targetId: String): Flow<RongMessage> = callbackFlow {
        val listener = object : RongIMClient.OnReceiveMessageListener {
            override fun onReceived(message: RongMessage?, p1: Int): Boolean {
                message?.let { trySend(it) }
                return true
            }
        }
        RongIM.getInstance().registerMessageListener(listener)
        awaitClose {
            RongIM.getInstance().unRegisterMessageListener(listener)
        }
    }

    fun startConversation(targetId: String) {
        RongIM.getInstance().startConversation(
            context,
            Conversation.ConversationType.PRIVATE,
            targetId,
            ""
        )
    }

    fun readMessages(targetId: String, timestamp: Long) {
        RongIM.getInstance().readMessages(
            Conversation.ConversationType.PRIVATE,
            targetId,
            timestamp,
            object : RongIMClient.ResultCallback<Boolean>() {
                override fun onSuccess(data: Boolean?) {}
                override fun onError(error: RongIMClient.ErrorCode?) {}
            }
        )
    }

    fun isConnected(): Boolean = isConnected
}
