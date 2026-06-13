package com.example.projectpamt.viewmodel.pengeluaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.model.Pengeluaran
import com.example.projectpamt.data.repository.PengeluaranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PengeluaranViewModel(
    private val repository: PengeluaranRepository = PengeluaranRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PengeluaranUiState>(PengeluaranUiState.Idle)
    val uiState: StateFlow<PengeluaranUiState> = _uiState.asStateFlow()

    init {
        fetchPengeluaran()
    }

    fun fetchPengeluaran() {
        viewModelScope.launch {
            _uiState.value = PengeluaranUiState.Loading
            try {
                val result = repository.getAllPengeluaran()
                _uiState.value = PengeluaranUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value =
                    PengeluaranUiState.Error("Gagal memuat data pengeluaran: ${e.message}")
            }
        }
    }

    fun addPengeluaran(
        idKategori: String,
        idKas: String,
        deskripsi: String,
        total: Double,
        kategori: Kategori?,
        kas: Kas?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = PengeluaranUiState.Loading
            try {
                val pengeluaranBaru = Pengeluaran(
                    idKategori = idKategori,
                    idKas = idKas,
                    deskripsi = deskripsi,
                    total = total,
                    kategori = kategori,
                    kas = kas
                )
                repository.insertPengeluaran(pengeluaranBaru)
                fetchPengeluaran()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value =
                    PengeluaranUiState.Error("Gagal mencatat pengeluaran: ${e.message}")
            }
        }
    }

    fun updatePengeluaran(
        idPengeluaran: String,
        idKategori: String,
        idKas: String,
        deskripsi: String,
        total: Double,
        kategori: Kategori?,
        kas: Kas?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = PengeluaranUiState.Loading
            try {
                val pengeluaranUpdate = Pengeluaran(
                    idKategori = idKategori,
                    idKas = idKas,
                    deskripsi = deskripsi,
                    total = total,
                    kategori = kategori,
                    kas = kas
                )
                repository.updatePengeluaran(idPengeluaran, pengeluaranUpdate)
                fetchPengeluaran()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value =
                    PengeluaranUiState.Error("Gagal memperbarui pengeluaran: ${e.message}")
            }
        }
    }

    fun deletePengeluaran(idPengeluaran: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = PengeluaranUiState.Loading
            try {
                repository.deletePengeluaran(idPengeluaran)
                fetchPengeluaran()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value =
                    PengeluaranUiState.Error("Gagal menghapus pengeluaran: ${e.message}")
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is PengeluaranUiState.Error) {
            _uiState.value = PengeluaranUiState.Idle
            fetchPengeluaran()
        }
    }
}