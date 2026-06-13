package com.example.projectpamt.viewmodel.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.KasRepository
import com.example.projectpamt.data.repository.PelangganRepository
import com.example.projectpamt.data.repository.PenjualanRepository
import com.example.projectpamt.data.repository.ProdukRepository
import com.example.projectpamt.ui.screens.home.dashboard.DashboardState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import com.example.projectpamt.ui.utils.DateTimeUtils

sealed class DashboardUiState {
    abstract val message: String?

    object Idle : DashboardUiState() {
        override val message: String? = null
    }
    object Loading : DashboardUiState() {
        override val message: String? = null
    }
    data class Success(val state: DashboardState) : DashboardUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : DashboardUiState()
}

class DashboardViewModel(
    private val penjualanRepository: PenjualanRepository = PenjualanRepository(),
    private val produkRepository: ProdukRepository = ProdukRepository(),
    private val pelangganRepository: PelangganRepository = PelangganRepository(),
    private val kasRepository: KasRepository = KasRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    fun fetchDashboardData(namaPengguna: String) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                // Fetch in parallel
                val totalProdukDeferred = async { produkRepository.getProdukAktif().size }
                val kasDeferred = async { kasRepository.getKasAktif() }
                val pelangganDeferred = async { pelangganRepository.getAllPelanggan() }
                val riwayatPenjualanDeferred = async { penjualanRepository.getRiwayatPenjualan() }

                val totalProduk = totalProdukDeferred.await()
                val kasList = kasDeferred.await()
                val pelangganList = pelangganDeferred.await()
                val riwayatPenjualan = riwayatPenjualanDeferred.await()

                val saldoKas = kasList.sumOf { it.saldo }
                val jumlahKasAktif = kasList.size

                val totalPelanggan = pelangganList.size
                val pelangganAktif = pelangganList.count { it.aktif }

                // Calculate monthly sales
                val now = Calendar.getInstance()
                val currentMonth = now.get(Calendar.MONTH)
                val currentYear = now.get(Calendar.YEAR)

                val penjualanBulanIni = riwayatPenjualan.filter { penjualan ->
                    val dateStr = penjualan.createdAt
                    if (dateStr == null) {
                        false
                    } else {
                        val date = DateTimeUtils.parseIso(dateStr)
                        if (date == null) {
                            false
                        } else {
                            val cal = Calendar.getInstance().apply { time = date }
                            cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear
                        }
                    }
                }.sumOf { it.totalHarga }

                // Calculate weekly sales (last 7 days grouped by day of week)
                val dayNames = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
                val weeklyMap = mutableMapOf<String, Double>()
                
                // Initialize map with days in order of last 7 days ending today
                val orderOfDays = mutableListOf<String>()
                val tempCal = Calendar.getInstance()
                for (i in 0..6) {
                    val dayName = dayNames[tempCal.get(Calendar.DAY_OF_WEEK) - 1]
                    orderOfDays.add(0, dayName)
                    weeklyMap[dayName] = 0.0
                    tempCal.add(Calendar.DAY_OF_YEAR, -1)
                }

                val sevenDaysAgo = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -7)
                }.time

                riwayatPenjualan.forEach { penjualan ->
                    val dateStr = penjualan.createdAt
                    if (dateStr != null) {
                        val date = DateTimeUtils.parseIso(dateStr)
                        if (date != null && date.after(sevenDaysAgo)) {
                            val cal = Calendar.getInstance().apply { time = date }
                            val dayName = dayNames[cal.get(Calendar.DAY_OF_WEEK) - 1]
                            weeklyMap[dayName] = (weeklyMap[dayName] ?: 0.0) + penjualan.totalHarga
                        }
                    }
                }

                // Map to list of pairs in the correct chronological order
                val penjualanMingguIni = orderOfDays.map { day ->
                    day to (weeklyMap[day] ?: 0.0)
                }

                _uiState.value = DashboardUiState.Success(
                    DashboardState(
                        penjualanBulanIni = penjualanBulanIni,
                        totalProduk = totalProduk,
                        saldoKas = saldoKas,
                        jumlahKasAktif = jumlahKasAktif,
                        totalPelanggan = totalPelanggan,
                        pelangganAktif = pelangganAktif,
                        penjualanMingguIni = penjualanMingguIni,
                        namaPengguna = namaPengguna
                    )
                )
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh(namaPengguna: String) {
        viewModelScope.launch {
            isRefreshing = true
            try {
                // Fetch in parallel
                val totalProdukDeferred = async { produkRepository.getProdukAktif().size }
                val kasDeferred = async { kasRepository.getKasAktif() }
                val pelangganDeferred = async { pelangganRepository.getAllPelanggan() }
                val riwayatPenjualanDeferred = async { penjualanRepository.getRiwayatPenjualan() }

                val totalProduk = totalProdukDeferred.await()
                val kasList = kasDeferred.await()
                val pelangganList = pelangganDeferred.await()
                val riwayatPenjualan = riwayatPenjualanDeferred.await()

                val saldoKas = kasList.sumOf { it.saldo }
                val jumlahKasAktif = kasList.size

                val totalPelanggan = pelangganList.size
                val pelangganAktif = pelangganList.count { it.aktif }

                val now = Calendar.getInstance()
                val currentMonth = now.get(Calendar.MONTH)
                val currentYear = now.get(Calendar.YEAR)

                val penjualanBulanIni = riwayatPenjualan.filter { penjualan ->
                    val dateStr = penjualan.createdAt
                    if (dateStr == null) {
                        false
                    } else {
                        val date = DateTimeUtils.parseIso(dateStr)
                        if (date == null) {
                            false
                        } else {
                            val cal = Calendar.getInstance().apply { time = date }
                            cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear
                        }
                    }
                }.sumOf { it.totalHarga }

                val dayNames = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
                val weeklyMap = mutableMapOf<String, Double>()
                
                val orderOfDays = mutableListOf<String>()
                val tempCal = Calendar.getInstance()
                for (i in 0..6) {
                    val dayName = dayNames[tempCal.get(Calendar.DAY_OF_WEEK) - 1]
                    orderOfDays.add(0, dayName)
                    weeklyMap[dayName] = 0.0
                    tempCal.add(Calendar.DAY_OF_YEAR, -1)
                }

                val sevenDaysAgo = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -7)
                }.time

                riwayatPenjualan.forEach { penjualan ->
                    val dateStr = penjualan.createdAt
                    if (dateStr != null) {
                        val date = DateTimeUtils.parseIso(dateStr)
                        if (date != null && date.after(sevenDaysAgo)) {
                            val cal = Calendar.getInstance().apply { time = date }
                            val dayName = dayNames[cal.get(Calendar.DAY_OF_WEEK) - 1]
                            weeklyMap[dayName] = (weeklyMap[dayName] ?: 0.0) + penjualan.totalHarga
                        }
                    }
                }

                val penjualanMingguIni = orderOfDays.map { day ->
                    day to (weeklyMap[day] ?: 0.0)
                }

                _uiState.value = DashboardUiState.Success(
                    DashboardState(
                        penjualanBulanIni = penjualanBulanIni,
                        totalProduk = totalProduk,
                        saldoKas = saldoKas,
                        jumlahKasAktif = jumlahKasAktif,
                        totalPelanggan = totalPelanggan,
                        pelangganAktif = pelangganAktif,
                        penjualanMingguIni = penjualanMingguIni,
                        namaPengguna = namaPengguna
                    )
                )
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }
}
