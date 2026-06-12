package com.example.projectpamt.viewmodel.kas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogKasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogKasViewModel(
    private val repository: LogKasRepository = LogKasRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LogKasUiState>(LogKasUiState.Idle)
    val uiState: StateFlow<LogKasUiState> = _uiState.asStateFlow()

    fun fetchLogKas(idKas: String) {
        viewModelScope.launch {
            _uiState.value = LogKasUiState.Loading
            try {
                val logs = repository.getLogKas(idKas)
                _uiState.value = LogKasUiState.Success(logs)
            } catch (e: Exception) {
                _uiState.value = LogKasUiState.Error(
                    e.message ?: "Gagal memuat riwayat log kas"
                )
            }
        }
    }
}
