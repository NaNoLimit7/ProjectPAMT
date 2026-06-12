package com.example.projectpamt.viewmodel.produk

import androidx.lifecycle.ViewModel
import com.example.projectpamt.data.model.LogInventory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LogInventoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LogInventoryUiState>(LogInventoryUiState.Idle)
    val uiState: StateFlow<LogInventoryUiState> = _uiState.asStateFlow()

    private val allLogs = mutableListOf<LogInventory>()
    private var currentFilter = LogInventoryFilter.SEMUA_WAKTU
    private var currentSearchQuery = ""

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        _uiState.value = LogInventoryUiState.Loading

        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID"))
        val cal = Calendar.getInstance()

        // Today: Stock Addition (Stok Masuk)
        val todayStr1 = df.format(cal.time)

        // Today: Price & Name Update
        cal.add(Calendar.HOUR, -2)
        val todayStr2 = df.format(cal.time)

        // Yesterday: Stock Reduction (Terjual/Stok Keluar)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = df.format(cal.time)

        // 4 Days ago: Stock Addition
        cal.add(Calendar.DAY_OF_YEAR, -3)
        val weekStr = df.format(cal.time)

        // 12 Days ago: Stock Reduction
        cal.add(Calendar.DAY_OF_YEAR, -8)
        val monthStr = df.format(cal.time)

        allLogs.clear()
        allLogs.addAll(
            listOf(
                LogInventory(
                    idLogInventory = "LOG-8831",
                    idProduk = "1",
                    namaLama = "HP 150 Wireless Mouse",
                    hargaLama = 110000.0,
                    stokLama = 40.0,
                    stokBaru = 45.0, // +5 (Stok Masuk)
                    updatedAt = todayStr1
                ),
                LogInventory(
                    idLogInventory = "LOG-7429",
                    idProduk = "2",
                    namaLama = "Kabel USB Type-C 1m",
                    hargaLama = 40000.0, // Price updated to 45000 in product list
                    stokLama = 20.0,
                    stokBaru = 20.0, // Only price updated
                    updatedAt = todayStr2
                ),
                LogInventory(
                    idLogInventory = "LOG-5528",
                    idProduk = "1",
                    namaLama = "HP 150 Wireless Mouse",
                    hargaLama = 110000.0,
                    stokLama = 42.0,
                    stokBaru = 40.0, // -2 (Terjual/Stok Keluar)
                    updatedAt = yesterdayStr
                ),
                LogInventory(
                    idLogInventory = "LOG-1902",
                    idProduk = "3",
                    namaLama = "Fantech Gaming Headset",
                    hargaLama = 350000.0,
                    stokLama = 12.0,
                    stokBaru = 20.0, // +8 (Stok Masuk)
                    updatedAt = weekStr
                ),
                LogInventory(
                    idLogInventory = "LOG-1029",
                    idProduk = "2",
                    namaLama = "Kabel USB Type-C 1m",
                    hargaLama = 40000.0,
                    stokLama = 25.0,
                    stokBaru = 20.0, // -5 (Terjual)
                    updatedAt = monthStr
                )
            )
        )

        applyFilterAndSearch()
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
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID"))
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
                    val logDate = try { df.parse(dateStr) } catch (e: Exception) { null }
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
