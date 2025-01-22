package com.vkolte.doorlocker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkolte.doorlocker.ui.theme.DoorLockerTheme
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.vkolte.doorlocker.ui.screens.SplashScreen
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vkolte.doorlocker.viewmodel.RegistrationViewModel
import com.vkolte.doorlocker.viewmodel.DashboardViewModel
import com.vkolte.doorlocker.viewmodel.VideoCallViewModel
import com.vkolte.doorlocker.ui.screens.DashboardScreen
import com.vkolte.doorlocker.ui.screens.VideoCallScreen

class MainActivity : ComponentActivity() {
    private val requiredPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.NEARBY_WIFI_DEVICES,
        // Add other permissions as needed
    )

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* Handle permission results if needed */ }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoorLockerTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
                val registrationViewModel: RegistrationViewModel = viewModel()
                val dashboardViewModel: DashboardViewModel = viewModel()
                val videoCallViewModel: VideoCallViewModel = viewModel()
                
                LaunchedEffect(Unit) {
                    delay(3000)
                    currentScreen = Screen.Registration
                    checkAndRequestPermissions()
                }

                LaunchedEffect(registrationViewModel.navigateToDashboard) {
                    if (registrationViewModel.navigateToDashboard) {
                        currentScreen = Screen.Dashboard
                        registrationViewModel.resetNavigation()
                    }
                }

                when (currentScreen) {
                    Screen.Splash -> SplashScreen()
                    Screen.Registration -> RegistrationScreen(registrationViewModel)
                    Screen.Dashboard -> DashboardScreen(
                        dashboardViewModel = dashboardViewModel,
                        onNavigateToVideoCall = {
                            currentScreen = Screen.VideoCall
                        }
                    )
                    Screen.VideoCall -> VideoCallScreen(
                        videoCallViewModel = videoCallViewModel,
                        onCallEnded = {
                            currentScreen = Screen.Dashboard
                        }
                    )
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }
}

sealed class Screen {
    object Splash : Screen()
    object Registration : Screen()
    object Dashboard : Screen()
    object VideoCall : Screen()
}

@Composable
fun VideoDoorbell() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        VideoDoorbellScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun VideoDoorbellScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppLogo()
            Spacer(modifier = Modifier.height(24.dp))
            AppTitle()
        }
    }
}

@Composable
private fun AppLogo() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Logo",
        modifier = Modifier.size(150.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun AppTitle() {
    Text(
        text = "VIDEO DOORBELL\nAPPLICATION!!",
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RegistrationScreen(registrationViewModel: RegistrationViewModel, modifier: Modifier = Modifier) {
    var ipAddress by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Registration",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = ipAddress,
                onValueChange = { ipAddress = it },
                label = { Text("IP Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = { registrationViewModel.autoDiscover() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Auto Discover")
            }

            Button(
                onClick = { registrationViewModel.connect() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Connect")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoDoorbellPreview() {
    DoorLockerTheme {
        VideoDoorbell()
    }
}