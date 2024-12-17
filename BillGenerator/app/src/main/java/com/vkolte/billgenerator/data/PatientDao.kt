package com.vkolte.billgenerator.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Insert
    suspend fun insertPatient(patient: Patient): Long

    @Query("SELECT * FROM patients WHERE patientNumber = :patientNumber")
    suspend fun getPatientByNumber(patientNumber: String): Patient?

    @Query("SELECT * FROM patients WHERE patientId = :patientId")
    suspend fun getPatientById(patientId: Long): Patient?

    @Query("SELECT * FROM patients ORDER BY registrationDate DESC")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT * FROM patients WHERE patientNumber LIKE :datePrefix || '%' ORDER BY patientNumber DESC LIMIT 1")
    suspend fun getLastPatientOfDay(datePrefix: String): Patient?

    @Transaction
    @Query("SELECT * FROM patients WHERE patientId = :patientId")
    fun getPatientWithPrescriptions(patientId: Long): Flow<PatientWithPrescriptions>
}

@Dao
interface PrescriptionDao {
    @Insert
    suspend fun insertPrescription(prescription: Prescription): Long

    @Query("SELECT * FROM prescriptions WHERE patientId = :patientId ORDER BY visitDate DESC")
    fun getPrescriptionsByPatientId(patientId: Long): Flow<List<Prescription>>

    @Query("SELECT * FROM prescriptions ORDER BY visitDate DESC")
    fun getAllPrescriptions(): Flow<List<Prescription>>
} 