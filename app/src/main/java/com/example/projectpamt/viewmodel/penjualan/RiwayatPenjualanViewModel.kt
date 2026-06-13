package com.example.projectpamt.viewmodel.penjualan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Penjualan
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.data.repository.PenjualanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RiwayatPenjualanViewModel(
    private val repository: PenjualanRepository = PenjualanRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<RiwayatPenjualanUiState>(RiwayatPenjualanUiState.Idle)
    val uiState: StateFlow<RiwayatPenjualanUiState> = _uiState.asStateFlow()

    private val allTransactions = mutableListOf<PenjualanWithDetails>()
    private var currentFilter = RiwayatFilter.SEMUA_WAKTU
    private var currentSearchQuery = ""

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
                _uiState.value = RiwayatPenjualanUiState.Error("Gagal memuat riwayat: ${e.message}")
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
                    val txnInstant = try {
                        java.time.Instant.parse(dateStr)
                    } catch (e: Exception) {
                        try {
                            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID"))
                            df.parse(dateStr)?.toInstant()
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    
                    if (txnInstant == null) {
                        false
                    } else {
                        val txnDate = Date.from(txnInstant)
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
