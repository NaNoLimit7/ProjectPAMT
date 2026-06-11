package com.example.projectpamt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.projectpamt.ui.navigation.AppNavHost
import com.example.projectpamt.ui.navigation.Login
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun AppAuthLayout(
    authViewModel: AuthViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF2F0EB)
    ) { innerPadding ->
        AppNavHost(
            modifier = Modifier.padding(innerPadding),
            authViewModel = authViewModel,
            startDestination = Login
        )
    }
}
