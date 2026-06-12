package com.example.projectpamt.viewmodel.pelanggan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Pelanggan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PelangganViewModel : ViewModel() {
    private val _pelangganList = MutableStateFlow(Pelanggan.dummyList)
    
    private val _uiState = MutableStateFlow<PelangganUiState>(PelangganUiState.Idle)
    val uiState: StateFlow<PelangganUiState> = _uiState.asStateFlow()

    init {
        fetchPelanggan()
    }

    fun fetchPelanggan() {
        viewModelScope.launch {
            _uiState.value = PelangganUiState.Loading
            try {
                // Menggunakan data dummy offline in-memory untuk kebutuhan UI
                _uiState.value = PelangganUiState.Success(_pelangganList.value)
            } catch (e: Exception) {
                _uiState.value = PelangganUiState.Error("Gagal mengambil data: ${e.message}")
            }
        }
    }

    fun addPelanggan(nama: String, telepon: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = PelangganUiState.Loading
            try {
                val newId = (_pelangganList.value.mapNotNull { it.idPelanggan?.toIntOrNull() }.maxOrNull() ?: 0) + 1
                val pelangganBaru = Pelanggan(
                    idPelanggan = newId.toString(),
                    nama = nama,
                    telepon = telepon,
                    aktif = true
                )
                _pelangganList.value += pelangganBaru
                fetchPelanggan()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = PelangganUiState.Error("Gagal menambah pelanggan: ${e.message}")
            }
        }
    }

    fun updatePelanggan(id: String, nama: String, telepon: String, aktif: Boolean, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = PelangganUiState.Loading
            try {
                _pelangganList.value = _pelangganList.value.map { pelanggan ->
                    if (pelanggan.idPelanggan == id) {
                        pelanggan.copy(nama = nama, telepon = telepon, aktif = aktif)
                    } else {
                        pelanggan
                    }
                }
                fetchPelanggan()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = PelangganUiState.Error("Gagal memperbarui data pelanggan: ${e.message}")
            }
        }
    }

    fun deletePelanggan(id: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = PelangganUiState.Loading
            try {
                _pelangganList.value = _pelangganList.value.filterNot { it.idPelanggan == id }
                fetchPelanggan()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = PelangganUiState.Error("Gagal menghapus pelanggan: ${e.message}")
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is PelangganUiState.Error) {
            _uiState.value = PelangganUiState.Idle
            fetchPelanggan()
        }
    }
}