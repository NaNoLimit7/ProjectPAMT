package com.example.projectpamt.viewmodel.log.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogInventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogInventoryViewModel : ViewModel() {
    private val repository = LogInventoryRepository()

    private val _uiState = MutableStateFlow<LogInventoryUiState>(LogInventoryUiState.Idle)
    val uiState: StateFlow<LogInventoryUiState> = _uiState.asStateFlow()

    init {
        fetchLogInventory()
    }

    fun fetchLogInventory() {
        viewModelScope.launch {
            _uiState.value = LogInventoryUiState.Loading
            try {
                val result = repository.getAllLogInventory()
                _uiState.value = LogInventoryUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = LogInventoryUiState.Error("Gagal menampilkan data log inventory ${e.message}")
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is LogInventoryUiState.Error) {
            _uiState.value = LogInventoryUiState.Idle
            fetchLogInventory()
        }
    }
}