package com.vkolte.doorlocker.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {
    var ipAddress by mutableStateOf("")
        private set

    private val _navigateToDashboard = mutableStateOf(false)
    val navigateToDashboard: Boolean
        get() = _navigateToDashboard.value

    fun updateIpAddress(newValue: String) {
        ipAddress = newValue
    }

    fun autoDiscover() {
        // Implement auto discover logic
    }

    fun connect() {
        // Implement connect logic
        _navigateToDashboard.value = true
    }

    fun resetNavigation() {
        _navigateToDashboard.value = false
    }
} 