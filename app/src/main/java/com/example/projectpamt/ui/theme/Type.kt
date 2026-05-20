package com.example.projectpamt.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.projectpamt.R

// Set of Material typography styles to start with
val manrope = FontFamily(
    Font(R.font.manrope_light, FontWeight.Light),
    Font(R.font.manrope, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
)

val andika = FontFamily(
    Font(R.font.andika, FontWeight.Normal),
    Font(R.font.andika_bold, FontWeight.Bold),
)

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = andika),
    displayMedium = TextStyle(fontFamily = andika),
    displaySmall = TextStyle(fontFamily = andika),
    headlineLarge = TextStyle(fontFamily = andika),
    headlineMedium = TextStyle(fontFamily = andika),
    headlineSmall = TextStyle(fontFamily = andika),
    titleLarge = TextStyle(fontFamily = andika),
    titleMedium = TextStyle(fontFamily = andika),
    titleSmall = TextStyle(fontFamily = andika),
    bodyLarge = TextStyle(
        fontFamily = andika,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(fontFamily = andika),
    bodySmall = TextStyle(fontFamily = andika),
    labelLarge = TextStyle(fontFamily = andika),
    labelMedium = TextStyle(fontFamily = andika),
    labelSmall = TextStyle(fontFamily = andika)
)