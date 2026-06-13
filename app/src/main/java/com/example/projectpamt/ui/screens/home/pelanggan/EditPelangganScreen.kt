package com.example.projectpamt.ui.screens.home.pelanggan

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
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.utils.ValidationUtils
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
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.viewmodel.pelanggan.PelangganUiState
import com.example.projectpamt.viewmodel.pelanggan.PelangganViewModel

@Composable
fun EditPelangganScreen(
    pelanggan: Pelanggan,
    modifier: Modifier = Modifier,
    viewModel: PelangganViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState is PelangganUiState.Loading

    EditPelangganContent(
        pelanggan = pelanggan,
        modifier = modifier,
        isLoading = isLoading,
        onBackClick = { navController.popBackStack() },
        onDeleteClick = {
            pelanggan.idPelanggan?.let { id ->
                viewModel.deletePelanggan(id) {
                    navController.popBackStack()
                }
            }
        },
        onSaveClick = { nama, telepon, aktif ->
            pelanggan.idPelanggan?.let { id ->
                viewModel.updatePelanggan(id, nama, telepon, aktif) {
                    navController.popBackStack()
                }
            }
        }
    )
}

@Composable
private fun EditPelangganContent(
    pelanggan: Pelanggan,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: (String, String, Boolean) -> Unit
) {
    var nama by remember { mutableStateOf(pelanggan.nama) }
    var telepon by remember { mutableStateOf(pelanggan.telepon) }
    var aktif by remember { mutableStateOf(pelanggan.aktif) }
    var namaError by remember { mutableStateOf<String?>(null) }
    var teleponError by remember { mutableStateOf<String?>(null) }

    val isChanged = remember(nama, telepon, aktif, pelanggan) {
        val cleanPhone = telepon.replace("-", "").replace(" ", "")
        val cleanOriginalPhone = pelanggan.telepon.replace("-", "").replace(" ", "")
        nama.trim() != pelanggan.nama.trim() ||
                cleanPhone != cleanOriginalPhone ||
                aktif != pelanggan.aktif
    }
    val isEnabled = remember(nama, telepon, namaError, teleponError, isChanged, isLoading) {
        nama.isNotBlank() &&
                telepon.isNotBlank() &&
                namaError == null &&
                teleponError == null &&
                isChanged &&
                !isLoading
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
            // ── HEADER SECTION ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        spotColor = Color(0x1A000000),
                        ambientColor = Color(0x1A000000)
                    )
                    .shadow(
                        elevation = 6.dp,
                        spotColor = Color(0x1A000000),
                        ambientColor = Color(0x1A000000)
                    )
                    .background(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
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
                        fontWeight = FontWeight(400),
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                // Title Area
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Edit Pelanggan",
                        fontSize = 24.sp,
                        fontWeight = FontWeight(700),
                        color = Color.White
                    )
                    Text(
                        text = pelanggan.nama,
                        fontSize = 14.sp,
                        color = Color(0xFFDBEAFE),
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
                // CARD 1: Data Pelanggan Form
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
                    border = BorderStroke(1.dp, Color(0x4DBECABE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 17.dp, horizontal = 17.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title: Data Pelanggan with Icon
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.profil),
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Data Pelanggan",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF181D18)
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0x33BECABE),
                                thickness = 1.dp
                            )
                        }

                        // Nama Lengkap Input
                        AppTextField(
                            value = nama,
                            onValueChange = {
                                nama = it
                                namaError = ValidationUtils.validateName(it).errorMessage
                            },
                            externalLabel = "Nama Lengkap *",
                            placeholder = "Masukkan nama lengkap",
                            leadingIcon = ImageVector.vectorResource(R.drawable.nama),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isError = namaError != null,
                            errorMessage = namaError
                        )

                        // Nomor Telepon Input
                        AppTextField(
                            value = telepon,
                            onValueChange = {
                                telepon = it
                                teleponError = ValidationUtils.validatePhone(it).errorMessage
                            },
                            externalLabel = "Nomor Telepon *",
                            placeholder = "Contoh: 0812-3456-7890",
                            leadingIcon = ImageVector.vectorResource(R.drawable.telepon),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done
                            ),
                            isError = teleponError != null,
                            errorMessage = teleponError
                        )
                    }
                }

                // CARD 2: Status Pelanggan Toggle
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
                    border = BorderStroke(1.dp, Color(0x4DBECABE)),
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
                                text = "Pelanggan Aktif",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF181D18)
                            )
                            Text(
                                text = "Dapat melakukan transaksi",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )
                        }

                        Switch(
                            checked = aktif,
                            onCheckedChange = { aktif = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = GreenPrimary,
                                uncheckedThumbColor = Color(0xFF9CA3AF),
                                uncheckedTrackColor = Color(0xFFE5E7EB),
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }

        // ── FIXED FOOTER ACTION SECTION ──────────────────────────────────────
        Box(
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    spotColor = Color(0x0D1E2430),
                    ambientColor = Color(0x0D1E2430)
                )
                .background(Color.White)
                .border(BorderStroke(1.dp, Color(0x33BECABE)))
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
                    modifier = Modifier
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFBA1A1A)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFFBA1A1A)
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
                            tint = Color(0xFFBA1A1A),
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
                        val namaVal = ValidationUtils.validateName(nama)
                        val teleponVal = ValidationUtils.validatePhone(telepon)
                        
                        namaError = namaVal.errorMessage
                        teleponError = teleponVal.errorMessage
                        
                        if (namaVal.isValid && teleponVal.isValid) {
                            onSaveClick(nama, telepon, aktif)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .shadow(
                            elevation = 6.dp,
                            spotColor = Color(0x1A2563EB),
                            ambientColor = Color(0x1A2563EB)
                        )
                        .shadow(
                            elevation = 15.dp,
                            spotColor = Color(0x332563EB),
                            ambientColor = Color(0x332563EB)
                        ),
                    enabled = isEnabled,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007A45),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF007A45).copy(alpha = 0.5f),
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
