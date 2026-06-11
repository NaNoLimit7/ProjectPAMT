package com.example.projectpamt.viewmodel.penjualan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.DetailPenjualan
import com.example.projectpamt.data.repository.PenjualanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement

class PenjualanViewModel(
    private val repository: PenjualanRepository = PenjualanRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<PenjualanUiState>(PenjualanUiState.Idle)
    val uiState: StateFlow<PenjualanUiState> = _uiState.asStateFlow()

    fun runProsesPenjualan(
        idPelanggan: String,
        idKas: String,
        jumlahBayar: Double,
        totalHarga: Double,
        items: List<DetailPenjualan>,
        detailPenjualan: JsonElement?) {
        viewModelScope.launch {
            _uiState.value = PenjualanUiState.Loading
            try {
                val idBaru = repository.prosesPenjualan(
                    idPelanggan,
                    idKas,
                    jumlahBayar,
                    totalHarga,
                    items,
                    detailPenjualan
                )
                _uiState.value = PenjualanUiState.Success(idBaru)
            } catch (e: Exception) {
                _uiState.value = PenjualanUiState.Error("Gagal memproses penjualan ${e.message}")
            }
        }
    }

    fun clearUiState() {
        _uiState.value = PenjualanUiState.Idle
    }
}