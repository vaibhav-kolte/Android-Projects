package com.vkolte.billgenerater.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PrescriptionViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(PrescriptionUiState())
    val uiState: StateFlow<PrescriptionUiState> = _uiState

    fun updatePatientName(name: String) {
        _uiState.update { it.copy(patientName = name, showErrors = false) }
    }

    fun updatePatientAge(age: String) {
        _uiState.update { it.copy(patientAge = age, showErrors = false) }
    }

    fun updateDiagnosis(diagnosis: String) {
        _uiState.update { it.copy(diagnosis = diagnosis, showErrors = false) }
    }

    fun updateMedicines(medicines: String) {
        _uiState.update { it.copy(medicines = medicines, showErrors = false) }
    }

    fun updateConsultationFee(fee: String) {
        _uiState.update { it.copy(consultationFee = fee, showErrors = false) }
    }

    fun validateFields(): Boolean {
        val currentState = _uiState.value
        
        val errorMessage = when {
            currentState.patientName.isBlank() -> "Please enter patient name"
            currentState.patientAge.isBlank() -> "Please enter patient age"
            currentState.diagnosis.isBlank() -> "Please enter diagnosis"
            currentState.medicines.isBlank() -> "Please enter prescribed medicines"
            currentState.consultationFee.isBlank() -> "Please enter consultation fee"
            else -> ""
        }

        _uiState.update { 
            it.copy(
                showErrors = errorMessage.isNotEmpty(),
                errorMessage = errorMessage
            )
        }

        return errorMessage.isEmpty()
    }

    fun showBill() {
        _uiState.update { it.copy(showBill = true) }
    }
}

data class PrescriptionUiState(
    val patientName: String = "",
    val patientAge: String = "",
    val diagnosis: String = "",
    val medicines: String = "",
    val consultationFee: String = "",
    val showErrors: Boolean = false,
    val errorMessage: String = "",
    val showBill: Boolean = false
) 