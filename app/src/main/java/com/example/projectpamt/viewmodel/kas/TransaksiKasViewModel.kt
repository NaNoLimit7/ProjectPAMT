package com.example.projectpamt.viewmodel.kas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.LogKasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransaksiKasViewModel(
    private val repository: LogKasRepository = LogKasRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransaksiKasUiState>(TransaksiKasUiState.Idle)
    val uiState: StateFlow<TransaksiKasUiState> = _uiState.asStateFlow()

    fun fetchTransaksiKas(idKas: String) {
        viewModelScope.launch {
            _uiState.value = TransaksiKasUiState.Loading
            try {
                val transactions = repository.getTransaksiKas(idKas)
                _uiState.value = TransaksiKasUiState.Success(transactions)
            } catch (e: Exception) {
                _uiState.value = TransaksiKasUiState.Error(
                    e.message ?: "Gagal memuat riwayat transaksi kas"
                )
            }
        }
    }
}
