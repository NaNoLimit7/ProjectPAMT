package com.example.projectpamt.viewmodel.pelanggan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class AktivitasPelangganViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<AktivitasPelangganUiState>(AktivitasPelangganUiState.Idle)
    val uiState: StateFlow<AktivitasPelangganUiState> = _uiState.asStateFlow()

    private var currentCustomerId: String = ""
    private var currentFilter: AktivitasFilter = AktivitasFilter.SEMUA_WAKTU

    private val indonesianLocale = Locale.Builder().setLanguage("in").setRegion("ID").build()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm", indonesianLocale)

    // Helper to create PelangganAktivitas with dynamic date formatting
    private fun createAktivitas(
        id: String,
        dateTime: LocalDateTime,
        tipe: AktivitasType,
        total: Double,
        jumlahItem: Int
    ): PelangganAktivitas {
        return PelangganAktivitas(
            idAktivitas = id,
            tanggal = dateTime.format(dateFormatter),
            dateTime = dateTime,
            tipe = tipe,
            total = total,
            jumlahItem = jumlahItem
        )
    }

    // Generate dynamic mock database relative to current time (all are purchases/SELESAI)
    private fun getMockActivities(now: LocalDateTime): Map<String, List<PelangganAktivitas>> {
        return mapOf(
            "1" to listOf(
                createAktivitas("TRX-20231015-01", now.minusDays(2).withHour(14).withMinute(30), AktivitasType.SELESAI, 850000.0, 3),
                createAktivitas("TRX-20230928-04", now.minusDays(15).withHour(10).withMinute(15), AktivitasType.SELESAI, 1200000.0, 5),
                createAktivitas("TRX-20230915-02", now.minusDays(28).withHour(16).withMinute(45), AktivitasType.SELESAI, 400000.0, 1),
                createAktivitas("TRX-20230810-09", now.minusMonths(4).withHour(11).withMinute(20), AktivitasType.SELESAI, 500000.0, 2),
                createAktivitas("TRX-20230702-03", now.minusMonths(11).withHour(9).withMinute(15), AktivitasType.SELESAI, 300000.0, 1)
            ),
            "2" to listOf(
                createAktivitas("TRX-20231018-02", now.minusMinutes(5).withHour(15).withMinute(20), AktivitasType.SELESAI, 150000.0, 2),
                createAktivitas("TRX-20231018-01", now.minusDays(1).withHour(12).withMinute(45), AktivitasType.SELESAI, 123948.0, 1)
            ),
            "3" to listOf(
                createAktivitas("TRX-20231008-05", now.minusWeeks(1).withHour(9).withMinute(30), AktivitasType.SELESAI, 221000.0, 4)
            ),
            "4" to listOf(
                createAktivitas("TRX-20230912-01", now.minusDays(30).withHour(11).withMinute(0), AktivitasType.SELESAI, 21000.0, 1)
            )
        )
    }

    private val mockSummaries = mapOf(
        "1" to AktivitasSummary(2450000.0, 18, "2 hari lalu"),
        "2" to AktivitasSummary(273948.0, 3, "5 menit lalu"),
        "3" to AktivitasSummary(221000.0, 2, "1 minggu lalu"),
        "4" to AktivitasSummary(21000.0, 1, "30 hari lalu")
    )

    fun loadAktivitas(customerId: String) {
        currentCustomerId = customerId
        fetchAktivitas()
    }

    fun setFilter(filter: AktivitasFilter) {
        currentFilter = filter
        fetchAktivitas()
    }

    private fun fetchAktivitas() {
        viewModelScope.launch {
            _uiState.value = AktivitasPelangganUiState.Loading
            try {
                val now = LocalDateTime.now()
                val mockDb = getMockActivities(now)
                val allActivities = mockDb[currentCustomerId] ?: emptyList()
                val summary = mockSummaries[currentCustomerId] ?: AktivitasSummary(0.0, 0, "Baru saja")

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
                _uiState.value = AktivitasPelangganUiState.Error("Gagal memuat aktivitas: ${e.message}")
            }
        }
    }
}
