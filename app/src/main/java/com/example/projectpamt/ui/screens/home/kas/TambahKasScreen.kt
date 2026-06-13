package com.example.projectpamt.ui.screens.home.kas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import com.example.projectpamt.ui.theme.TextMuted
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.utils.ValidationUtils
import com.example.projectpamt.viewmodel.kas.KasUiState
import com.example.projectpamt.viewmodel.kas.KasViewModel

@Composable
fun TambahKasScreen(
    viewModel: KasViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState is KasUiState.Loading

    TambahKasContent(
        isLoading = isLoading,
        onBackClick = { navController.popBackStack() },
        onSaveClick = { nama, saldo, keterangan, aktif ->
            viewModel.addKas(nama, saldo, keterangan, aktif) {
                navController.popBackStack()
            }
        }
    )
}

@Composable
private fun TambahKasContent(
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: (String, Double, String, Boolean) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }
    var aktif by remember { mutableStateOf(true) }
    
    var namaError by remember { mutableStateOf<String?>(null) }
    var saldoError by remember { mutableStateOf<String?>(null) }

    val isEnabled = remember(nama, saldo, namaError, saldoError, isLoading) {
        nama.isNotBlank() && saldo.isNotBlank() &&
                namaError == null && saldoError == null && !isLoading
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // ── Scrollable Content Area ──────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            // ── HEADER SECTION (Fixed) ──────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        spotColor = Color(0x1A000000),
                        ambientColor = Color(0x1A000000)
                    )
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
                        .clickable { onBackClick() }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Kembali",
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                // Title Area
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Tambah Akun Kas",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Mendaftarkan akun kas baru",
                        fontSize = 14.sp,
                        color = Color(0xFFDCFCE7),
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // ── FORM SECTION ────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // CARD 1: Data Kas Form
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 20.dp,
                            spotColor = Color(0x0D1E2430),
                            ambientColor = Color(0x0D1E2430)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSlate),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title: Data Kas with Icon
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.kas),
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Data Akun Kas Baru",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0x33BECABE),
                                thickness = 1.dp
                            )
                        }

                        // Nama Kas Input
                        AppTextField(
                            value = nama,
                            onValueChange = {
                                nama = it
                                namaError = ValidationUtils.validateKasName(it).errorMessage
                            },
                            externalLabel = "Nama Akun Kas *",
                            placeholder = "Masukkan nama kas baru",
                            leadingIcon = ImageVector.vectorResource(R.drawable.nama),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isError = namaError != null,
                            errorMessage = namaError
                        )

                        // Saldo Awal Input
                        AppTextField(
                            value = saldo,
                            onValueChange = { input ->
                                val digits = input.filter { it.isDigit() }
                                saldo = if (digits.isEmpty()) "" else ValidationUtils.formatThousandSeparator(digits)
                                saldoError = if (saldo.isBlank()) "Saldo tidak boleh kosong." else null
                            },
                            externalLabel = "Saldo Awal (Rp) *",
                            placeholder = "Contoh: 1.000.000",
                            leadingIcon = ImageVector.vectorResource(R.drawable.kas),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isError = saldoError != null,
                            errorMessage = saldoError
                        )

                        // Keterangan Input
                        AppTextField(
                            value = keterangan,
                            onValueChange = { keterangan = it },
                            externalLabel = "Keterangan",
                            placeholder = "Tambahkan catatan singkat...",
                            leadingIcon = ImageVector.vectorResource(R.drawable.edit),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            )
                        )

                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            color = Color(0x33BECABE),
                            thickness = 1.dp
                        )

                        // Aktif Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Aktif",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Text(
                                    text = "Status akun saat ini",
                                    fontSize = 14.sp,
                                    color = TextMuted
                                )
                            }

                            Switch(
                                checked = aktif,
                                onCheckedChange = { aktif = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = GreenPrimary,
                                    uncheckedThumbColor = Color(0xFF94A3B8),
                                    uncheckedTrackColor = Color(0xFFE2E8F0),
                                    uncheckedBorderColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }

        // ── FIXED FOOTER ACTION SECTION ──────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    spotColor = Color(0x0D1E2430),
                    ambientColor = Color(0x0D1E2430)
                )
                .background(Color.White)
                .border(BorderStroke(1.dp, BorderSlate))
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Tombol Simpan (Full Width)
            Button(
                onClick = {
                    val nameVal = ValidationUtils.validateKasName(nama)
                    namaError = nameVal.errorMessage
                    
                    if (saldo.isBlank()) {
                        saldoError = "Saldo tidak boleh kosong."
                    }

                    if (nameVal.isValid && saldoError == null) {
                        val saldoDouble = ValidationUtils.parseThousandSeparator(saldo)
                        onSaveClick(nama, saldoDouble, keterangan, aktif)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = isEnabled,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = Color.White,
                    disabledContainerColor = GreenPrimary.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.8f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Simpan Akun Kas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.28.sp
                    )
                }
            }
        }
    }
}
