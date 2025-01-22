package com.vkolte.doorlocker.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class VideoCallViewModel : ViewModel() {
    var isSpeakerOn by mutableStateOf(true)
        private set
    
    var isDoorLocked by mutableStateOf(false)
        private set
    
    var isMicOn by mutableStateOf(true)
        private set

    private val _toastMessage = mutableStateOf<String?>(null)
    val toastMessage: String?
        get() = _toastMessage.value

    fun clearToast() {
        _toastMessage.value = null
    }

    fun toggleSpeaker() {
        isSpeakerOn = !isSpeakerOn
        _toastMessage.value = if (isSpeakerOn) "Speaker ON" else "Speaker OFF"
    }

    fun toggleDoorLock() {
        isDoorLocked = !isDoorLocked
        _toastMessage.value = if (isDoorLocked) "Door Locked" else "Door Unlocked"
    }

    fun toggleMic() {
        isMicOn = !isMicOn
        _toastMessage.value = if (isMicOn) "Microphone ON" else "Microphone OFF"
    }

    fun hangupCall() {
        _toastMessage.value = "Call Ended"
    }

    fun triggerSOS() {
        _toastMessage.value = "SOS Triggered"
    }
} 