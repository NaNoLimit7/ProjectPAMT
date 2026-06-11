package com.example.projectpamt.viewmodel.log.pelanggan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogPelangganRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogPelangganViewModel : ViewModel() {
    private val repository = LogPelangganRepository()

    private val _uiState = MutableStateFlow<LogPelangganUiState>(LogPelangganUiState.Idle)
    val uiState: StateFlow<LogPelangganUiState> = _uiState.asStateFlow()

    init {
        fetchLog()
    }

    fun fetchLog() {
        viewModelScope.launch {
            _uiState.value = LogPelangganUiState.Loading
            try {
                val result = repository.getAllLogPelanggan()
                _uiState.value = LogPelangganUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = LogPelangganUiState.Error("Gagal menampilakan data log pelanggan ${e.message}")
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is LogPelangganUiState.Error) {
            _uiState.value = LogPelangganUiState.Idle
            fetchLog()
        }
    }
}