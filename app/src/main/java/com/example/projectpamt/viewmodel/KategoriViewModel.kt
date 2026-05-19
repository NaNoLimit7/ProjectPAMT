package com.example.projectpamt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.repository.KategoriRepository
import kotlinx.coroutines.launch

class KategoriViewModel : ViewModel() {
    private val repository = KategoriRepository()

    var kategoriList by mutableStateOf<List<Kategori>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchKategori()
    }

    fun fetchKategori() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                kategoriList = repository.getAllKategori()
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data kategori ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addKategori(name: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val kategoriBaru = Kategori(name = name)
                repository.insertKategori(kategoriBaru)

                fetchKategori()
            } catch (e: Exception) {
                errorMessage = "Gagal menambahkan kategori ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateKategori(id: String, name: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val kategoriUpdate = Kategori(name = name)
                repository.updateKategori(id, kategoriUpdate)

                fetchKategori()
            } catch (e: Exception) {
                errorMessage = "Gagal memperbarui kategori ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteKategori(id: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.deleteKategori(id)

                fetchKategori()
            } catch (e: Exception) {
                errorMessage = "Gagal menghapus kategori ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}