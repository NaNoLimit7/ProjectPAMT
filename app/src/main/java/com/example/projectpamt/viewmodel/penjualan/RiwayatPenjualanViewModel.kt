package com.example.projectpamt.viewmodel.penjualan

import androidx.lifecycle.ViewModel
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Penjualan
import com.example.projectpamt.data.model.Produk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RiwayatPenjualanViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RiwayatPenjualanUiState>(RiwayatPenjualanUiState.Idle)
    val uiState: StateFlow<RiwayatPenjualanUiState> = _uiState.asStateFlow()

    private val allTransactions = mutableListOf<PenjualanWithDetails>()
    private var currentFilter = RiwayatFilter.SEMUA_WAKTU
    private var currentSearchQuery = ""

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        _uiState.value = RiwayatPenjualanUiState.Loading
        
        // Define some dummy products from Produk.dummyList
        val mouse = Produk.dummyList.find { it.idProduk == "1" } ?: Produk("1", "HP Mouse", 110000.0, 10.0, "pcs")
        val cable = Produk.dummyList.find { it.idProduk == "2" } ?: Produk("2", "Kabel Type-C", 45000.0, 20.0, "pcs")
        
        // Define dummy pelanggan from Pelanggan.dummyList
        val sarah = Pelanggan.dummyList.find { it.idPelanggan == "1" }
        val michael = Pelanggan.dummyList.find { it.idPelanggan == "2" }
        val emily = Pelanggan.dummyList.find { it.idPelanggan == "3" }
        
        // Define dummy kas from Kas.dummyList
        val kasUtama = Kas.dummyList.find { it.idKas == "1" }
        val kasLaci = Kas.dummyList.find { it.idKas == "2" }
        
        // Get date instances for today, yesterday, and older
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID"))
        val cal = Calendar.getInstance()
        
        // Today transaction 1
        val todayStr1 = df.format(cal.time)
        
        // Today transaction 2
        cal.add(Calendar.HOUR, -3)
        val todayStr2 = df.format(cal.time)
        
        // Yesterday transaction
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = df.format(cal.time)
        
        // 4 days ago transaction
        cal.add(Calendar.DAY_OF_YEAR, -3)
        val weekStr = df.format(cal.time)
        
        // 15 days ago transaction
        cal.add(Calendar.DAY_OF_YEAR, -11)
        val monthStr = df.format(cal.time)

        allTransactions.clear()
        allTransactions.addAll(
            listOf(
                PenjualanWithDetails(
                    penjualan = Penjualan(
                        idPenjualan = "TXN-8742",
                        idPelanggan = sarah?.idPelanggan ?: "1",
                        idKas = kasUtama?.idKas ?: "1",
                        jumlahBayar = 300000.0,
                        totalHarga = 265000.0,
                        createdAt = todayStr1
                    ),
                    pelanggan = sarah,
                    kas = kasUtama,
                    items = listOf(
                        CartItem(mouse, 2), // 220.000
                        CartItem(cable, 1)  // 45.000
                    )
                ),
                PenjualanWithDetails(
                    penjualan = Penjualan(
                        idPenjualan = "TXN-4391",
                        idPelanggan = michael?.idPelanggan ?: "2",
                        idKas = kasLaci?.idKas ?: "2",
                        jumlahBayar = 50000.0,
                        totalHarga = 45000.0,
                        createdAt = todayStr2
                    ),
                    pelanggan = michael,
                    kas = kasLaci,
                    items = listOf(
                        CartItem(cable, 1) // 45.000
                    )
                ),
                PenjualanWithDetails(
                    penjualan = Penjualan(
                        idPenjualan = "TXN-3298",
                        idPelanggan = "", // Walk-in customer (Umum)
                        idKas = kasUtama?.idKas ?: "1",
                        jumlahBayar = 150000.0,
                        totalHarga = 110000.0,
                        createdAt = yesterdayStr
                    ),
                    pelanggan = null,
                    kas = kasUtama,
                    items = listOf(
                        CartItem(mouse, 1) // 110.000
                    )
                ),
                PenjualanWithDetails(
                    penjualan = Penjualan(
                        idPenjualan = "TXN-1082",
                        idPelanggan = emily?.idPelanggan ?: "3",
                        idKas = kasUtama?.idKas ?: "1",
                        jumlahBayar = 500000.0,
                        totalHarga = 485000.0,
                        createdAt = weekStr
                    ),
                    pelanggan = emily,
                    kas = kasUtama,
                    items = listOf(
                        CartItem(mouse, 4), // 440.000
                        CartItem(cable, 1)  // 45.000
                    )
                ),
                PenjualanWithDetails(
                    penjualan = Penjualan(
                        idPenjualan = "TXN-9081",
                        idPelanggan = sarah?.idPelanggan ?: "1",
                        idKas = kasLaci?.idKas ?: "2",
                        jumlahBayar = 200000.0,
                        totalHarga = 155000.0,
                        createdAt = monthStr
                    ),
                    pelanggan = sarah,
                    kas = kasLaci,
                    items = listOf(
                        CartItem(mouse, 1), // 110.000
                        CartItem(cable, 1)  // 45.000
                    )
                )
            )
        )
        
        applyFilterAndSearch()
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
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID"))
        val now = Date()
        
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
                    val txnDate = try { df.parse(dateStr) } catch(e: Exception) { null }
                    if (txnDate == null) {
                        false
                    } else {
                        when (currentFilter) {
                            RiwayatFilter.HARI_INI -> {
                                isSameDay(txnDate, now)
                            }
                            RiwayatFilter.MINGGU_INI -> {
                                // within last 7 days
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
