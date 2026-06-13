package com.example.projectpamt.viewmodel.pelanggan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.PenjualanRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import com.example.projectpamt.viewmodel.pelanggan.uistate.AktivitasFilter
import com.example.projectpamt.viewmodel.pelanggan.uistate.AktivitasPelangganUiState
import com.example.projectpamt.viewmodel.pelanggan.uistate.AktivitasSummary
import com.example.projectpamt.viewmodel.pelanggan.uistate.AktivitasType
import com.example.projectpamt.viewmodel.pelanggan.uistate.PelangganAktivitas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AktivitasPelangganViewModel(
    private val penjualanRepository: PenjualanRepository = PenjualanRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<AktivitasPelangganUiState>(AktivitasPelangganUiState.Idle)
    val uiState: StateFlow<AktivitasPelangganUiState> = _uiState.asStateFlow()

    private var currentCustomerId: String = ""
    private var currentFilter: AktivitasFilter = AktivitasFilter.SEMUA_WAKTU

    private val indonesianLocale = Locale.Builder().setLanguage("in").setRegion("ID").build()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm", indonesianLocale)

    var isRefreshing by mutableStateOf(false)
        private set

    fun loadAktivitas(customerId: String) {
        currentCustomerId = customerId
        fetchAktivitas()
    }

    fun setFilter(filter: AktivitasFilter) {
        currentFilter = filter
        fetchAktivitas()
    }

    private fun fetchAktivitas() {
        fetchAktivitasInternal(showLoading = true)
    }

    fun refresh() {
        if (currentCustomerId.isNotEmpty()) {
            fetchAktivitasInternal(showLoading = false, isRefresh = true)
        }
    }

    private fun fetchAktivitasInternal(showLoading: Boolean, isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = AktivitasPelangganUiState.Loading
            }
            if (isRefresh) {
                isRefreshing = true
            }
            try {
                // Fetch real transactions for the customer
                val transactions = penjualanRepository.getPenjualanByPelanggan(currentCustomerId)

                val now = LocalDateTime.now()

                // Map Penjualan to PelangganAktivitas
                val allActivities = transactions.map { penjualan ->
                    val dateTime = try {
                        val zdt = ZonedDateTime.parse(penjualan.createdAt)
                        zdt.toLocalDateTime()
                    } catch (_: Exception) {
                        LocalDateTime.now()
                    }

                    // Count items from detailPenjualan JSON array
                    var totalQuantity = 0
                    val details = penjualan.detailPenjualan
                    if (details is JsonArray) {
                        for (element in details) {
                            try {
                                val obj = element.jsonObject
                                val qty = obj["kuantitas"]?.jsonPrimitive?.doubleOrNull
                                    ?: obj["quantity"]?.jsonPrimitive?.doubleOrNull
                                    ?: 1.0
                                totalQuantity += qty.toInt()
                            } catch (_: Exception) {
                                totalQuantity += 1
                            }
                        }
                    }

                    PelangganAktivitas(
                        idAktivitas = "TRX-${penjualan.idPenjualan?.take(8)?.uppercase() ?: ""}",
                        tanggal = dateTime.format(dateFormatter),
                        dateTime = dateTime,
                        tipe = AktivitasType.SELESAI,
                        total = penjualan.totalHarga,
                        jumlahItem = if (totalQuantity > 0) totalQuantity else 1
                    )
                }.sortedByDescending { it.dateTime }

                // Calculate Summary
                val totalBelanja = allActivities.sumOf { it.total }
                val totalTransaksi = allActivities.size

                // Calculate "terakhir aktif"
                val terakhirAktif = if (allActivities.isNotEmpty()) {
                    val lastDate = allActivities.first().tanggal
                    lastDate
                } else {
                    "Belum ada transaksi"
                }

                val summary = AktivitasSummary(
                    totalBelanja = totalBelanja,
                    totalTransaksi = totalTransaksi,
                    terakhirAktif = terakhirAktif
                )

                // Filter aktivitas secara dinamis berbasis tanggal hari ini
                val filteredActivities = when (currentFilter) {
                    AktivitasFilter.SEMUA_WAKTU -> allActivities
                    AktivitasFilter.BULAN_INI -> {
                        allActivities.filter {
                            it.dateTime.year == now.year && it.dateTime.month == now.month
                        }
                    }

                    AktivitasFilter.TIGA_BULAN_TERAKHIR -> {
                        val limitDate = now.toLocalDate().withDayOfMonth(1).minusMonths(2)
                        allActivities.filter {
                            val activityDate = it.dateTime.toLocalDate()
                            activityDate.isAfter(limitDate) || activityDate.isEqual(limitDate)
                        }
                    }

                    AktivitasFilter.TAHUN_INI -> {
                        allActivities.filter {
                            it.dateTime.year == now.year
                        }
                    }
                }

                _uiState.value = AktivitasPelangganUiState.Success(
                    summary = summary,
                    listAktivitas = filteredActivities,
                    selectedFilter = currentFilter
                )
            } catch (e: Exception) {
                _uiState.value = AktivitasPelangganUiState.Error(e.toAppError().toUserMessage())
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
            }
        }
    }
}
