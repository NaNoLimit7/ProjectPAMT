package com.example.projectpamt.viewmodel.kas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogTotalKasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogTotalKasViewModel(
    private val repository: LogTotalKasRepository = LogTotalKasRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LogTotalKasUiState>(LogTotalKasUiState.Idle)
    val uiState: StateFlow<LogTotalKasUiState> = _uiState.asStateFlow()

    init {
        fetchLogTotalKas()
    }

    fun fetchLogTotalKas() {
        viewModelScope.launch {
            _uiState.value = LogTotalKasUiState.Loading
            try {
                val summary = repository.getLogTotalKasSummary()
                _uiState.value = LogTotalKasUiState.Success(summary)
            } catch (e: Exception) {
                _uiState.value = LogTotalKasUiState.Error(
                    e.message ?: "Gagal memuat log total kas"
                )
            }
        }
    }
}
