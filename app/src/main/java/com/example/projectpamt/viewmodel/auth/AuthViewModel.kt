package com.example.projectpamt.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.repository.AuthRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _authCheckState = MutableStateFlow<AuthCheckState>(AuthCheckState.Checking)
    val authCheckState: StateFlow<AuthCheckState> = _authCheckState

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _name = MutableStateFlow("")
    val fullname: StateFlow<String> = _name

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _role = MutableStateFlow("Store Manager")
    val role: StateFlow<String> = _role

    init {
        observeAuthStatus()
    }

    private fun observeAuthStatus() {
        viewModelScope.launch {
            repository.sessionStatus.collect { status ->
                _authCheckState.value = when (status) {
                    is SessionStatus.Authenticated -> {
                        val user = repository.getCurrentUser()
                        val metadata = user?.userMetadata
                        val fullName = metadata?.get("full_name")?.jsonPrimitive?.contentOrNull
                            ?: metadata?.get("fullname")?.jsonPrimitive?.contentOrNull
                            ?: ""
                        _name.value = fullName
                        _email.value = user?.email ?: ""
                        _phone.value = metadata?.get("phone")?.jsonPrimitive?.contentOrNull ?: ""
                        _role.value =
                            metadata?.get("role")?.jsonPrimitive?.contentOrNull ?: "Store Manager"

                        AuthCheckState.Authenticated
                    }

                    is SessionStatus.NotAuthenticated -> AuthCheckState.NotAuthenticated
                    is SessionStatus.Initializing -> AuthCheckState.Checking
                    is SessionStatus.RefreshFailure -> {
                        if (repository.isLoggedIn()) {
                            val user = repository.getCurrentUser()
                            val metadata = user?.userMetadata
                            val fullName = metadata?.get("full_name")?.jsonPrimitive?.contentOrNull
                                ?: metadata?.get("fullname")?.jsonPrimitive?.contentOrNull
                                ?: ""
                            _name.value = fullName
                            _email.value = user?.email ?: ""
                            _phone.value =
                                metadata?.get("phone")?.jsonPrimitive?.contentOrNull ?: ""
                            _role.value = metadata?.get("role")?.jsonPrimitive?.contentOrNull
                                ?: "Store Manager"

                            AuthCheckState.Authenticated
                        } else {
                            AuthCheckState.NotAuthenticated
                        }
                    }
                }
            }
        }
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onNameChange(value: String) {
        _name.value = value
    }

    fun onPhoneChange(value: String) {
        _phone.value = value
    }

    fun login() {
        viewModelScope.launch {
            try {
                _uiState.value = AuthUiState.Loading
                repository.login(
                    email = _email.value,
                    password = _password.value
                )
                _uiState.value = AuthUiState.Success

            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.toAppError().toUserMessage())
                Log.e("AUTH_LOGIN", "Login error: ${e.message}", e)
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            try {
                _uiState.value = AuthUiState.Loading

                repository.register(
                    fullname = _name.value,
                    email = _email.value,
                    password = _password.value,
                    phone = _phone.value
                )

                _uiState.value = AuthUiState.Success

            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.toAppError().toUserMessage())
                Log.e("AUTH_REGISTER", "Register error: ${e.message}", e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState.Idle
        }
    }

}