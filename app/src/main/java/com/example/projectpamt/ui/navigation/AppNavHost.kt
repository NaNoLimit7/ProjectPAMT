package com.example.projectpamt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projectpamt.ui.screens.home.DashboardScreen
import com.example.projectpamt.ui.screens.auth.LoginScreen
import com.example.projectpamt.ui.screens.auth.RegisterScreen
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    startDestination: Any,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
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