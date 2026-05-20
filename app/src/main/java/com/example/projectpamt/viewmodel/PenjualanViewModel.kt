package com.example.projectpamt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.DetailPenjualan
import com.example.projectpamt.data.repository.PenjualanRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement

class PenjualanViewModel : ViewModel() {
    private val repository = PenjualanRepository()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var idPenjualanSukses by mutableStateOf<String?>(null)

    fun runProsesPenjualan(
        idPelanggan: String,
        idKas: String,
        jumlahBayar: Double,
        totalHarga: Double,
        items: List<DetailPenjualan>,
        detailPenjualan: JsonElement?) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            idPenjualanSukses = null
            try {
                val idBaru = repository.prosesPenjualan(
                    idPelanggan,
                    idKas,
                    jumlahBayar,
                    totalHarga,
                    items,
                    detailPenjualan
                )

                idPenjualanSukses = idBaru
            } catch (e: Exception) {
                errorMessage = "Gagal memproses penjualan ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun clearSuccessstatus() {
        idPenjualanSukses = null
    }
}