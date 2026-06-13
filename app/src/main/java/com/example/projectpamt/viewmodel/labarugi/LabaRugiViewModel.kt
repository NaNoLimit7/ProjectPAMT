package com.example.projectpamt.viewmodel.labarugi

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LabaRugiRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import com.example.projectpamt.viewmodel.labarugi.uistate.LabaRugiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LabaRugiViewModel(
    private val repository: LabaRugiRepository = LabaRugiRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LabaRugiUiState>(LabaRugiUiState.Idle)
    val uiState: StateFlow<LabaRugiUiState> = _uiState.asStateFlow()

    private var currentPeriod by mutableStateOf(LabaRugiPeriode.BULAN_INI)
    val selectedPeriod: LabaRugiPeriode get() = currentPeriod

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = LabaRugiUiState.Loading
            try {
                val (startDate, endDate) = getStartAndEndDates(currentPeriod)
                val state = repository.getLabaRugi(startDate, endDate)
                _uiState.value = LabaRugiUiState.Success(state, currentPeriod)
            } catch (e: Exception) {
                Log.d("LABA", e.toString())
                _uiState.value = LabaRugiUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val (startDate, endDate) = getStartAndEndDates(currentPeriod)
                val state = repository.getLabaRugi(startDate, endDate)
                _uiState.value = LabaRugiUiState.Success(state, currentPeriod)
            } catch (e: Exception) {
                Log.d("LABA", e.toString())
                _uiState.value = LabaRugiUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }

    fun setPeriod(period: LabaRugiPeriode) {
        currentPeriod = period
        loadData()
    }

    private fun getStartAndEndDates(period: LabaRugiPeriode): Pair<String, String> {
        val now = Date()
        val calStart = Calendar.getInstance()
        val calEnd = Calendar.getInstance()

        when (period) {
            LabaRugiPeriode.BULAN_INI -> {
                calStart.set(Calendar.DAY_OF_MONTH, 1)
                calStart.set(Calendar.HOUR_OF_DAY, 0)
                calStart.set(Calendar.MINUTE, 0)
                calStart.set(Calendar.SECOND, 0)
                calStart.set(Calendar.MILLISECOND, 0)

                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH))
                calEnd.set(Calendar.HOUR_OF_DAY, 23)
                calEnd.set(Calendar.MINUTE, 59)
                calEnd.set(Calendar.SECOND, 59)
                calEnd.set(Calendar.MILLISECOND, 999)
            }
            LabaRugiPeriode.BULAN_LALU -> {
                calStart.add(Calendar.MONTH, -1)
                calStart.set(Calendar.DAY_OF_MONTH, 1)
                calStart.set(Calendar.HOUR_OF_DAY, 0)
                calStart.set(Calendar.MINUTE, 0)
                calStart.set(Calendar.SECOND, 0)
                calStart.set(Calendar.MILLISECOND, 0)

                calEnd.add(Calendar.MONTH, -1)
                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH))
                calEnd.set(Calendar.HOUR_OF_DAY, 23)
                calEnd.set(Calendar.MINUTE, 59)
                calEnd.set(Calendar.SECOND, 59)
                calEnd.set(Calendar.MILLISECOND, 999)
            }
            LabaRugiPeriode.TIGA_BULAN_TERAKHIR -> {
                calStart.add(Calendar.MONTH, -3)
                calStart.set(Calendar.HOUR_OF_DAY, 0)
                calStart.set(Calendar.MINUTE, 0)
                calStart.set(Calendar.SECOND, 0)
                calStart.set(Calendar.MILLISECOND, 0)

                calEnd.set(Calendar.HOUR_OF_DAY, 23)
                calEnd.set(Calendar.MINUTE, 59)
                calEnd.set(Calendar.SECOND, 59)
                calEnd.set(Calendar.MILLISECOND, 999)
            }
            LabaRugiPeriode.TAHUN_INI -> {
                calStart.set(Calendar.MONTH, Calendar.JANUARY)
                calStart.set(Calendar.DAY_OF_MONTH, 1)
                calStart.set(Calendar.HOUR_OF_DAY, 0)
                calStart.set(Calendar.MINUTE, 0)
                calStart.set(Calendar.SECOND, 0)
                calStart.set(Calendar.MILLISECOND, 0)

                calEnd.set(Calendar.MONTH, Calendar.DECEMBER)
                calEnd.set(Calendar.DAY_OF_MONTH, 31)
                calEnd.set(Calendar.HOUR_OF_DAY, 23)
                calEnd.set(Calendar.MINUTE, 59)
                calEnd.set(Calendar.SECOND, 59)
                calEnd.set(Calendar.MILLISECOND, 999)
            }
            LabaRugiPeriode.SEMUA_WAKTU -> {
                calStart.time = Date(0) // 1970-01-01

                calEnd.set(Calendar.HOUR_OF_DAY, 23)
                calEnd.set(Calendar.MINUTE, 59)
                calEnd.set(Calendar.SECOND, 59)
                calEnd.set(Calendar.MILLISECOND, 999)
            }
        }
        return Pair(formatDateToIso(calStart.time), formatDateToIso(calEnd.time))
    }

    private fun formatDateToIso(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
        return sdf.format(date)
    }
}
