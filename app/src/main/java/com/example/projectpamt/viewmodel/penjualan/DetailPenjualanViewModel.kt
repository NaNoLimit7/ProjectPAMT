package com.example.projectpamt.viewmodel.penjualan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.DetailPenjualan
import com.example.projectpamt.data.repository.DetailPenjualanRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailPenjualanViewModel(
    private val repository: DetailPenjualanRepository = DetailPenjualanRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailPenjualanUiState>(DetailPenjualanUiState.Idle)
    val uiState: StateFlow<DetailPenjualanUiState> = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        fetchDetailPenjualan()
    }

    fun fetchDetailPenjualan() {
        viewModelScope.launch {
            _uiState.value = DetailPenjualanUiState.Loading
            try {
                val result = repository.getAllDetailPenjualan()
                _uiState.value = DetailPenjualanUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = DetailPenjualanUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val result = repository.getAllDetailPenjualan()
                _uiState.value = DetailPenjualanUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = DetailPenjualanUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }

    fun addDetailPenjualan(idPenjualan: String, idProduk: String, kuantitas: Double, hargaSatuan: Double){
        viewModelScope.launch {
            _uiState.value = DetailPenjualanUiState.Loading
            try {
                val detailPenjualanBaru = DetailPenjualan(
                    idPenjualan = idPenjualan,
                    idProduk = idProduk,
                    kuantitas = kuantitas,
                    hargaSatuan = hargaSatuan
                )
                repository.insertDetailPenjualan(detailPenjualanBaru)
                fetchDetailPenjualan()
            } catch (e: Exception) {
                _uiState.value = DetailPenjualanUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is DetailPenjualanUiState.Error) {
            _uiState.value = DetailPenjualanUiState.Idle
            fetchDetailPenjualan()
        }
    }
}