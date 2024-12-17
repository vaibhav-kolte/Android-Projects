package com.vkolte.billgenerator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vkolte.billgenerator.viewmodel.RecheckViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import com.vkolte.billgenerator.data.Patient
import com.vkolte.billgenerator.data.Prescription
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecheckScreen(
    viewModel: RecheckViewModel = viewModel(),
    onNavigateToNewPrescription: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Patient Search",
                style = MaterialTheme.typography.headlineSmall
            )
            // Empty box for alignment
            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Enter Patient Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = viewModel::searchPatient,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        uiState.patient?.let { patient ->
            PatientDetails(
                patient = patient,
                prescriptions = uiState.prescriptions,
                onNewPrescription = { onNavigateToNewPrescription(patient.patientId) }
            )
        }

        if (uiState.error.isNotBlank()) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun PatientDetails(
    patient: Patient,
    prescriptions: List<Prescription>,
    onNewPrescription: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Patient Information",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Patient Number: ${patient.patientNumber}")
                Text("Name: ${patient.name}")
                Text("Age: ${patient.age}")
                Text(
                    "Registration Date: ${
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(patient.registrationDate)
                    }"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onNewPrescription,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("New Prescription")
                }
            }
        }

        prescriptions.forEach { prescription ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Visit Date: ${
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(prescription.visitDate)
                        }",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Diagnosis: ${prescription.diagnosis}")
                    Text("Medicines:")
                    Text(
                        prescription.medicines,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Text("Consultation Fee: ₹${prescription.consultationFee}")
                }
            }
        }
    }
} 