package com.example.projectpamt.viewmodel.auth

sealed class AuthUiState {
    abstract val message: String?

    object Idle : AuthUiState() {
        override val message: String? = null
    }
    object Loading : AuthUiState() {
        override val message: String? = null
    }
    object Success : AuthUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : AuthUiState()
}