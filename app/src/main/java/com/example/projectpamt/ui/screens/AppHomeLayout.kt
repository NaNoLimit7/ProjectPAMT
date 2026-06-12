package com.example.projectpamt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projectpamt.ui.components.AppNavigationBar
import com.example.projectpamt.ui.navigation.AppNavHost
import com.example.projectpamt.ui.navigation.Dashboard
import com.example.projectpamt.ui.navigation.PenjualanList
import com.example.projectpamt.ui.navigation.ProdukList
import com.example.projectpamt.ui.navigation.PelangganList
import com.example.projectpamt.ui.navigation.KasList
import com.example.projectpamt.ui.navigation.ProsesPembayaran
import com.example.projectpamt.ui.navigation.RiwayatPenjualan
import com.example.projectpamt.ui.navigation.LogInventoryList
import com.example.projectpamt.ui.navigation.LogTotalKas
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.utils.DynamicStatusBar
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun AppHomeLayout(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    // Mapping route → warna background
    val statusBarColor = when {
        currentDestination?.hasRoute(Dashboard::class) == true ||
        currentDestination?.hasRoute(PenjualanList::class) == true ||
        currentDestination?.hasRoute(KasList::class) == true ||
        currentDestination?.hasRoute(ProsesPembayaran::class) == true ||
        currentDestination?.hasRoute(RiwayatPenjualan::class) == true ||
        currentDestination?.hasRoute(LogInventoryList::class) == true ||
        currentDestination?.hasRoute(LogTotalKas::class) == true -> GreenPrimary
        else -> BackgroundSlate // default untuk screen lain
    }

    DynamicStatusBar(backgroundColor = statusBarColor)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            val routeWithBottomBar = listOf(
                Dashboard::class,
                PenjualanList::class,
                ProdukList::class,
                PelangganList::class,
                KasList::class
            )
            val isHaveBottomBar = routeWithBottomBar.any { currentDestination?.hasRoute(it) == true }

            if (isHaveBottomBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    AppNavigationBar(
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            modifier = Modifier.padding(innerPadding),
            authViewModel = authViewModel,
            startDestination = Dashboard,
            navController = navController
        )
    }
}
