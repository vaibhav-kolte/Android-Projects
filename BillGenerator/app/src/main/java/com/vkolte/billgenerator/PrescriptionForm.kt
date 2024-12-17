package com.vkolte.billgenerator

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine
import com.itextpdf.layout.properties.TextAlignment
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.vkolte.billgenerator.viewmodel.PrescriptionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vkolte.billgenerator.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import android.graphics.Paint
import android.graphics.Typeface

@Composable
fun PrescriptionForm(
    viewModel: PrescriptionViewModel = viewModel(),
    patientId: Long? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(patientId) {
        patientId?.let { viewModel.getPatientDetails(it) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                text = if (patientId != null) "New Prescription" else "New Patient",
                style = MaterialTheme.typography.headlineSmall
            )
            // Empty box for alignment
            Box(modifier = Modifier.size(48.dp))
        }

        OutlinedTextField(
            value = uiState.patientName,
            onValueChange = viewModel::updatePatientName,
            label = { Text("Patient Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.showErrors && uiState.patientName.isBlank(),
            supportingText = if (uiState.showErrors && uiState.patientName.isBlank()) {
                { Text("Patient name is required") }
            } else null
        )

        OutlinedTextField(
            value = uiState.patientAge,
            onValueChange = viewModel::updatePatientAge,
            label = { Text("Patient Age") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.showErrors && uiState.patientAge.isBlank(),
            supportingText = if (uiState.showErrors && uiState.patientAge.isBlank()) {
                { Text("Patient age is required") }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = uiState.diagnosis,
            onValueChange = viewModel::updateDiagnosis,
            label = { Text("Diagnosis") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            isError = uiState.showErrors && uiState.diagnosis.isBlank(),
            supportingText = if (uiState.showErrors && uiState.diagnosis.isBlank()) {
                { Text("Diagnosis is required") }
            } else null
        )

        OutlinedTextField(
            value = uiState.medicines,
            onValueChange = viewModel::updateMedicines,
            label = { Text("Prescribed Medicines") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            isError = uiState.showErrors && uiState.medicines.isBlank(),
            supportingText = if (uiState.showErrors && uiState.medicines.isBlank()) {
                { Text("Prescribed medicines are required") }
            } else null
        )

        OutlinedTextField(
            value = uiState.consultationFee,
            onValueChange = viewModel::updateConsultationFee,
            label = { Text("Consultation Fee (₹)") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.showErrors && uiState.consultationFee.isBlank(),
            supportingText = if (uiState.showErrors && uiState.consultationFee.isBlank()) {
                { Text("Consultation fee is required") }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    viewModel.savePrescription()
                },
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Saving..." else "Show Bill")
            }

            Button(
                onClick = {
                    if (viewModel.validateFields()) {
                        generatePDF(
                            context = context,
                            patientDetails = BillDetails(
                                patientNumber = uiState.patientNumber,
                                patientName = uiState.patientName,
                                patientAge = uiState.patientAge,
                                diagnosis = uiState.diagnosis,
                                medicines = uiState.medicines,
                                consultationFee = uiState.consultationFee
                            )
                        )
                    }
                },
                enabled = uiState.showBill
            ) {
                Text("Download PDF")
            }
        }

        if (uiState.showBill) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Bill Summary", style = MaterialTheme.typography.titleMedium)
                    Text("Patient Number: ${uiState.patientNumber}")
                    Text("Patient Name: ${uiState.patientName}")
                    Text("Age: ${uiState.patientAge}")
                    Text("Diagnosis: ${uiState.diagnosis}")
                    Text("Prescribed Medicines:")
                    Text(uiState.medicines)
                    HorizontalDivider()
                    Text(
                        "Total Amount: ₹${uiState.consultationFee}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        if (uiState.errorMessage.isNotBlank()) {
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

data class BillDetails(
    val patientNumber: String,
    val patientName: String,
    val patientAge: String,
    val diagnosis: String,
    val medicines: String,
    val consultationFee: String
)

fun generatePDF(context: Context, patientDetails: BillDetails) {
    try {
        // Create file
        val fileName = "Bill_${patientDetails.patientNumber}_${System.currentTimeMillis()}.pdf"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        // Initialize PDF writer
        val writer = PdfWriter(file)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)
        
        // Add hospital name
        val hospitalName = Paragraph(Constants.HOSPITAL_NAME)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(16f)
        document.add(hospitalName)
        
        document.add(Paragraph("\n")) // Add spacing
        
        // Add patient details
        document.add(Paragraph("Patient Number: ${patientDetails.patientNumber}"))
        document.add(Paragraph("Patient Name: ${patientDetails.patientName}"))
        document.add(Paragraph("Age: ${patientDetails.patientAge}"))
        
        document.add(Paragraph("\n")) // Add spacing
        
        // Add diagnosis
        document.add(Paragraph("Diagnosis:"))
        document.add(Paragraph(patientDetails.diagnosis)
            .setMarginLeft(20f))
        
        document.add(Paragraph("\n")) // Add spacing
        
        // Add medicines
        document.add(Paragraph("Prescribed Medicines:"))
        patientDetails.medicines.split("\n").forEach { medicine ->
            document.add(Paragraph(medicine)
                .setMarginLeft(20f))
        }
        
        document.add(Paragraph("\n")) // Add spacing
        
        // Add dotted line
        document.add(LineSeparator(DottedLine()))
        
        // Add consultation fee
        document.add(Paragraph("Consultation Fee: ₹${patientDetails.consultationFee}")
            .setTextAlignment(TextAlignment.RIGHT)
            .setFontSize(12f)
            .setBold())
        
        // Close document
        document.close()
        
        // Show success message
        Toast.makeText(context, "PDF saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        
        // Show notification for download
        showDownloadNotification(context, fileName)
        
    } catch (e: Exception) {
        Toast.makeText(context, "Error generating PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun showDownloadNotification(context: Context, fileName: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "pdf_download_channel"
    
    // Create notification channel for Android 8.0 and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "PDF Downloads",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for PDF download completion"
        }
        notificationManager.createNotificationChannel(channel)
    }

    // Create intent to open the PDF file
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
    val fileUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
    
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(fileUri, "application/pdf")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    // Build the notification
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("PDF Downloaded Successfully")
        .setContentText("Prescription saved as $fileName")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

    // Show the notification
    notificationManager.notify(1, notification)
}
