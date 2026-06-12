package com.example.projectpamt.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.ui.components.CircleContainer
import com.example.projectpamt.ui.components.DashboardCard
import com.example.projectpamt.ui.navigation.Dashboard
import com.example.projectpamt.ui.navigation.Login
import com.example.projectpamt.ui.theme.ProjectPAMTTheme
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val email by authViewModel.email.collectAsStateWithLifecycle()
    val fullname by authViewModel.fullname.collectAsStateWithLifecycle()

    DashboardContent(
        modifier = modifier,
        fullname = fullname,
        email = email,
        onLogoutClick = {
            authViewModel.logout()
            navController.navigate(Login) {
                popUpTo(Dashboard) {
                    inclusive = true
                }
            }
        },
    )
}

@Composable
private fun DashboardContent(
    modifier: Modifier,
    fullname: String,
    email: String,
    onLogoutClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF00754A),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Dashboard",
                        fontSize = 30.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFFDBEAFE),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Senin, 20 Mei 2024",
                            fontSize = 14.sp,
                            color = Color(0xFFDBEAFE),
                        )
                    }
                }

                CircleContainer(
                    size = 48.dp,
                    backgroundColor = Color(0xFF53A285)
                ) {
                    Text(
                        text = "JD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                    )
                }

            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        titleIcon = R.drawable.penjualan,
                        title = "Penjualan Bulan ini",
                        value = "Rp500.000",
                        statsLabelIcon = Icons.Default.ArrowUpward,
                        statsLabel = "12.5% vs bulan lalu",
                        statsLabelColor = Color(0xFF86EFAC),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        titleIcon = R.drawable.kas,
                        title = "Saldo Seluruh Kas",
                        value = "Rp7.000.000",
                        statsLabel = "3 Akun",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        titleIcon = R.drawable.produk,
                        title = "Produk",
                        value = "342",
                        valueFontSize = 24.sp,
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        titleIcon = R.drawable.pelanggan,
                        title = "Pelanggan",
                        value = "1.248",
                        valueFontSize = 22.sp,
                        statsLabelIcon = Icons.Default.ArrowUpward,
                        statsLabel = "24 baru",
                        statsLabelColor = Color(0xFF86EFAC),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenPreview() {
    ProjectPAMTTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            DashboardContent (
                onLogoutClick = {},
                fullname = "John Doe",
                email = "john.doe@example.com",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}