package com.vkolte.doorlocker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vkolte.doorlocker.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = viewModel(),
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAddPerson: () -> Unit = {},
    onNavigateToUpdateVolume: () -> Unit = {},
    onNavigateToVideoCall: () -> Unit = {},
    onNavigateToRegistration: () -> Unit = {}
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }

    // Handle toast messages
    LaunchedEffect(dashboardViewModel.toastMessage) {
        dashboardViewModel.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            dashboardViewModel.clearToast()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DoorLocker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showMenu = false
                                onNavigateToSettings()
                            },
                            leadingIcon = { Icon(Icons.Default.Settings, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Add Person") },
                            onClick = {
                                showMenu = false
                                onNavigateToAddPerson()
                            },
                            leadingIcon = { Icon(Icons.Default.Person, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Update Volume") },
                            onClick = {
                                showMenu = false
                                onNavigateToUpdateVolume()
                            },
                            leadingIcon = { Icon(Icons.Default.VolumeUp, null) }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VDB Status: ${dashboardViewModel.vdbStatus}",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Grid of Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { dashboardViewModel.toggleDoorLock() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (dashboardViewModel.isDoorLocked) "Unlock Door" else "Lock Door")
                    }
                    Button(
                        onClick = {
                            dashboardViewModel.requestOnDemandVideo()
                            onNavigateToVideoCall()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("OnDemand Video")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { dashboardViewModel.triggerSOS() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("SOS")
                    }
                    Button(
                        onClick = { dashboardViewModel.toggleDND() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (dashboardViewModel.isDNDEnabled) "Disable DND" else "Enable DND")
                    }
                }

                Button(
                    onClick = { dashboardViewModel.toggleAI() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (dashboardViewModel.isAIEnabled) "AI OFF" else "AI ON")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Disconnect Button
            Button(
                onClick = {
                    dashboardViewModel.disconnect()
                    onNavigateToRegistration()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Disconnect")
            }
        }
    }
} 