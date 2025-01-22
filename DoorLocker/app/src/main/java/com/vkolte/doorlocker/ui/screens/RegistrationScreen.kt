package com.vkolte.doorlocker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vkolte.doorlocker.viewmodel.RegistrationViewModel

@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel = viewModel()
) {
    Box(
        modifier = Modifier
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
                value = registrationViewModel.ipAddress,
                onValueChange = { registrationViewModel.updateIpAddress(it) },
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