package com.vkolte.billgenerator

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vkolte.billgenerator.ui.theme.BillGeneratorTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState

    fun checkPermissionsAndStartDelay(context: Context, permissions: Array<String>) {
        viewModelScope.launch {
            // Check if all permissions are granted
            val allPermissionsGranted = permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }

            if (allPermissionsGranted) {
                // If all permissions are granted, start delay immediately
                startSplashDelay()
            } else {
                // If any permission is not granted, show permission dialog
                _uiState.value = SplashUiState.RequiresPermission
            }
        }
    }

    fun startSplashDelay() {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Loading
            delay(3000) // 3 seconds delay
            _uiState.value = SplashUiState.NavigateToMain
        }
    }
}

sealed class SplashUiState {
    object Loading : SplashUiState()
    object RequiresPermission : SplashUiState()
    object NavigateToMain : SplashUiState()
}

@Composable
fun SplashScreen(
    showPermissionDialog: Boolean,
    onRequestPermissions: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = Constants.HOSPITAL_NAME,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (showPermissionDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Permission Required") },
                text = { 
                    Text("This app needs permissions to save prescriptions and send notifications. Please grant the required permissions to continue.")
                },
                confirmButton = {
                    Button(onClick = onRequestPermissions) {
                        Text("Grant Permissions")
                    }
                }
            )
        }
    }
}

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    private val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            // If all permissions are granted, start delay
            viewModel.startSplashDelay()
        } else {
            // If any permission is denied, check again (this will show the dialog)
            viewModel.checkPermissionsAndStartDelay(this, requiredPermissions)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            BillGeneratorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by viewModel.uiState.collectAsState()
                    
                    SplashScreen(
                        showPermissionDialog = uiState is SplashUiState.RequiresPermission,
                        onRequestPermissions = { permissionLauncher.launch(requiredPermissions) }
                    )
                    
                    LaunchedEffect(uiState) {
                        when (uiState) {
                            is SplashUiState.Loading -> {
                                viewModel.checkPermissionsAndStartDelay(this@SplashActivity, requiredPermissions)
                            }
                            is SplashUiState.NavigateToMain -> {
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                finish()
                            }
                            else -> { /* do nothing */ }
                        }
                    }
                }
            }
        }
    }
}
