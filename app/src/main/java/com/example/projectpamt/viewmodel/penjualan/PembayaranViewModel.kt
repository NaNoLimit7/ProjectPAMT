package com.example.projectpamt.viewmodel.penjualan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kas
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PembayaranViewModel : ViewModel() {
    private val _kasList = MutableStateFlow<List<Kas>>(Kas.dummyList.filter { it.aktif })
    val kasList: StateFlow<List<Kas>> = _kasList.asStateFlow()

    private val _selectedKas = MutableStateFlow<Kas?>(null)
    val selectedKas: StateFlow<Kas?> = _selectedKas.asStateFlow()

    private val _penerimaanKas = MutableStateFlow("")
    val penerimaanKas: StateFlow<String> = _penerimaanKas.asStateFlow()

    private val _uiState = MutableStateFlow<PembayaranUiState>(PembayaranUiState.Idle)
    val uiState: StateFlow<PembayaranUiState> = _uiState.asStateFlow()

    init {
        // Default select the first active Kas (usually Kas Utama)
        _selectedKas.value = _kasList.value.firstOrNull()
    }

    fun selectKas(kas: Kas) {
        _selectedKas.value = kas
    }

    fun onPenerimaanKasChange(value: String) {
        // Only allow numeric input (digits)
        val cleanValue = value.replace(Regex("[^0-9]"), "")
        _penerimaanKas.value = cleanValue
    }

    fun prosesPembayaran(
        pelangganId: String,
        cartItems: List<CartItem>,
        totalHarga: Double,
        onSuccess: () -> Unit
    ) {
        val kas = _selectedKas.value
        if (kas == null) {
            _uiState.value = PembayaranUiState.Error("Silakan pilih akun kas terlebih dahulu")
            return
        }

        val penerimaan = _penerimaanKas.value.toDoubleOrNull() ?: 0.0
        if (penerimaan < totalHarga) {
            _uiState.value = PembayaranUiState.Error("Penerimaan kas kurang dari total pembayaran")
            return
        }

        viewModelScope.launch {
            _uiState.value = PembayaranUiState.Loading
            try {
                // Simulate network latency
                delay(1000)
                
                // Keep it in-memory for now per guidelines
                _uiState.value = PembayaranUiState.Success
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = PembayaranUiState.Error(e.message ?: "Gagal memproses pembayaran")
            }
        }
    }

    fun resetState() {
        _uiState.value = PembayaranUiState.Idle
    }
}
