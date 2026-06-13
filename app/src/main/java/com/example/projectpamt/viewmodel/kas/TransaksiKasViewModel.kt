package com.example.projectpamt.viewmodel.kas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogKasRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransaksiKasViewModel(
    private val repository: LogKasRepository = LogKasRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransaksiKasUiState>(TransaksiKasUiState.Idle)
    val uiState: StateFlow<TransaksiKasUiState> = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    fun fetchTransaksiKas(idKas: String) {
        viewModelScope.launch {
            _uiState.value = TransaksiKasUiState.Loading
            try {
                val transactions = repository.getTransaksiKas(idKas)
                _uiState.value = TransaksiKasUiState.Success(transactions)
            } catch (e: Exception) {
                _uiState.value = TransaksiKasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh(idKas: String) {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val transactions = repository.getTransaksiKas(idKas)
                _uiState.value = TransaksiKasUiState.Success(transactions)
            } catch (e: Exception) {
                _uiState.value = TransaksiKasUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }
}
