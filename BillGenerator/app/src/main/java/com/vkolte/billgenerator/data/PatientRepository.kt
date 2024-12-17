package com.vkolte.billgenerator.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PatientRepository(private val database: AppDatabase) {
    private val patientDao = database.patientDao()
    private val prescriptionDao = database.prescriptionDao()

    suspend fun generatePatientNumber(): String {
        val currentDate = SimpleDateFormat("yyMMdd", Locale.getDefault()).format(Date())
        val lastPatient = patientDao.getLastPatientOfDay(currentDate)
        val sequence = (lastPatient?.patientNumber?.takeLast(3)?.toIntOrNull() ?: 0) + 1
        return "$currentDate${sequence.toString().padStart(3, '0')}"
    }

    suspend fun savePatientWithPrescription(
        patientName: String,
        patientAge: String,
        diagnosis: String,
        medicines: String,
        consultationFee: String
    ): Long {
        val patientNumber = generatePatientNumber()
        
        // Save patient
        val patient = Patient(
            patientNumber = patientNumber,
            name = patientName,
            age = patientAge
        )
        val patientId = patientDao.insertPatient(patient)

        // Save prescription
        val prescription = Prescription(
            patientId = patientId,
            diagnosis = diagnosis,
            medicines = medicines,
            consultationFee = consultationFee
        )
        prescriptionDao.insertPrescription(prescription)

        return patientId
    }

    fun getPatientWithPrescriptions(patientId: Long): Flow<PatientWithPrescriptions> {
        return patientDao.getPatientWithPrescriptions(patientId)
    }

    suspend fun getPatientById(patientId: Long): Patient? {
        return patientDao.getPatientById(patientId)
    }

    suspend fun getPatientByNumber(patientNumber: String): Patient? {
        return patientDao.getPatientByNumber(patientNumber)
    }
} 