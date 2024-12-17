package com.vkolte.billgenerator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vkolte.billgenerator.data.Patient
import com.vkolte.billgenerator.data.Prescription
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PatientDetails(
    patient: Patient,
    prescriptions: List<Prescription>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PatientInfoCard(patient)
        }

        items(prescriptions) { prescription ->
            PrescriptionCard(prescription)
        }
    }
}

@Composable
private fun PatientInfoCard(patient: Patient) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
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
        }
    }
}

@Composable
private fun PrescriptionCard(prescription: Prescription) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
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