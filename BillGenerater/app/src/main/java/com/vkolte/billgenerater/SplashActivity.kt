package com.vkolte.billgenerater

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vkolte.billgenerater.ui.theme.BillGeneraterTheme
import com.vkolte.billgenerater.viewmodel.SplashViewModel
import com.vkolte.billgenerater.viewmodel.SplashUiState

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
        if (permissions.values.all { it }) {
            viewModel.startSplashDelay()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            BillGeneraterTheme {
                val uiState by viewModel.uiState.collectAsState()
                
                SplashScreen(
                    showPermissionDialog = uiState is SplashUiState.RequiresPermission,
                    onRequestPermissions = { permissionLauncher.launch(requiredPermissions) }
                )
                
                // Handle navigation in a LaunchedEffect
                LaunchedEffect(uiState) {
                    when (uiState) {
                        is SplashUiState.Loading -> {
                            viewModel.checkPermissionsAndStartDelay(requiredPermissions)
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

@Composable
fun SplashScreen(
    showPermissionDialog: Boolean,
    onRequestPermissions: () -> Unit
) {
    var showDialog by remember { mutableStateOf(showPermissionDialog) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo/Icon
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = Constants.HOSPITAL_NAME,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${Constants.DOCTOR_NAME}\n${Constants.EDUCATION}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Experience: ${Constants.EXPERIENCE}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("Permission Required") },
                    text = { 
                        Text("This app needs permissions to save prescriptions and send notifications. Please grant the required permissions to continue.")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                onRequestPermissions()
                            }
                        ) {
                            Text("Grant Permissions")
                        }
                    }
                )
            }
        }
    }
}
