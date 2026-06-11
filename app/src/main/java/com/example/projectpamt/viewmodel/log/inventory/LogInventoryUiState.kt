package com.example.projectpamt.viewmodel.log.inventory

import com.example.projectpamt.data.model.LogInventory

sealed class LogInventoryUiState {
    object Idle : LogInventoryUiState()
    object Loading : LogInventoryUiState()
    data class Success(val data: List<LogInventory>) : LogInventoryUiState()
    data class Error(val message: String) : LogInventoryUiState()
}
