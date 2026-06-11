package com.example.projectpamt.viewmodel.kategori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.repository.KategoriRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KategoriViewModel(
    private val repository: KategoriRepository = KategoriRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<KategoriUiState>(KategoriUiState.Idle)
    val uiState: StateFlow<KategoriUiState> = _uiState.asStateFlow()

    init {
        fetchKategori()
    }

    fun fetchKategori() {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                val result = repository.getAllKategori()
                _uiState.value = KategoriUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error("Gagal memuat data kategori ${e.message}")
            }
        }
    }

    fun addKategori(name: String) {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                val kategoriBaru = Kategori(name = name)
                repository.insertKategori(kategoriBaru)
                fetchKategori()
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error("Gagal menambahkan kategori ${e.message}")
            }
        }
    }

    fun updateKategori(id: String, name: String) {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                val kategoriUpdate = Kategori(name = name)
                repository.updateKategori(id, kategoriUpdate)
                fetchKategori()
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error("Gagal memperbarui kategori ${e.message}")
            }
        }
    }

    fun deleteKategori(id: String) {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                repository.deleteKategori(id)
                fetchKategori()
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error("Gagal menghapus kategori ${e.message}")
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is KategoriUiState.Error) {
            _uiState.value = KategoriUiState.Idle
            fetchKategori()
        }
    }
}