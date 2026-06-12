package com.example.projectpamt.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.projectpamt.ui.navigation.AppNavHost
import com.example.projectpamt.ui.navigation.Dashboard
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun AppHomeLayout(
    authViewModel: AuthViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xffF8FAFC),
        bottomBar = {
            // Nanti akan ada AppNavigationBar di bottomBar
        }
    ) { innerPadding ->
        AppNavHost(
            modifier = Modifier.padding(innerPadding),
            authViewModel = authViewModel,
            startDestination = Dashboard
        )
    }
}
