package com.vkolte.billgenerator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vkolte.billgenerator.data.AppDatabase
import com.vkolte.billgenerator.data.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PrescriptionUiState(
    val patientNumber: String = "",
    val patientName: String = "",
    val patientAge: String = "",
    val diagnosis: String = "",
    val medicines: String = "",
    val consultationFee: String = "",
    val showErrors: Boolean = false,
    val isLoading: Boolean = false,
    val showBill: Boolean = false,
    val patientId: Long? = null,
    val errorMessage: String = ""
)

class PrescriptionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PatientRepository(AppDatabase.getDatabase(application))
    private val _uiState = MutableStateFlow(PrescriptionUiState())
    val uiState: StateFlow<PrescriptionUiState> = _uiState

    fun updatePatientName(value: String) {
        _uiState.update { it.copy(patientName = value) }
    }

    fun updatePatientAge(value: String) {
        _uiState.update { it.copy(patientAge = value) }
    }

    fun updateDiagnosis(value: String) {
        _uiState.update { it.copy(diagnosis = value) }
    }

    fun updateMedicines(value: String) {
        _uiState.update { it.copy(medicines = value) }
    }

    fun updateConsultationFee(value: String) {
        _uiState.update { it.copy(consultationFee = value) }
    }

    fun validateFields(): Boolean {
        val currentState = _uiState.value
        val isValid = !currentState.patientName.isBlank() &&
                !currentState.patientAge.isBlank() &&
                !currentState.diagnosis.isBlank() &&
                !currentState.medicines.isBlank() &&
                !currentState.consultationFee.isBlank()

        _uiState.update { it.copy(showErrors = !isValid) }
        return isValid
    }

    fun savePrescription() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val currentState = _uiState.value
                val patientId = repository.savePatientWithPrescription(
                    patientName = currentState.patientName,
                    patientAge = currentState.patientAge,
                    diagnosis = currentState.diagnosis,
                    medicines = currentState.medicines,
                    consultationFee = currentState.consultationFee
                )
                
                val patientNumber = repository.getPatientById(patientId)?.patientNumber ?: ""
                
                _uiState.update { it.copy(
                    patientId = patientId,
                    patientNumber = patientNumber,
                    showBill = true
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = e.message ?: "Error saving prescription"
                ) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun getPatientDetails(patientId: Long) {
        viewModelScope.launch {
            repository.getPatientWithPrescriptions(patientId)
                .collect { patientWithPrescriptions ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            patientName = patientWithPrescriptions.patient.name,
                            patientAge = patientWithPrescriptions.patient.age,
                            // ... update other fields if needed
                        )
                    }
                }
        }
    }
} 