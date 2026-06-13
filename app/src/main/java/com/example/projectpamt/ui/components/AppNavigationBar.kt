package com.example.projectpamt.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.projectpamt.R
import com.example.projectpamt.ui.navigation.Dashboard
import com.example.projectpamt.ui.navigation.KasList
import com.example.projectpamt.ui.navigation.PelangganList
import com.example.projectpamt.ui.navigation.PenjualanList
import com.example.projectpamt.ui.navigation.ProdukList
import com.example.projectpamt.ui.theme.GreenMintActive
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextPlaceholder

@Composable
fun AppNavigationBar(
    navController: NavController,
    currentDestination: NavDestination?,
    containerColor: Color = Color.White,
    contentColor: Color = TextPlaceholder,
    selectedIconColor: Color = GreenPrimary,
    unselectedIconColor: Color = TextPlaceholder,
    indicatorColor: Color = GreenMintActive,
) {
    NavigationBar(
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = Modifier
            .navigationBarsPadding()
            .padding(horizontal = 13.dp)
    ) {
        NavigationBarItem(
            selected = currentDestination?.hasRoute<Dashboard>() == true,
            onClick = {
                navController.navigate(Dashboard) {
                    popUpTo<Dashboard> { inclusive = false; saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.dashboard),
                    contentDescription = "Dashboard",
                    modifier = Modifier.size(24.dp),
                )
            },
            label = {
                Text(
                    "Dashboard",
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight(700),
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                selectedTextColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                unselectedTextColor = unselectedIconColor,
                indicatorColor = indicatorColor
            ),
        )

        NavigationBarItem(
            selected = currentDestination?.hasRoute<PenjualanList>() == true,
            onClick = {
                navController.navigate(PenjualanList) {
                    popUpTo<Dashboard> { inclusive = false; saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.penjualan),
                    contentDescription = "Penjualan",
                    modifier = Modifier.size(24.dp),
                )
            },
            label = {
                Text(
                    "Penjualan", fontSize = 10.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight(700),
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                selectedTextColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                unselectedTextColor = unselectedIconColor,
                indicatorColor = indicatorColor
            )
        )

        NavigationBarItem(
            selected = currentDestination?.hasRoute<ProdukList>() == true,
            onClick = {
                navController.navigate(ProdukList) {
                    popUpTo<Dashboard> { inclusive = false; saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.produk),
                    contentDescription = "Produk",
                    modifier = Modifier.size(24.dp),
                )
            },
            label = {
                Text(
                    "Produk", fontSize = 10.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight(700),
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                selectedTextColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                unselectedTextColor = unselectedIconColor,
                indicatorColor = indicatorColor
            )
        )

        NavigationBarItem(
            selected = currentDestination?.hasRoute<PelangganList>() == true,
            onClick = {
                navController.navigate(PelangganList) {
                    popUpTo<Dashboard> { inclusive = false; saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.pelanggan),
                    contentDescription = "Pelanggan",
                    modifier = Modifier.size(24.dp),
                )
            },
            label = {
                Text(
                    "Pelanggan", fontSize = 10.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight(700),
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                selectedTextColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                unselectedTextColor = unselectedIconColor,
                indicatorColor = indicatorColor
            )
        )

        NavigationBarItem(
            selected = currentDestination?.hasRoute<KasList>() == true,
            onClick = {
                navController.navigate(KasList) {
                    popUpTo<Dashboard> { inclusive = false; saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.kas),
                    contentDescription = "Kas",
                    modifier = Modifier.size(24.dp),
                )
            },
            label = {
                Text(
                    "Kas", fontSize = 10.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight(700),
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                selectedTextColor = selectedIconColor,
                unselectedIconColor = unselectedIconColor,
                unselectedTextColor = unselectedIconColor,
                indicatorColor = indicatorColor
            )
        )
    }
}
