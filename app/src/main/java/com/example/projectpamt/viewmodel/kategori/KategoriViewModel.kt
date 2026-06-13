package com.example.projectpamt.viewmodel.kategori

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.repository.KategoriRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KategoriViewModel(
    private val repository: KategoriRepository = KategoriRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<KategoriUiState>(KategoriUiState.Idle)
    val uiState: StateFlow<KategoriUiState> = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        fetchKategori()
    }

    fun fetchKategori() {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                val list = repository.getAllKategori()
                _uiState.value = KategoriUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val list = repository.getAllKategori()
                _uiState.value = KategoriUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }

    fun addKategori(name: String) {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                val newId = java.util.UUID.randomUUID().toString()
                val kategoriBaru = Kategori(idKategori = newId, name = name)
                repository.insertKategori(kategoriBaru)
                fetchKategori()
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun updateKategori(id: String, name: String) {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                val kategoriUpdate = Kategori(idKategori = id, name = name)
                repository.updateKategori(id, kategoriUpdate)
                fetchKategori()
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error(e.toAppError().toUserMessage())
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
                _uiState.value = KategoriUiState.Error(e.toAppError().toUserMessage())
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