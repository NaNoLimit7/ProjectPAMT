package com.example.projectpamt.ui.screens.home.kas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.ui.utils.ValidationUtils
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.ui.components.KasCard
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenMintActive
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.utils.formatRupiah
import com.example.projectpamt.viewmodel.kas.KasUiState
import com.example.projectpamt.viewmodel.kas.KasViewModel

// ─── Screen (stateful) ──────────────────────────────────────────────────────

@Composable
fun KasScreen(
    modifier: Modifier = Modifier,
    viewModel: KasViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Dialog states
    var showAddDialog by remember { mutableStateOf(false) }
    var newKasNama by remember { mutableStateOf("") }
    var newKasSaldo by remember { mutableStateOf("") }
    var newKasNamaError by remember { mutableStateOf<String?>(null) }
    var newKasSaldoError by remember { mutableStateOf<String?>(null) }

    var editingKas by remember { mutableStateOf<Kas?>(null) }
    var editKasNama by remember { mutableStateOf("") }
    var editKasNamaError by remember { mutableStateOf<String?>(null) }

    var showInfoDialog by remember { mutableStateOf(false) }
    var infoDialogTitle by remember { mutableStateOf("") }
    var infoDialogMessage by remember { mutableStateOf("") }

    val isAddConfirmEnabled = newKasNama.isNotBlank() && newKasSaldo.isNotBlank() &&
            newKasNamaError == null && newKasSaldoError == null

    val isEditConfirmEnabled = editKasNama.isNotBlank() && editKasNamaError == null

    // Dialog Tambah Kas
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { 
                showAddDialog = false 
                newKasNama = ""
                newKasSaldo = ""
                newKasNamaError = null
                newKasSaldoError = null
            },
            title = { Text("Tambah Akun Kas", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppTextField(
                        value = newKasNama,
                        onValueChange = {
                            newKasNama = it
                            newKasNamaError = ValidationUtils.validateKasName(it).errorMessage
                        },
                        label = "Nama Kas",
                        isError = newKasNamaError != null,
                        errorMessage = newKasNamaError
                    )
                    AppTextField(
                        value = newKasSaldo,
                        onValueChange = {
                            newKasSaldo = it
                            newKasSaldoError = ValidationUtils.validateKasSaldo(it).errorMessage
                        },
                        label = "Saldo Awal (Rp)",
                        isError = newKasSaldoError != null,
                        errorMessage = newKasSaldoError
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nameVal = ValidationUtils.validateKasName(newKasNama)
                        val saldoVal = ValidationUtils.validateKasSaldo(newKasSaldo)
                        newKasNamaError = nameVal.errorMessage
                        newKasSaldoError = saldoVal.errorMessage
                        
                        if (nameVal.isValid && saldoVal.isValid) {
                            val saldoDouble = newKasSaldo.toDoubleOrNull() ?: 0.0
                            viewModel.addKas(newKasNama, saldoDouble)
                            showAddDialog = false
                            newKasNama = ""
                            newKasSaldo = ""
                            newKasNamaError = null
                            newKasSaldoError = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    enabled = isAddConfirmEnabled
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showAddDialog = false 
                    newKasNama = ""
                    newKasSaldo = ""
                    newKasNamaError = null
                    newKasSaldoError = null
                }) {
                    Text("Batal")
                }
            }
        )
    }

    // Dialog Edit Kas
    if (editingKas != null) {
        AlertDialog(
            onDismissRequest = { 
                editingKas = null 
                editKasNama = ""
                editKasNamaError = null
            },
            title = { Text("Ubah Nama Kas", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppTextField(
                        value = editKasNama,
                        onValueChange = {
                            editKasNama = it
                            editKasNamaError = ValidationUtils.validateKasName(it).errorMessage
                        },
                        label = "Nama Kas Baru",
                        isError = editKasNamaError != null,
                        errorMessage = editKasNamaError
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nameVal = ValidationUtils.validateKasName(editKasNama)
                        editKasNamaError = nameVal.errorMessage
                        
                        if (nameVal.isValid) {
                            editingKas?.idKas?.let { id ->
                                viewModel.updateNamaKas(id, editKasNama)
                                editingKas = null
                                editKasNama = ""
                                editKasNamaError = null
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    enabled = isEditConfirmEnabled
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    editingKas = null 
                    editKasNama = ""
                    editKasNamaError = null
                }) {
                    Text("Batal")
                }
            }
        )
    }

    // Info Dialog (mock action status)
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text(infoDialogTitle, fontWeight = FontWeight.Bold) },
            text = { Text(infoDialogMessage) },
            confirmButton = {
                Button(
                    onClick = { showInfoDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("OK")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllActiveKas()
    }

    KasContent(
        modifier = modifier,
        uiState = uiState,
        onAddKasClick = { showAddDialog = true },
        onTransaksiClick = { kas ->
            infoDialogTitle = "Transaksi Kas"
            infoDialogMessage = "Membuka halaman transaksi untuk ${kas.nama}. Saldo saat ini: ${formatRupiah(kas.saldo)}."
            showInfoDialog = true
        },
        onLihatLogClick = { kas ->
            navController.navigate(com.example.projectpamt.ui.navigation.LogKas(kas))
        },
        onEditClick = { kas ->
            editKasNama = kas.nama
            editingKas = kas
        },
        onLogTotalClick = {
            navController.navigate(com.example.projectpamt.ui.navigation.LogTotalKas)
        }
    )
}

// ─── Content (stateless) ────────────────────────────────────────────────────

@Composable
private fun KasContent(
    modifier: Modifier = Modifier,
    uiState: KasUiState,
    onAddKasClick: () -> Unit,
    onTransaksiClick: (Kas) -> Unit,
    onLihatLogClick: (Kas) -> Unit,
    onEditClick: (Kas) -> Unit,
    onLogTotalClick: () -> Unit
) {
    // Menghitung total saldo kas secara dinamis dari akun kas yang aktif
    val totalSaldo = when (uiState) {
        is KasUiState.Success -> uiState.data.filter { it.aktif }.sumOf { it.saldo }
        else -> 0.0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── 1. HEADER GREEN AREA (Judul & Bento Card) ──────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = GreenPrimary,
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Judul Layar & Ikon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Manajemen Kas",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Kelola akun kas Anda",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // Wallet Icon
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.kas),
                            contentDescription = "Kas",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Bento Card Total Saldo (Glassmorphism)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Total Saldo Kas",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = formatRupiah(totalSaldo),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = (-0.9).sp
                            )
                        }

                        // Tombol Log Total
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(34.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GreenMintActive)
                                .clickable { onLogTotalClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.history),
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Log Total",
                                    fontSize = 14.sp,
                                    color = GreenPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // ── 2. SUB-HEADER AREA ("Akun Kas" & "+ Tambah Kas") ────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Akun Kas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    // Button Tambah Kas (Pill Shape)
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(9999.dp))
                            .background(GreenPrimary)
                            .clickable { onAddKasClick() }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.add),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Tambah Kas",
                                fontSize = 14.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // ── 3. LIST OF CASH ACCOUNTS ───────────────────────────────────────
            item {
                when (uiState) {
                    is KasUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = GreenPrimary)
                        }
                    }

                    is KasUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.message,
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }

                    is KasUiState.Success -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            uiState.data.forEach { kas ->
                                KasCard(
                                    kas = kas,
                                    onTransaksiClick = { onTransaksiClick(kas) },
                                    onLihatLogClick = { onLihatLogClick(kas) },
                                    onEditClick = { onEditClick(kas) }
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
