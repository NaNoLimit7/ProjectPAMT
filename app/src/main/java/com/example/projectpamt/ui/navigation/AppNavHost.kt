package com.example.projectpamt.ui.navigation

import androidx.compose.runtime.Composable
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
import com.example.projectpamt.ui.screens.home.penjualan.ProsesPembayaranScreen
import com.example.projectpamt.ui.screens.home.penjualan.PembayaranBerhasilScreen
import com.example.projectpamt.ui.screens.home.penjualan.RiwayatPenjualanScreen
import com.example.projectpamt.ui.screens.home.produk.LogInventoryScreen
import com.example.projectpamt.ui.navigation.LogInventoryList
import com.example.projectpamt.ui.screens.home.kas.LogTotalKasScreen
import com.example.projectpamt.ui.navigation.LogTotalKas
import com.example.projectpamt.ui.navigation.LogKas
import com.example.projectpamt.ui.screens.home.kas.LogKasScreen
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.ui.screens.home.produk.DetailProdukScreen
import com.example.projectpamt.ui.screens.home.produk.EditProdukScreen
import com.example.projectpamt.ui.screens.home.produk.ProdukScreen
import com.example.projectpamt.ui.screens.home.produk.TambahProdukScreen
import com.example.projectpamt.viewmodel.auth.AuthViewModel
import com.example.projectpamt.viewmodel.kategori.KategoriViewModel
import com.example.projectpamt.viewmodel.pelanggan.PelangganViewModel
import com.example.projectpamt.viewmodel.produk.ProdukViewModel
import com.example.projectpamt.data.model.Produk
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

        composable<DetailProduk>(
            typeMap = mapOf(typeOf<Produk>() to Produk.ProdukNavType)
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<DetailProduk>()
            DetailProdukScreen(
                modifier = modifier,
                produk = route.produk,
                viewModel = produkViewModel,
                navController = navController
            )
        }

        composable<EditProduk>(
            typeMap = mapOf(typeOf<Produk>() to Produk.ProdukNavType)
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<EditProduk>()
            EditProdukScreen(
                modifier = modifier,
                produk = route.produk,
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

        composable<ProsesPembayaran>(
            typeMap = mapOf(
                typeOf<Pelanggan>() to Pelanggan.PelangganNavType
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ProsesPembayaran>()
            ProsesPembayaranScreen(
                modifier = modifier,
                pelanggan = route.pelanggan,
                cartItemsJson = route.cartItemsJson,
                totalHarga = route.totalHarga,
                navController = navController
            )
        }

        composable<InfoPembayaranBerhasil> { backStackEntry ->
            val route = backStackEntry.toRoute<InfoPembayaranBerhasil>()
            PembayaranBerhasilScreen(
                modifier = modifier,
                idTransaksi = route.idTransaksi,
                totalPembayaran = route.totalPembayaran,
                kembalian = route.kembalian,
                tanggalWaktu = route.tanggalWaktu,
                navController = navController
            )
        }

        composable<RiwayatPenjualan> {
            RiwayatPenjualanScreen(
                modifier = modifier,
                navController = navController
            )
        }

        composable<LogInventoryList> {
            LogInventoryScreen(
                modifier = modifier,
                navController = navController
            )
        }

        composable<LogTotalKas> {
            LogTotalKasScreen(
                modifier = modifier,
                navController = navController
            )
        }

        composable<LogKas>(
            typeMap = mapOf(typeOf<Kas>() to Kas.KasNavType)
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<LogKas>()
            LogKasScreen(
                modifier = modifier,
                kas = route.kas,
                navController = navController
            )
        }
    }
}