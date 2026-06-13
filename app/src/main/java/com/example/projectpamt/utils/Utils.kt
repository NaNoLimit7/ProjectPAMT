package com.example.projectpamt.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

fun formatIsoDate(isoString: String?): String {
    if (isoString == null) return ""
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val date = parser.parse(isoString.substring(0, 19)) ?: Date()
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        formatter.format(date)
    } catch (e: Exception) {
        isoString
    }
}

fun buildAnnotatedLogDescription(text: String): AnnotatedString {
    return buildAnnotatedString {
        // Find currency patterns like Rp 5.000.000 or Rp5.000.000
        val pattern = Regex("Rp\\s*[0-9.,]+")
        val matches = pattern.findAll(text)
        var lastIdx = 0
        for (match in matches) {
            // Append regular text before match
            append(text.substring(lastIdx, match.range.first))
            // Append styled match
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF005F34) // Dark Green
                )
            ) {
                append(match.value)
            }
            lastIdx = match.range.last + 1
        }
        if (lastIdx < text.length) {
            append(text.substring(lastIdx))
        }
    }
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