package com.example.projectpamt.viewmodel.kas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.repository.KasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KasViewModel(
    private val repository: KasRepository = KasRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<KasUiState>(KasUiState.Idle)
    val uiState: StateFlow<KasUiState> = _uiState.asStateFlow()

    init {
        fetchAllActiveKas()
    }

    fun fetchAllActiveKas() {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                val result = repository.getKasAktif()
                _uiState.value = KasUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal menampilkan kas aktif")
            }
        }
    }

    fun addKas(nama: String, saldo: Double) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                val kasBaru = Kas(nama = nama, saldo = saldo)
                repository.tambahKas(kasBaru)
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal menambahkan kas baru")
            }
        }
    }

    fun updateNamaKas(id: String, nama: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                repository.updateNamaKas(id, nama)
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal mengubah nama kas")
            }
        }
    }

    fun updateSaldo(id: String, saldo: Double, keterangan: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                repository.updateSaldo(
                    idKas = id,
                    perubahanSaldo = saldo,
                    keterangan = keterangan
                )
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal merubah saldo kas")
            }
        }
    }

    fun nonaktifkanKas(id: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                repository.nonaktifkanKas(idKas = id)
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal menonaktifkan kas")
            }
        }
    }

    fun clearUiState() {
        // Option to reset to Idle or keep current Success state
        // For simple error clearing, we can just fetch data again or go Idle
        if (_uiState.value is KasUiState.Error) {
            _uiState.value = KasUiState.Idle
            fetchAllActiveKas()
        }
    }
}