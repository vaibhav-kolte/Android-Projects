package com.vkolte.doorlocker.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    var isDoorLocked by mutableStateOf(false)
        private set
    
    var isDNDEnabled by mutableStateOf(false)
        private set
    
    var isAIEnabled by mutableStateOf(false)
        private set
    
    var vdbStatus by mutableStateOf("Online")
        private set

    private val _toastMessage = mutableStateOf<String?>(null)
    val toastMessage: String?
        get() = _toastMessage.value

    private val _navigateToVideoCall = mutableStateOf(false)
    val navigateToVideoCall: Boolean
        get() = _navigateToVideoCall.value

    fun clearToast() {
        _toastMessage.value = null
    }

    fun toggleDoorLock() {
        isDoorLocked = !isDoorLocked
        _toastMessage.value = if (isDoorLocked) "Door Locked" else "Door Unlocked"
    }

    fun toggleDND() {
        isDNDEnabled = !isDNDEnabled
        _toastMessage.value = if (isDNDEnabled) "DND Enabled" else "DND Disabled"
    }

    fun toggleAI() {
        isAIEnabled = !isAIEnabled
        _toastMessage.value = if (isAIEnabled) "AI Enabled" else "AI Disabled"
    }

    fun requestOnDemandVideo() {
        _toastMessage.value = "Requesting OnDemand Video"
        _navigateToVideoCall.value = true
    }

    fun resetVideoCallNavigation() {
        _navigateToVideoCall.value = false
    }

    fun triggerSOS() {
        _toastMessage.value = "SOS Triggered"
    }

    fun disconnect() {
        _toastMessage.value = "Disconnecting..."
    }
} 