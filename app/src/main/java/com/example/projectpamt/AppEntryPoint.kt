package com.example.projectpamt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpamt.ui.screens.AppAuthLayout
import com.example.projectpamt.ui.screens.AppHomeLayout
import com.example.projectpamt.viewmodel.auth.AuthCheckState
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun AppEntryPoint(
    authViewModel: AuthViewModel = viewModel()
) {
    val authCheckState = authViewModel.authCheckState.collectAsStateWithLifecycle()

    when (authCheckState.value) {
        is AuthCheckState.Authenticated -> {
            AppHomeLayout(authViewModel = authViewModel)
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
            AppAuthLayout(authViewModel = authViewModel)
        }
    }
}
