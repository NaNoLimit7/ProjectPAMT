package com.example.projectpamt.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectpamt.ui.screens.home.DashboardScreen
import com.example.projectpamt.ui.screens.auth.LoginScreen
import com.example.projectpamt.ui.screens.auth.RegisterScreen
import com.example.projectpamt.viewmodel.auth.AuthUiState
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    startDestination: Any
) {
    val navController = rememberNavController()
    val email = authViewModel.email.collectAsStateWithLifecycle()
    val password = authViewModel.password.collectAsStateWithLifecycle()
    val fullname = authViewModel.fullname.collectAsStateWithLifecycle()
    val phone = authViewModel.phone.collectAsStateWithLifecycle()
    val uiState = authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value) {
        if (uiState.value is AuthUiState.Success) {
            navController.navigate(Dashboard) {
                popUpTo(Login) {
                    inclusive = true
                }
            }
            authViewModel.resetState()
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Login> {
            LoginScreen(
                modifier = modifier,
                email = email.value,
                password = password.value,
                uiState = uiState.value,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onLoginClick = {
                    authViewModel.login()
                },
                onNavigateToRegister = {
                    navController.navigate(Register)
                }
            )
        }

        composable<Register> {
            RegisterScreen(
                modifier = Modifier.fillMaxSize(),
                email = email.value,
                password = password.value,
                name = fullname.value,
                phone = phone.value,
                uiState = uiState.value,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onNameChange = authViewModel::onNameChange,
                onPhoneChange = authViewModel::onPhoneChange,
                onRegisterClick = {
                    authViewModel.register()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
            )
        }

        composable<Dashboard> {
            DashboardScreen(
                onLogoutClick = {
                    authViewModel.logout()

                    navController.navigate(Login) {
                        popUpTo(Dashboard) {
                            inclusive = true
                        }
                    }
                },
                fullname = fullname.value,
                email = email.value
            )
        }
    }
}