package com.example.projectpamt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.LogPelanggan
import com.example.projectpamt.data.repository.LogPelangganRepository
import kotlinx.coroutines.launch

class LogPelangganViewModel : ViewModel() {
    private val repository = LogPelangganRepository()

    var logPelangganList by mutableStateOf<List<LogPelanggan>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchLog()
    }

    fun fetchLog() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                logPelangganList = repository.getAllLogPelanggan()
            } catch (e: Exception) {
                errorMessage = "Gagal menampilakan data log pelanggan ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}