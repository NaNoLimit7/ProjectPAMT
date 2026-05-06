package com.example.projectpamt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onLogoutClick: () -> Unit,
    fullname: String,
    email: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            "Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        Text("Anda berhasil login ke aplikasi.")

        Spacer(Modifier.height(12.dp))

        Text("Fullname: $fullname")

        Spacer(Modifier.height(12.dp))

        Text("Email: $email")

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onLogoutClick
        ) {
            Text("Logout")
        }
    }
}