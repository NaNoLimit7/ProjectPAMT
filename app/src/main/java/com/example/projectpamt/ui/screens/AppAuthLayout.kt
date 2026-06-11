package com.example.projectpamt.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.projectpamt.ui.navigation.AppNavHost
import com.example.projectpamt.ui.navigation.Login
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun AppAuthLayout(
    authViewModel: AuthViewModel
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        AppNavHost(
            modifier = Modifier.padding(innerPadding),
            authViewModel = authViewModel,
            startDestination = Login
        )
    }
}
