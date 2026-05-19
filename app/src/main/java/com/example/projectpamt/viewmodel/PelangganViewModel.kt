package com.example.projectpamt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.repository.PelangganRepository
import kotlinx.coroutines.launch

class PelangganViewModel(): ViewModel() {
    private val repository = PelangganRepository()

    var pelangganList by mutableStateOf<List<Pelanggan>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errormessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchPelanggan()
    }

    fun fetchPelanggan() {
        viewModelScope.launch {
            isLoading = true
            errormessage = null
            try {
                pelangganList = repository.getAllPelanggan()
            }catch (e: Exception) {
                errormessage = "Gagal mengambil data ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addPelanggan(nama: String, telepon: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val pelangganBaru = Pelanggan(nama = nama, telepon = telepon, aktif = true)
                repository.insertPelanggan(pelangganBaru)

                fetchPelanggan()
            } catch (e: Exception) {
                errormessage = "Gagal menambah pelanggan ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updatePelanggan(id: String, nama: String, telepon: String, aktif: Boolean) {
        viewModelScope.launch {
            isLoading = true
            try {
                val pelangganUpdate = Pelanggan(nama = nama, telepon = telepon, aktif = aktif)
                repository.updatePelanggan(id, pelangganUpdate)
                fetchPelanggan()
            } catch (e: Exception) {
                errormessage = "Gagal memperbarui data pelanggan: ${e.message}"
            } finally {
                isLoading = true
            }
        }
    }

    fun clearError() {
        errormessage = null
    }
}