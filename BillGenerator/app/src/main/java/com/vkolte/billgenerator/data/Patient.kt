package com.vkolte.billgenerator.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val patientId: Long = 0,
    val patientNumber: String,
    val name: String,
    val age: String,
    val registrationDate: Date = Date()
)

@Entity(tableName = "prescriptions")
data class Prescription(
    @PrimaryKey(autoGenerate = true)
    val prescriptionId: Long = 0,
    val patientId: Long,
    val diagnosis: String,
    val medicines: String,
    val consultationFee: String,
    val visitDate: Date = Date(),
    val isRecheck: Boolean = false
) 