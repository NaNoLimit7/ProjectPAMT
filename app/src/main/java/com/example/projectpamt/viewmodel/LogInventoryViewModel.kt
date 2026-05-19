package com.example.projectpamt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.LogInventory
import com.example.projectpamt.data.repository.LogInventoryRepository
import kotlinx.coroutines.launch

class LogInventoryViewModel : ViewModel() {
    private val repository = LogInventoryRepository()

    var logInventoryList by mutableStateOf<List<LogInventory>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchLogInventory()
    }

    fun fetchLogInventory() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                logInventoryList = repository.getAllLogInventory()
            } catch (e: Exception) {
                errorMessage = "Gagal menampilkan data log inventory ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}