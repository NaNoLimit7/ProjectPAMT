package com.example.projectpamt.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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
    startDestination: Any,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Login> {
            LoginScreen(
                modifier = modifier,
                authViewModel = authViewModel,
                navController = navController,
            )
        }

        composable<Register> {
            RegisterScreen(
                modifier = modifier,
                authViewModel = authViewModel,
                navController = navController,
            )
        }

        composable<Dashboard> {
            DashboardScreen(
                modifier = modifier,
                authViewModel = authViewModel,
                navController = navController
            )
        }
    }
}