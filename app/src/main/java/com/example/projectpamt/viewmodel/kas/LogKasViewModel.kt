package com.example.projectpamt.viewmodel.kas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogKasRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import com.example.projectpamt.viewmodel.kas.uistate.LogKasUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogKasViewModel(
    private val repository: LogKasRepository = LogKasRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LogKasUiState>(LogKasUiState.Idle)
    val uiState: StateFlow<LogKasUiState> = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    fun fetchLogKas(idKas: String) {
        viewModelScope.launch {
            _uiState.value = LogKasUiState.Loading
            try {
                val logs = repository.getLogKas(idKas)
                _uiState.value = LogKasUiState.Success(logs)
            } catch (e: Exception) {
                _uiState.value = LogKasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh(idKas: String) {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val logs = repository.getLogKas(idKas)
                _uiState.value = LogKasUiState.Success(logs)
            } catch (e: Exception) {
                _uiState.value = LogKasUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }
}
