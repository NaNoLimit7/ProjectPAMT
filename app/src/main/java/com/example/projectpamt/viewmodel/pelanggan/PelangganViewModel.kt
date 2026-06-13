package com.example.projectpamt.viewmodel.pelanggan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Pelanggan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.projectpamt.data.repository.PelangganRepository

class PelangganViewModel(
    private val repository: PelangganRepository = PelangganRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<PelangganUiState>(PelangganUiState.Idle)
    val uiState: StateFlow<PelangganUiState> = _uiState.asStateFlow()

    init {
        fetchPelanggan()
    }

    fun fetchPelanggan() {
        viewModelScope.launch {
            _uiState.value = PelangganUiState.Loading
            try {
                val list = repository.getAllPelanggan()
                _uiState.value = PelangganUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = PelangganUiState.Error("Gagal mengambil data: ${e.message}")
            }
        }
    }

    fun addPelanggan(nama: String, telepon: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = PelangganUiState.Loading
            try {
                val pelangganBaru = Pelanggan(
                    nama = nama,
                    telepon = telepon,
                    aktif = true
                )
                repository.insertPelanggan(pelangganBaru)
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
                val pelangganBaru = Pelanggan(
                    idPelanggan = id,
                    nama = nama,
                    telepon = telepon,
                    aktif = aktif
                )
                repository.updatePelanggan(id, pelangganBaru)
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
                repository.softDeletePelanggan(id)
                fetchPelanggan()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = PelangganUiState.Error("Gagal menonaktifkan pelanggan: ${e.message}")
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