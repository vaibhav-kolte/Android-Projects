package com.vkolte.billgenerater

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.vkolte.billgenerater.viewmodel.PrescriptionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun PrescriptionForm(
    viewModel: PrescriptionViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Doctor's Prescription & Bill Generator",
            style = MaterialTheme.typography.headlineSmall
        )

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
                    if (viewModel.validateFields()) {
                        viewModel.showBill()
                    }
                }
            ) {
                Text("Show Bill")
            }

            Button(
                onClick = {
                    if (viewModel.validateFields()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            generatePDF(
                                context,
                                uiState.patientName,
                                uiState.patientAge,
                                uiState.diagnosis,
                                uiState.medicines,
                                uiState.consultationFee
                            )
                        } else {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    generatePDF(
                                        context,
                                        uiState.patientName,
                                        uiState.patientAge,
                                        uiState.diagnosis,
                                        uiState.medicines,
                                        uiState.consultationFee
                                    )
                                }
                                else -> {
                                    Toast.makeText(
                                        context,
                                        "Storage permission required",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
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
                    Text("Patient Name: ${uiState.patientName}")
                    Text("Age: ${uiState.patientAge}")
                    Text("Diagnosis: ${uiState.diagnosis}")
                    Text("Prescribed Medicines:")
                    Text(uiState.medicines)
                    Divider()
                    Text(
                        "Total Amount: ₹${uiState.consultationFee}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

private fun generatePDF(
    context: Context,
    patientName: String,
    patientAge: String,
    diagnosis: String,
    medicines: String,
    consultationFee: String
) {
    try {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val fileName = "Prescription_${currentDate}.pdf"

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        PdfWriter(file).use { writer ->
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)

            // Set page margins
            document.setMargins(40f, 40f, 40f, 40f)

            // Hospital name with larger, bold font
            val hospitalName = Paragraph(Constants.HOSPITAL_NAME)
                .setFontSize(20f)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
            document.add(hospitalName)

            // Doctor details with professional formatting
            val doctorInfo = Paragraph("${Constants.DOCTOR_NAME} (${Constants.EDUCATION})")
                .setFontSize(16f)
                .setTextAlignment(TextAlignment.CENTER)
            document.add(doctorInfo)

            val experience = Paragraph("Experience: ${Constants.EXPERIENCE}")
                .setFontSize(12f)
                .setTextAlignment(TextAlignment.CENTER)
            document.add(experience)

            val contact = Paragraph("Contact: ${Constants.CONTACT_NUMBER}")
                .setFontSize(12f)
                .setTextAlignment(TextAlignment.CENTER)
            document.add(contact)

            // Separator line
            document.add(LineSeparator(DottedLine())
                .setMarginTop(10f)
                .setMarginBottom(10f))

            // Current date
            val currentDateFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            document.add(
                Paragraph("Date: $currentDateFormatted")
                    .setFontSize(12f)
            )

            // Patient details section
            document.add(
                Paragraph("Patient Details")
                    .setBold()
                    .setFontSize(14f)
                    .setMarginTop(20f)
            )

            document.add(Paragraph("Name: $patientName").setFontSize(12f))
            document.add(Paragraph("Age: $patientAge").setFontSize(12f))

            // Diagnosis section
            document.add(
                Paragraph("Diagnosis")
                    .setBold()
                    .setFontSize(14f)
                    .setMarginTop(20f)
            )
            document.add(Paragraph(diagnosis).setFontSize(12f))

            // Medicines section
            document.add(
                Paragraph("Prescribed Medicines")
                    .setBold()
                    .setFontSize(14f)
                    .setMarginTop(20f)
            )
            
            // Split medicines into bullet points
            medicines.split("\n").forEach { medicine ->
                if (medicine.isNotBlank()) {
                    document.add(
                        Paragraph("• $medicine")
                            .setFontSize(12f)
                            .setMarginLeft(20f)
                    )
                }
            }

            // Separator line
            document.add(LineSeparator(DottedLine())
                .setMarginTop(20f)
                .setMarginBottom(20f))

            // Consultation fee with bold total
            document.add(
                Paragraph("Consultation Fee: ₹$consultationFee")
                    .setBold()
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.RIGHT)
            )

            // Footer
            document.add(
                Paragraph("Thank you for your visit")
                    .setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30f)
                    .setItalic()
            )

            document.close()
        }

        // Show notification for successful download
        showDownloadNotification(context, fileName)

        Toast.makeText(
            context,
            "PDF saved to Downloads folder: $fileName",
            Toast.LENGTH_LONG
        ).show()

    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error generating PDF: ${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }
}

private fun showDownloadNotification(context: Context, fileName: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "pdf_download_channel"
    
    // Create notification channel for Android 8.0 and above
    val channel = NotificationChannel(
        channelId,
        "PDF Downloads",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Notifications for PDF download completion"
    }
    notificationManager.createNotificationChannel(channel)

    // Create intent to open the PDF file
    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
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
        .setSmallIcon(R.drawable.ic_notifications) // You'll need to add this icon
        .setContentTitle("PDF Downloaded Successfully")
        .setContentText("Prescription saved as $fileName")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .addAction(
            R.drawable.ic_open, // You'll need to add this icon
            "Open PDF",
            pendingIntent
        )
        .build()

    // Show the notification
    notificationManager.notify(1, notification)
}

@Preview(showBackground = true)
@Composable
fun PrescriptionFormPreview() {
    PrescriptionForm()
}