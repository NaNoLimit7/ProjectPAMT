package com.example.projectpamt.viewmodel.produk

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.LogInventory
import com.example.projectpamt.data.repository.LogInventoryRepository
import com.example.projectpamt.ui.utils.DateTimeUtils
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class LogInventoryViewModel(
    private val repository: LogInventoryRepository = LogInventoryRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<LogInventoryUiState>(LogInventoryUiState.Idle)
    val uiState: StateFlow<LogInventoryUiState> = _uiState.asStateFlow()

    private val allLogs = mutableListOf<LogInventory>()
    private var currentFilter = LogInventoryFilter.SEMUA_WAKTU
    private var currentSearchQuery = ""

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        fetchLogs()
    }

    fun fetchLogs() {
        viewModelScope.launch {
            _uiState.value = LogInventoryUiState.Loading
            try {
                val logs = repository.getAllLogInventory()
                allLogs.clear()
                allLogs.addAll(logs)
                applyFilterAndSearch()
            } catch (e: Exception) {
                _uiState.value = LogInventoryUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val logs = repository.getAllLogInventory()
                allLogs.clear()
                allLogs.addAll(logs)
                applyFilterAndSearch()
            } catch (e: Exception) {
                _uiState.value = LogInventoryUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }

    fun setFilter(filter: LogInventoryFilter) {
        currentFilter = filter
        applyFilterAndSearch()
    }

    fun setSearchQuery(query: String) {
        currentSearchQuery = query
        applyFilterAndSearch()
    }

    private fun applyFilterAndSearch() {
        val now = Date()

        val filtered = allLogs.filter { item ->
            // 1. Search Query Filter
            val matchesQuery = if (currentSearchQuery.isEmpty()) {
                true
            } else {
                item.namaLama.contains(currentSearchQuery, ignoreCase = true)
            }

            // 2. Date Filter
            val matchesDate = if (currentFilter == LogInventoryFilter.SEMUA_WAKTU) {
                true
            } else {
                val dateStr = item.updatedAt
                if (dateStr == null) {
                    false
                } else {
                    val logDate = DateTimeUtils.parseIso(dateStr)
                    if (logDate == null) {
                        false
                    } else {
                        when (currentFilter) {
                            LogInventoryFilter.HARI_INI -> {
                                isSameDay(logDate, now)
                            }
                            LogInventoryFilter.MINGGU_INI -> {
                                val diff = now.time - logDate.time
                                diff >= 0 && diff <= 7L * 24 * 60 * 60 * 1000
                            }
                            LogInventoryFilter.BULAN_INI -> {
                                isSameMonth(logDate, now)
                            }
                            else -> true
                        }
                    }
                }
            }

            matchesQuery && matchesDate
        }

        _uiState.value = LogInventoryUiState.Success(
            listLogs = filtered,
            searchQuery = currentSearchQuery,
            selectedFilter = currentFilter
        )
    }

    private fun isSameDay(d1: Date, d2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = d1 }
        val cal2 = Calendar.getInstance().apply { time = d2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isSameMonth(d1: Date, d2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = d1 }
        val cal2 = Calendar.getInstance().apply { time = d2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
    }
}
