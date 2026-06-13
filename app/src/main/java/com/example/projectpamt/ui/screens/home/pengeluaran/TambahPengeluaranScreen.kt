package com.example.projectpamt.ui.screens.home.pengeluaran

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.utils.ValidationUtils
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.kas.KasUiState
import com.example.projectpamt.viewmodel.kas.KasViewModel
import com.example.projectpamt.viewmodel.kategori.KategoriViewModel
import com.example.projectpamt.viewmodel.kategori.KategoriUiState
import com.example.projectpamt.viewmodel.pengeluaran.PengeluaranUiState
import com.example.projectpamt.viewmodel.pengeluaran.PengeluaranViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun TambahPengeluaranScreen(
    modifier: Modifier = Modifier,
    pengeluaranViewModel: PengeluaranViewModel,
    kategoriViewModel: KategoriViewModel, // Kept to avoid breaking AppNavHost
    kasViewModel: KasViewModel,
    navController: NavController
) {
    val uiState by pengeluaranViewModel.uiState.collectAsStateWithLifecycle()
    val kasUiState by kasViewModel.uiState.collectAsStateWithLifecycle()
    val kategoriUiState by kategoriViewModel.uiState.collectAsStateWithLifecycle()

    val isLoading = uiState is PengeluaranUiState.Loading

    // Fetch lists
    LaunchedEffect(Unit) {
        kasViewModel.fetchAllActiveKas()
        kategoriViewModel.fetchKategori()
    }

    val activeKas = when (kasUiState) {
        is KasUiState.Success -> (kasUiState as KasUiState.Success).data.filter { it.aktif }
        else -> emptyList()
    }

    val categories = when (kategoriUiState) {
        is KategoriUiState.Success -> (kategoriUiState as KategoriUiState.Success).data
        else -> emptyList()
    }

    TambahPengeluaranContent(
        modifier = modifier,
        isLoading = isLoading,
        activeKas = activeKas,
        categories = categories,
        onBackClick = { navController.popBackStack() },
        onSaveClick = { idKas, idKategori, deskripsi, total, kategoriObj, kasObj ->
            pengeluaranViewModel.addPengeluaran(
                idKategori = idKategori,
                idKas = idKas,
                deskripsi = deskripsi,
                total = total,
                kategori = kategoriObj,
                kas = kasObj
            ) {
                navController.popBackStack()
            }
        }
    )
}

@Composable
private fun TambahPengeluaranContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    activeKas: List<Kas>,
    categories: List<Kategori>,
    onBackClick: () -> Unit,
    onSaveClick: (String, String, String, Double, Kategori, Kas?) -> Unit
) {
    val context = LocalContext.current

    var totalAmount by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }
    var selectedKas by remember { mutableStateOf<Kas?>(null) }
    var selectedKategori by remember(categories) {
        mutableStateOf(categories.firstOrNull { it.idKategori == "5602b255-f3d1-4339-86ff-3da58c0437be" } ?: categories.firstOrNull())
    }

    var expandedKas by remember { mutableStateOf(false) }
    var expandedKategori by remember { mutableStateOf(false) }

    var totalError by remember { mutableStateOf<String?>(null) }
    var deskripsiError by remember { mutableStateOf<String?>(null) }
    var kasError by remember { mutableStateOf<String?>(null) }

    // Date Picker Dialog Setup
    val calendar = remember { Calendar.getInstance() }
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val isEnabled = remember(deskripsi, totalAmount, selectedKas, selectedKategori, deskripsiError, totalError, isLoading) {
        deskripsi.isNotBlank() && totalAmount.isNotBlank() && selectedKas != null && selectedKategori != null &&
                deskripsiError == null && totalError == null && !isLoading
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // ── Scrollable Form Area ─────────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            // ── HEADER SECTION (Fixed style) ──────────────────────────────────
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
                        text = "Tambah Pengeluaran",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // ── FORM SECTION ──────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // CARD 1: Total Nominal
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
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Total Nominal",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        AppTextField(
                            value = totalAmount,
                            onValueChange = { input ->
                                val digits = input.filter { it.isDigit() }
                                totalAmount = if (digits.isEmpty()) "" else ValidationUtils.formatThousandSeparator(digits)
                                totalError = if (totalAmount.isBlank()) "Total nominal tidak boleh kosong." else null
                            },
                            placeholder = "Rp 0",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isError = totalError != null,
                            errorMessage = totalError
                        )
                    }
                }

                // CARD 2: Detail Transaksi
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
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Date field
                        val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(selectedDate)
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AppTextField(
                                value = formattedDate,
                                onValueChange = {},
                                externalLabel = "Tanggal Transaksi",
                                placeholder = "Pilih tanggal",
                                leadingIcon = ImageVector.vectorResource(R.drawable.update_time),
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.clickable { datePickerDialog.show() }
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { datePickerDialog.show() }
                            )
                        }

                        // Keterangan / Deskripsi field
                        AppTextField(
                            value = deskripsi,
                            onValueChange = {
                                deskripsi = it
                                deskripsiError = if (it.isBlank()) "Keterangan tidak boleh kosong." else null
                            },
                            externalLabel = "Keterangan / Deskripsi",
                            placeholder = "Contoh: Bayar Zakat Mal atau Tagihan Listrik",
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            isError = deskripsiError != null,
                            errorMessage = deskripsiError
                        )

                        // Pilih Kategori Dropdown Field
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AppTextField(
                                value = selectedKategori?.name ?: "",
                                onValueChange = {},
                                externalLabel = "Pilih Kategori",
                                placeholder = "Pilih Kategori Pengeluaran",
                                leadingIcon = ImageVector.vectorResource(R.drawable.deskripsi),
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.clickable { expandedKategori = true }
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { expandedKategori = true }
                            )
                            DropdownMenu(
                                expanded = expandedKategori,
                                onDismissRequest = { expandedKategori = false },
                                modifier = Modifier.fillMaxWidth(0.8f).background(Color.White)
                            ) {
                                categories.forEach { kategori ->
                                    DropdownMenuItem(
                                        text = { Text(kategori.name) },
                                        onClick = {
                                            selectedKategori = kategori
                                            expandedKategori = false
                                        }
                                    )
                                }
                            }
                        }

                        // Pilih Kas Dropdown Field
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AppTextField(
                                value = selectedKas?.nama ?: "",
                                onValueChange = {},
                                externalLabel = "Pilih Kas",
                                placeholder = "Pilih Akun Kas",
                                leadingIcon = ImageVector.vectorResource(R.drawable.kas),
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.clickable { expandedKas = true }
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { expandedKas = true }
                            )
                            DropdownMenu(
                                expanded = expandedKas,
                                onDismissRequest = { expandedKas = false },
                                modifier = Modifier.fillMaxWidth(0.8f).background(Color.White)
                            ) {
                                activeKas.forEach { kas ->
                                    DropdownMenuItem(
                                        text = { Text("${kas.nama} (Saldo: ${formatRupiah(kas.saldo)})") },
                                        onClick = {
                                            selectedKas = kas
                                            expandedKas = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Green Info Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFFE6F4EA), shape = RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Informasi",
                        tint = Color(0xFF137333),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Semua pengeluaran yang dicatat akan secara otomatis memotong saldo kas terpilih dan tercatat dalam laporan arus kas bulanan.",
                        fontSize = 12.sp,
                        color = Color(0xFF137333),
                        lineHeight = 18.sp
                    )
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
            Button(
                onClick = {
                    val isDeskripsiValid = deskripsi.isNotBlank()
                    val isTotalValid = totalAmount.isNotBlank()

                    if (!isDeskripsiValid) deskripsiError = "Deskripsi tidak boleh kosong."
                    if (!isTotalValid) totalError = "Total nominal tidak boleh kosong."

                    if (isDeskripsiValid && isTotalValid && selectedKas != null && selectedKategori != null) {
                        val amountDouble = ValidationUtils.parseThousandSeparator(totalAmount)
                        onSaveClick(
                            selectedKas!!.idKas!!,
                            selectedKategori!!.idKategori!!,
                            deskripsi,
                            amountDouble,
                            selectedKategori!!,
                            selectedKas
                        )
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
                        text = "Simpan Pengeluaran",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.28.sp
                    )
                }
            }
        }
    }
}
