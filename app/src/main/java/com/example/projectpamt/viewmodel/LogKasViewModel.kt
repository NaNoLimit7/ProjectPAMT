package com.example.projectpamt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.LogInventory
import com.example.projectpamt.data.model.LogKas
import com.example.projectpamt.data.repository.LogKasRepository
import kotlinx.coroutines.launch

class LogKasViewModel : ViewModel() {
    private val repository = LogKasRepository()

    var logKasList by mutableStateOf<List<LogKas>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchLogKas()
    }

    fun fetchLogKas() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                logKasList = repository.getAllLogKas()
            } catch (e: Exception) {
                errorMessage = "Gagal menampilkan data log kas ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError(){
        errorMessage = null
    }
}