package com.example.projectpamt.ui.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.core.view.WindowCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatRupiah(amount: Double): String {
    val formatted = String.format(Locale("id", "ID"), "%,.0f", amount)
        .replace(',', '.')
    return "Rp$formatted"
}

fun formatNumber(number: Int): String {
    return String.format(Locale("id", "ID"), "%,d", number).replace(',', '.')
}

fun Date.toIndonesianFormattedDate(): String {
    val formatter = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
    return formatter.format(this).replaceFirstChar { it.uppercase() }
}

fun String.getInitials(): String {
    return this.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "U" }
}

@Composable
fun DynamicStatusBar(backgroundColor: Color) {
    val activity = LocalActivity.current
    SideEffect {
        val window = activity?.window ?: return@SideEffect
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = backgroundColor.luminance() > 0.179f
    }
}