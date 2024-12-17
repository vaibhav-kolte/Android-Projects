package com.vkolte.billgenerator.data

import androidx.room.Embedded
import androidx.room.Relation

data class PatientWithPrescriptions(
    @Embedded
    val patient: Patient,
    
    @Relation(
        parentColumn = "patientId",
        entityColumn = "patientId"
    )
    val prescriptions: List<Prescription>
) 