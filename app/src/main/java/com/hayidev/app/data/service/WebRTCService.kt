package com.hayidev.app.data.service

import android.content.Context
import io.getstream.webrtc.android.ui.VideoRenderer
import io.getstream.webrtc.android.video.WebRTCRenderer
import io.stream.webrtc.android.PeerConnectionFactory
import io.stream.webrtc.android.RTCIceCandidate
import io.stream.webrtc.android.RTCSessionDescription
import io.stream.webrtc.android.RTCTrackEvent
import io.stream.webrtc.android.VideoTrack
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebRTCService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var peerConnection: io.stream.webrtc.android.PeerConnection? = null
    private var localVideoTrack: VideoTrack? = null
    private var localAudioTrack: io.stream.webrtc.android.AudioTrack? = null

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _remoteVideoTrack = MutableStateFlow<VideoTrack?>(null)
    val remoteVideoTrack: StateFlow<VideoTrack?> = _remoteVideoTrack

    private val _localVideoTrack = MutableStateFlow<VideoTrack?>(null)
    val localVideoTrackFlow: StateFlow<VideoTrack?> = _localVideoTrack

    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        FAILED,
        CLOSED
    }

    fun initialize() {
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    suspend fun createOffer(
        onOffer: (RTCSessionDescription) -> Unit,
        onError: ((Exception) -> Unit)? = null
    ) {
        try {
            _connectionState.value = ConnectionState.CONNECTING
            val offer = peerConnection?.createOffer(null)
            offer?.let { onOffer(it) }
        } catch (e: Exception) {
            _connectionState.value = ConnectionState.FAILED
            onError?.invoke(e)
        }
    }

    suspend fun createAnswer(
        onAnswer: (RTCSessionDescription) -> Unit,
        onError: ((Exception) -> Unit)? = null
    ) {
        try {
            val answer = peerConnection?.createAnswer(null)
            answer?.let { onAnswer(it) }
        } catch (e: Exception) {
            onError?.invoke(e)
        }
    }

    fun setRemoteDescription(description: RTCSessionDescription) {
        peerConnection?.setRemoteDescription(description)
    }

    fun setLocalDescription(description: RTCSessionDescription) {
        peerConnection?.setLocalDescription(description)
    }

    fun addIceCandidate(candidate: RTCIceCandidate) {
        peerConnection?.addIceCandidate(candidate)
    }

    fun enableVideo(enabled: Boolean) {
        localVideoTrack?.setEnabled(enabled)
    }

    fun enableAudio(enabled: Boolean) {
        localAudioTrack?.setEnabled(enabled)
    }

    fun switchCamera() {
        localVideoTrack?.switchCamera()
    }

    fun toggleMute(muted: Boolean) {
        localAudioTrack?.setEnabled(!muted)
    }

    fun disconnect() {
        localVideoTrack?.dispose()
        localAudioTrack?.dispose()
        peerConnection?.close()
        peerConnection = null
        _connectionState.value = ConnectionState.CLOSED
        _remoteVideoTrack.value = null
        _localVideoTrack.value = null
    }

    fun isVideoEnabled(): Boolean = localVideoTrack?.enabled() ?: false
    fun isAudioEnabled(): Boolean = localAudioTrack?.enabled() ?: false
}
