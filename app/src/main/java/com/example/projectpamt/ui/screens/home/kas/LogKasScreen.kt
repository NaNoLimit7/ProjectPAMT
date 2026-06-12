package com.example.projectpamt.ui.screens.home.kas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.LogKasItem
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.ui.utils.DynamicStatusBar
import com.example.projectpamt.viewmodel.kas.LogKasUiState
import com.example.projectpamt.viewmodel.kas.LogKasViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogKasScreen(
    modifier: Modifier = Modifier,
    kas: Kas,
    viewModel: LogKasViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Fetch logs on startup
    LaunchedEffect(kas.idKas) {
        kas.idKas?.let { id ->
            viewModel.fetchLogKas(id)
        }
    }

    // Set dynamic status bar color to GreenPrimary
    DynamicStatusBar(backgroundColor = GreenPrimary)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundSlate)
                .padding(innerPadding)
        ) {
            // ── HEADER SECTION ──────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Back Button
                Row(
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Kembali",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Title Area
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Log ${kas.nama}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Riwayat aktivitas untuk akun kas ini",
                        fontSize = 14.sp,
                        color = Color(0xFFDCFCE7)
                    )
                }
            }

            // ── CONTENT BODY ────────────────────────────────────────────────────
            when (val state = uiState) {
                is LogKasUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
                is LogKasUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = DangerRed,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                is LogKasUiState.Success -> {
                    SuccessContent(kas = kas, logs = state.logs)
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun SuccessContent(
    kas: Kas,
    logs: List<LogKasItem>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bento Status Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSlate)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Status Akun",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3E4940)
                        )
                        Text(
                            text = if (kas.aktif) "Aktif" else "Tidak Aktif",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (kas.aktif) Color(0xFF005F34) else DangerRed
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(9999.dp))
                            .background(if (kas.aktif) Color(0xFF8AF5B3) else Color(0xFFFFEBEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.kas),
                            contentDescription = null,
                            tint = if (kas.aktif) GreenPrimary else DangerRed,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "Riwayat Aktivitas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )
        }

        // Log Items List
        items(logs, key = { it.idLogKas ?: it.updatedAt }) { logItem ->
            LogEntryCard(logItem = logItem)
        }

        // Decorative Security Insight Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .shadow(1.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE4EAE2)),
                border = BorderStroke(1.dp, Color(0x1ABECA1B))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "SECURITY INSIGHT",
                        fontSize = 11.sp,
                        color = Color(0xFF6E7A70),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.6.sp
                    )
                    Text(
                        text = "\"Semua perubahan pada akun kas dicatat secara permanen untuk integritas data finansial.\"",
                        fontSize = 14.sp,
                        color = TextDark,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LogEntryCard(
    logItem: LogKasItem
) {
    val dateStr = logItem.updatedAt
    val formattedDate = remember(dateStr) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID")).parse(dateStr)
            if (date != null) {
                SimpleDateFormat("d MMMM, HH:mm", Locale("id", "ID")).format(date)
            } else {
                dateStr
            }
        } catch (e: Exception) {
            dateStr
        }
    }

    // Determine icon details based on log activity type
    val (icon, iconTint, iconBg) = when (logItem.tipeAktivitas) {
        "Pembuatan Akun" -> Triple(
            ImageVector.vectorResource(R.drawable.add),
            GreenPrimary,
            Color(0xFF94F7B6)
        )
        "Saldo disesuaikan oleh Admin" -> Triple(
            ImageVector.vectorResource(R.drawable.edit),
            GreenPrimary,
            Color(0xFF8AF5B3)
        )
        "Status berubah menjadi Tidak Aktif" -> Triple(
            Icons.Default.Warning,
            DangerRed,
            Color(0xFFFFDAD6)
        )
        "Status berubah menjadi Aktif" -> Triple(
            Icons.Default.Check,
            GreenPrimary,
            Color(0xFFD1E8DB)
        )
        else -> Triple(
            ImageVector.vectorResource(R.drawable.update_time),
            TextMuted,
            Color(0xFFE5E7EB)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSlate)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Icon Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Right Information Box
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Title & Date Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = logItem.tipeAktivitas,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formattedDate,
                        fontSize = 12.sp,
                        color = Color(0xFF3E4940),
                        fontWeight = FontWeight.Normal
                    )
                }

                // Description Text with highlighted money amounts
                Text(
                    text = buildAnnotatedLogDescription(logItem.detailKeterangan),
                    fontSize = 14.sp,
                    color = Color(0xFF3E4940),
                    lineHeight = 20.sp
                )

                // Author Footer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.profil),
                        contentDescription = null,
                        tint = Color(0xFF6E7A70),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Oleh: ${logItem.pelaku}",
                        fontSize = 13.sp,
                        color = Color(0xFF6E7A70),
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun buildAnnotatedLogDescription(text: String): AnnotatedString {
    return buildAnnotatedString {
        // Find currency patterns like Rp 5.000.000 or Rp5.000.000
        val pattern = Regex("Rp\\s*[0-9\\.,]+")
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
