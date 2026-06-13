package com.example.projectpamt.viewmodel.kas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogTotalKasRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogTotalKasViewModel(
    private val repository: LogTotalKasRepository = LogTotalKasRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LogTotalKasUiState>(LogTotalKasUiState.Idle)
    val uiState: StateFlow<LogTotalKasUiState> = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

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
                _uiState.value = LogTotalKasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val summary = repository.getLogTotalKasSummary()
                _uiState.value = LogTotalKasUiState.Success(summary)
            } catch (e: Exception) {
                _uiState.value = LogTotalKasUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }
}
