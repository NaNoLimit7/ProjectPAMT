package com.example.projectpamt.viewmodel.labarugi.uistate

import com.example.projectpamt.viewmodel.labarugi.LabaRugiPeriode
import com.example.projectpamt.viewmodel.labarugi.LabaRugiState

sealed class LabaRugiUiState {
    abstract val message: String?

    object Idle : LabaRugiUiState() {
        override val message: String? = null
    }

    object Loading : LabaRugiUiState() {
        override val message: String? = null
    }

    data class Success(
        val state: LabaRugiState,
        val selectedPeriod: LabaRugiPeriode
    ) : LabaRugiUiState() {
        override val message: String? = null
    }

    data class Error(override val message: String) : LabaRugiUiState()
}
