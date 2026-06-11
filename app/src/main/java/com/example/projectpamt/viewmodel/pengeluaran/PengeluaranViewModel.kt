package com.example.projectpamt.viewmodel.pengeluaran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Pengeluaran
import com.example.projectpamt.data.repository.PengeluaranRepository
import kotlinx.coroutines.launch

class PengeluaranViewModel : ViewModel() {

    private val repository = PengeluaranRepository()

    var pengeluaranList by mutableStateOf<List<Pengeluaran>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchPengeluaran()
    }

    fun fetchPengeluaran() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                pengeluaranList = repository.getAllPengeluaran()
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data pengeluaran: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addPengeluaran(idKategori: String, idKas: String, deskripsi: String, total: Double) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val pengeluaranBaru = Pengeluaran(
                    idKategori = idKategori,
                    idKas = idKas,
                    deskripsi = deskripsi,
                    total = total
                )
                repository.insertPengeluaran(pengeluaranBaru)

                fetchPengeluaran()
            } catch (e: Exception) {
                errorMessage = "Gagal mencatat pengeluaran: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updatePengeluaran(idPengeluaran: String, idKategori: String, idKas: String, deskripsi: String, total: Double) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val pengeluaranUpdate = Pengeluaran(
                    idKategori = idKategori,
                    idKas = idKas,
                    deskripsi = deskripsi,
                    total = total
                )
                repository.updatePengeluaran(idPengeluaran, pengeluaranUpdate)

                fetchPengeluaran()
            } catch (e: Exception) {
                errorMessage = "Gagal memperbarui pengeluaran: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deletePengeluaran(idPengeluaran: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.deletePengeluaran(idPengeluaran)

                fetchPengeluaran()
            } catch (e: Exception) {
                errorMessage = "Gagal menghapus pengeluaran ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}