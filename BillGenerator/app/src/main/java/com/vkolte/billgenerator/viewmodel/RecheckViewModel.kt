package com.vkolte.billgenerator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vkolte.billgenerator.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RecheckUiState(
    val searchQuery: String = "",
    val patient: Patient? = null,
    val prescriptions: List<Prescription> = emptyList(),
    val error: String = ""
)

class RecheckViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PatientRepository(AppDatabase.getDatabase(application))
    private val _uiState = MutableStateFlow(RecheckUiState())
    val uiState: StateFlow<RecheckUiState> = _uiState

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun searchPatient() {
        viewModelScope.launch {
            try {
                val patientNumber = _uiState.value.searchQuery
                val patient = repository.getPatientByNumber(patientNumber)
                
                if (patient != null) {
                    repository.getPatientWithPrescriptions(patient.patientId)
                        .collect { patientWithPrescriptions ->
                            _uiState.update {
                                it.copy(
                                    patient = patientWithPrescriptions.patient,
                                    prescriptions = patientWithPrescriptions.prescriptions,
                                    error = ""
                                )
                            }
                        }
                } else {
                    _uiState.update {
                        it.copy(
                            patient = null,
                            prescriptions = emptyList(),
                            error = "Patient not found"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Error searching for patient"
                    )
                }
            }
        }
    }
} 