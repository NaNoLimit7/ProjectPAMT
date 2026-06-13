package com.example.projectpamt.viewmodel.penjualan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.PenjualanRepository
import com.example.projectpamt.ui.utils.DateTimeUtils
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import com.example.projectpamt.viewmodel.penjualan.uistate.CartItem
import com.example.projectpamt.viewmodel.penjualan.uistate.PenjualanWithDetails
import com.example.projectpamt.viewmodel.penjualan.uistate.RiwayatFilter
import com.example.projectpamt.viewmodel.penjualan.uistate.RiwayatPenjualanUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.util.Calendar
import java.util.Date

class RiwayatPenjualanViewModel(
    private val repository: PenjualanRepository = PenjualanRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<RiwayatPenjualanUiState>(RiwayatPenjualanUiState.Idle)
    val uiState: StateFlow<RiwayatPenjualanUiState> = _uiState.asStateFlow()

    private val allTransactions = mutableListOf<PenjualanWithDetails>()
    private var currentFilter = RiwayatFilter.SEMUA_WAKTU
    private var currentSearchQuery = ""

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        fetchRiwayat()
    }

    fun fetchRiwayat() {
        viewModelScope.launch {
            _uiState.value = RiwayatPenjualanUiState.Loading
            try {
                val rawPenjualan = repository.getRiwayatPenjualan()
                val mapped = rawPenjualan.map { penjualan ->
                    val items = try {
                        if (penjualan.detailPenjualan != null) {
                            Json.decodeFromJsonElement<List<CartItem>>(penjualan.detailPenjualan)
                        } else {
                            emptyList()
                        }
                    } catch (e: Exception) {
                        emptyList()
                    }
                    PenjualanWithDetails(
                        penjualan = penjualan,
                        pelanggan = penjualan.pelanggan,
                        kas = penjualan.kas,
                        items = items
                    )
                }
                allTransactions.clear()
                allTransactions.addAll(mapped)
                applyFilterAndSearch()
            } catch (e: Exception) {
                _uiState.value = RiwayatPenjualanUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val rawPenjualan = repository.getRiwayatPenjualan()
                val mapped = rawPenjualan.map { penjualan ->
                    val items = try {
                        if (penjualan.detailPenjualan != null) {
                            Json.decodeFromJsonElement<List<CartItem>>(penjualan.detailPenjualan)
                        } else {
                            emptyList()
                        }
                    } catch (e: Exception) {
                        emptyList()
                    }
                    PenjualanWithDetails(
                        penjualan = penjualan,
                        pelanggan = penjualan.pelanggan,
                        kas = penjualan.kas,
                        items = items
                    )
                }
                allTransactions.clear()
                allTransactions.addAll(mapped)
                applyFilterAndSearch()
            } catch (e: Exception) {
                _uiState.value = RiwayatPenjualanUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }

    fun setFilter(filter: RiwayatFilter) {
        currentFilter = filter
        applyFilterAndSearch()
    }

    fun setSearchQuery(query: String) {
        currentSearchQuery = query
        applyFilterAndSearch()
    }

    private fun applyFilterAndSearch() {
        val filtered = allTransactions.filter { item ->
            // 1. Search Query Filter
            val matchesQuery = if (currentSearchQuery.isEmpty()) {
                true
            } else {
                val txnId = item.penjualan.idPenjualan ?: ""
                val customerName = item.pelanggan?.nama ?: "Umum"
                txnId.contains(currentSearchQuery, ignoreCase = true) ||
                        customerName.contains(currentSearchQuery, ignoreCase = true)
            }
            
            // 2. Date Filter
            val matchesDate = if (currentFilter == RiwayatFilter.SEMUA_WAKTU) {
                true
            } else {
                val dateStr = item.penjualan.createdAt
                if (dateStr == null) {
                    false
                } else {
                    val txnDate = DateTimeUtils.parseIso(dateStr)
                    
                    if (txnDate == null) {
                        false
                    } else {
                        val now = Date()
                        when (currentFilter) {
                            RiwayatFilter.HARI_INI -> {
                                isSameDay(txnDate, now)
                            }
                            RiwayatFilter.MINGGU_INI -> {
                                val diff = now.time - txnDate.time
                                diff >= 0 && diff <= 7L * 24 * 60 * 60 * 1000
                            }
                            RiwayatFilter.BULAN_INI -> {
                                isSameMonth(txnDate, now)
                            }
                            else -> true
                        }
                    }
                }
            }
            
            matchesQuery && matchesDate
        }

        val totalPendapatan = filtered.sumOf { it.penjualan.totalHarga }
        val totalTransaksi = filtered.size

        _uiState.value = RiwayatPenjualanUiState.Success(
            listPenjualan = filtered,
            totalPendapatan = totalPendapatan,
            totalTransaksi = totalTransaksi,
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
