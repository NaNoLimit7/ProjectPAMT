package com.example.projectpamt.viewmodel.produk

import com.example.projectpamt.data.model.LogInventory

enum class LogInventoryFilter {
    SEMUA_WAKTU,
    HARI_INI,
    MINGGU_INI,
    BULAN_INI
}

sealed class LogInventoryUiState {
    abstract val message: String?

    object Idle : LogInventoryUiState() {
        override val message: String? = null
    }
    object Loading : LogInventoryUiState() {
        override val message: String? = null
    }
    data class Success(
        val listLogs: List<LogInventory>,
        val searchQuery: String,
        val selectedFilter: LogInventoryFilter
    ) : LogInventoryUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : LogInventoryUiState()
}
