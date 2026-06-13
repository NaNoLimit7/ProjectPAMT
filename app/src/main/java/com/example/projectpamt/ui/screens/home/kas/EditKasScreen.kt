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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.DangerRed
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.utils.ValidationUtils
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.kas.uistate.KasUiState
import com.example.projectpamt.viewmodel.kas.KasViewModel

@Composable
fun EditKasScreen(
    kas: Kas,
    modifier: Modifier = Modifier,
    viewModel: KasViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState is KasUiState.Loading

    var showDeleteDialog by remember { mutableStateOf(false) }

    EditKasContent(
        kas = kas,
        modifier = modifier,
        isLoading = isLoading,
        onBackClick = { navController.popBackStack() },
        onDeleteClick = { showDeleteDialog = true },
        onSaveClick = { nama, aktif, keterangan ->
            kas.idKas?.let { id ->
                viewModel.updateKas(id, nama, aktif, keterangan) {
                    navController.previousBackStackEntry?.savedStateHandle?.set("need_refresh", true)
                    navController.popBackStack()
                }
            }
        }
    )

    if (showDeleteDialog) {
        var deleteReason by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false 
                deleteReason = ""
            },
            title = { Text("Nonaktifkan Akun Kas", fontWeight = FontWeight.Bold, color = TextDark) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Apakah Anda yakin ingin menonaktifkan akun kas ini? Akun kas yang dinonaktifkan tidak akan muncul pada daftar transaksi aktif.")
                    
                    AppTextField(
                        value = deleteReason,
                        onValueChange = { deleteReason = it },
                        externalLabel = "Alasan Penonaktifan *",
                        placeholder = "Masukkan alasan penonaktifan",
                        leadingIcon = ImageVector.vectorResource(R.drawable.edit),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        kas.idKas?.let { id ->
                            viewModel.softDeleteKas(id, deleteReason) {
                                navController.previousBackStackEntry?.savedStateHandle?.set("need_refresh", true)
                                navController.popBackStack()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                    enabled = deleteReason.isNotBlank()
                ) {
                    Text("Nonaktifkan", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { 
                        showDeleteDialog = false 
                        deleteReason = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF1F5F9),
                        contentColor = Color(0xFF6B7280)
                    )
                ) {
                    Text("Batal")
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun EditKasContent(
    kas: Kas,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: (String, Boolean, String) -> Unit
) {
    var nama by remember { mutableStateOf(kas.nama) }
    var aktif by remember { mutableStateOf(kas.aktif) }
    var keterangan by remember { mutableStateOf("") }
    var namaError by remember { mutableStateOf<String?>(null) }

    val isChanged = remember(nama, aktif, kas) {
        nama.trim() != kas.nama.trim() || aktif != kas.aktif
    }

    val isEnabled = remember(nama, namaError, keterangan, isChanged, isLoading) {
        nama.isNotBlank() && namaError == null && keterangan.isNotBlank() && isChanged && !isLoading
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
                        text = "Edit Akun Kas",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = kas.nama,
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
                                    text = "Data Akun Kas",
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
                            placeholder = "Masukkan nama kas",
                            leadingIcon = ImageVector.vectorResource(R.drawable.nama),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isError = namaError != null,
                            errorMessage = namaError
                        )

                        // Keterangan Perubahan Input
                        AppTextField(
                            value = keterangan,
                            onValueChange = { keterangan = it },
                            externalLabel = "Keterangan Perubahan *",
                            placeholder = "Contoh: Koreksi ejaan nama / penyesuaian status",
                            leadingIcon = ImageVector.vectorResource(R.drawable.edit),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            )
                        )

                        // Saldo Awal Display (Informational/Read-Only)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        ) {
                            Text(
                                text = "Total Saldo Saat Ini",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = TextMuted,
                                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, BorderSlate, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = formatRupiah(kas.saldo),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            }
                        }
                    }
                }

                // CARD 2: Status Kas Toggle
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Kas Aktif",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Dapat digunakan untuk transaksi",
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Hapus (weight = 1)
                Button(
                    onClick = { onDeleteClick() },
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, DangerRed),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = DangerRed
                    ),
                    enabled = !isLoading
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.delete),
                            contentDescription = "Hapus",
                            tint = DangerRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Hapus",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Tombol Simpan Perubahan (weight = 2)
                Button(
                    onClick = {
                        val nameVal = ValidationUtils.validateKasName(nama)
                        namaError = nameVal.errorMessage
                        if (nameVal.isValid) {
                            onSaveClick(nama, aktif, keterangan)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
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
                            text = "Simpan Perubahan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.28.sp
                        )
                    }
                }
            }
        }
    }
}
