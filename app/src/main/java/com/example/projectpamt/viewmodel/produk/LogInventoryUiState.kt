package com.example.projectpamt.viewmodel.produk

import com.example.projectpamt.data.model.LogInventory

enum class LogInventoryFilter {
    SEMUA_WAKTU,
    HARI_INI,
    MINGGU_INI,
    BULAN_INI
}

sealed class LogInventoryUiState {
    object Idle : LogInventoryUiState()
    object Loading : LogInventoryUiState()
    data class Success(
        val listLogs: List<LogInventory>,
        val searchQuery: String,
        val selectedFilter: LogInventoryFilter
    ) : LogInventoryUiState()
    data class Error(val message: String) : LogInventoryUiState()
}
