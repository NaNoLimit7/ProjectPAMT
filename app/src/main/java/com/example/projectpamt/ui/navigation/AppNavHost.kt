package com.example.projectpamt.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projectpamt.ui.screens.home.DashboardScreen
import com.example.projectpamt.ui.screens.auth.LoginScreen
import com.example.projectpamt.ui.screens.auth.RegisterScreen
import com.example.projectpamt.ui.screens.home.PenjualanScreen
import com.example.projectpamt.viewmodel.auth.AuthViewModel
import com.example.projectpamt.viewmodel.penjualan.PenjualanViewModel

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

        composable<PenjualanList> {
            PenjualanScreen(
                modifier = modifier,
                navController = navController
            )
        }

        composable<TambahPelanggan> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Halaman Tambah Pelanggan")
            }
        }

        composable<ProsesPembayaran> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Halaman Proses Pembayaran")
            }
        }
    }
}