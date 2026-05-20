package com.example.projectpamt.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectpamt.ui.DashboardScreen
import com.example.projectpamt.ui.NewLoginScreen
import com.example.projectpamt.ui.NewRegisterScreen
import com.example.projectpamt.viewmodel.auth.AuthCheckState
import com.example.projectpamt.viewmodel.auth.AuthUiState
import com.example.projectpamt.viewmodel.auth.AuthViewModel


@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    val authCheckState = authViewModel.authCheckState.collectAsStateWithLifecycle()

    when (authCheckState.value) {
        is AuthCheckState.Authenticated -> {
            MainNavHost(
                modifier = modifier,
                authViewModel = authViewModel,
                startDestination = Screen.Dashboard.route
            )
        }

        is AuthCheckState.Checking -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }


        is AuthCheckState.NotAuthenticated -> {
            MainNavHost(
                modifier = modifier,
                authViewModel = authViewModel,
                startDestination = Screen.Login.route
            )
        }
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    startDestination: String
) {
    val navController = rememberNavController()
    val email = authViewModel.email.collectAsStateWithLifecycle()
    val password = authViewModel.password.collectAsStateWithLifecycle()
    val fullname = authViewModel.fullname.collectAsStateWithLifecycle()
    val phone = authViewModel.phone.collectAsStateWithLifecycle()
    val uiState = authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value) {
        if (uiState.value is AuthUiState.Success) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) {
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
        composable(Screen.Login.route) {

            NewLoginScreen(
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
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            NewRegisterScreen(
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

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onLogoutClick = {
                    authViewModel.logout()

                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) {
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