package com.example.projectpamt.viewmodel.log.kas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogKasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogKasViewModel : ViewModel() {
    private val repository = LogKasRepository()

    private val _uiState = MutableStateFlow<LogKasUiState>(LogKasUiState.Idle)
    val uiState: StateFlow<LogKasUiState> = _uiState.asStateFlow()

    init {
        fetchLogKas()
    }

    fun fetchLogKas() {
        viewModelScope.launch {
            _uiState.value = LogKasUiState.Loading
            try {
                val result = repository.getAllLogKas()
                _uiState.value = LogKasUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = LogKasUiState.Error("Gagal menampilkan data log kas ${e.message}")
            }
        }
    }

    fun clearUiState(){
        if (_uiState.value is LogKasUiState.Error) {
            _uiState.value = LogKasUiState.Idle
            fetchLogKas()
        }
    }
}