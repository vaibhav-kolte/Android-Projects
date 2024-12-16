package com.vkolte.billgenerater.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState

    fun checkPermissionsAndStartDelay(permissions: Array<String>) {
        viewModelScope.launch {
            if (arePermissionsGranted(permissions)) {
                startSplashDelay()
            } else {
                _uiState.value = SplashUiState.RequiresPermission
            }
        }
    }

    private fun arePermissionsGranted(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(getApplication(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun startSplashDelay() {
        viewModelScope.launch {
            delay(5000) // 5 seconds delay
            _uiState.value = SplashUiState.NavigateToMain
        }
    }
}

sealed class SplashUiState {
    object Loading : SplashUiState()
    object RequiresPermission : SplashUiState()
    object NavigateToMain : SplashUiState()
} 