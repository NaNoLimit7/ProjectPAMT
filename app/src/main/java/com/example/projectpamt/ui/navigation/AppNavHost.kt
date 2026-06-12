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
import androidx.navigation.toRoute
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.ui.screens.auth.LoginScreen
import com.example.projectpamt.ui.screens.auth.RegisterScreen
import com.example.projectpamt.ui.screens.home.dashboard.DashboardScreen
import com.example.projectpamt.ui.screens.home.kas.KasScreen
import com.example.projectpamt.ui.screens.home.pelanggan.AktivitasPelangganScreen
import com.example.projectpamt.ui.screens.home.pelanggan.EditPelangganScreen
import com.example.projectpamt.ui.screens.home.pelanggan.PelangganScreen
import com.example.projectpamt.ui.screens.home.pelanggan.TambahPelangganScreen
import com.example.projectpamt.ui.screens.home.penjualan.PenjualanScreen
import com.example.projectpamt.ui.screens.home.produk.ProdukScreen
import com.example.projectpamt.ui.screens.home.produk.TambahProdukScreen
import com.example.projectpamt.viewmodel.auth.AuthViewModel
import com.example.projectpamt.viewmodel.kategori.KategoriViewModel
import com.example.projectpamt.viewmodel.pelanggan.PelangganViewModel
import com.example.projectpamt.viewmodel.produk.ProdukViewModel
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    startDestination: Any,
    navController: NavHostController,
) {
    val pelangganViewModel: PelangganViewModel = viewModel()
    val produkViewModel: ProdukViewModel = viewModel()
    val kategoriViewModel: KategoriViewModel = viewModel()

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

        composable<PenjualanList> {
            PenjualanScreen(
                modifier = modifier,
                navController = navController
            )
        }

        composable<ProdukList> {
            ProdukScreen(
                modifier = modifier,
                viewModel = produkViewModel,
                navController = navController
            )
        }

        composable<PelangganList> {
            PelangganScreen(
                modifier = modifier,
                viewModel = pelangganViewModel,
                navController = navController
            )
        }

        composable<KasList> {
            KasScreen(
                modifier = modifier,
                navController = navController
            )
        }

        composable<TambahPelanggan> {
            TambahPelangganScreen(
                modifier = modifier,
                viewModel = pelangganViewModel,
                navController = navController
            )
        }

        composable<TambahProduk> {
            TambahProdukScreen(
                modifier = modifier,
                produkViewModel = produkViewModel,
                kategoriViewModel = kategoriViewModel,
                navController = navController
            )
        }

        composable<EditPelanggan>(
            typeMap = mapOf(typeOf<Pelanggan>() to Pelanggan.PelangganNavType)
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<EditPelanggan>()
            EditPelangganScreen(
                modifier = modifier,
                pelanggan = route.pelanggan,
                viewModel = pelangganViewModel,
                navController = navController
            )
        }

        composable<AktivitasPelanggan>(
            typeMap = mapOf(typeOf<Pelanggan>() to Pelanggan.PelangganNavType)
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<AktivitasPelanggan>()
            AktivitasPelangganScreen(
                modifier = modifier,
                pelanggan = route.pelanggan,
                navController = navController
            )
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