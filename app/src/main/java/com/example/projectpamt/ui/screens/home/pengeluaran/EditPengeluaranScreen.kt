package com.example.projectpamt.ui.screens.home.pengeluaran

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.projectpamt.data.model.Pengeluaran
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
import com.example.projectpamt.viewmodel.kategori.KategoriViewModel
import com.example.projectpamt.viewmodel.kategori.KategoriUiState
import com.example.projectpamt.viewmodel.pengeluaran.PengeluaranUiState
import com.example.projectpamt.viewmodel.pengeluaran.PengeluaranViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun EditPengeluaranScreen(
    modifier: Modifier = Modifier,
    pengeluaran: Pengeluaran,
    pengeluaranViewModel: PengeluaranViewModel,
    kategoriViewModel: KategoriViewModel,
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

    var showCancelDialog by remember { mutableStateOf(false) }

    EditPengeluaranContent(
        modifier = modifier,
        pengeluaran = pengeluaran,
        isLoading = isLoading,
        activeKas = activeKas,
        categories = categories,
        onBackClick = { navController.popBackStack() },
        onCancelClick = { showCancelDialog = true },
        onSaveClick = { deskripsi, total, kasId, kasObj, selectedDate, kategoriObj ->
            pengeluaran.idPengeluaran?.let { id ->
                pengeluaranViewModel.updatePengeluaran(
                    idPengeluaran = id,
                    idKategori = kategoriObj.idKategori!!,
                    idKas = kasId,
                    deskripsi = deskripsi,
                    total = total,
                    kategori = kategoriObj,
                    kas = kasObj
                ) {
                    navController.previousBackStackEntry?.savedStateHandle?.set("need_refresh", true)
                    navController.popBackStack()
                }
            }
        }
    )

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = {
                Text(
                    "Batalkan Pengeluaran",
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            },
            text = {
                Text(
                    "Apakah Anda yakin ingin membatalkan pengeluaran ini? Pembatalan pengeluaran akan mengembalikan saldo kas semula sebesar ${
                        formatRupiah(
                            pengeluaran.total
                        )
                    }."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelDialog = false
                        pengeluaran.idPengeluaran?.let { id ->
                            pengeluaranViewModel.deletePengeluaran(id) {
                                navController.previousBackStackEntry?.savedStateHandle?.set("need_refresh", true)
                                navController.popBackStack()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("Ya, Batalkan", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Kembali", color = TextMuted)
                }
            }
        )
    }
}

@Composable
private fun EditPengeluaranContent(
    modifier: Modifier = Modifier,
    pengeluaran: Pengeluaran,
    isLoading: Boolean,
    activeKas: List<Kas>,
    categories: List<Kategori>,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: (String, Double, String, Kas?, Date, Kategori) -> Unit
) {
    val context = LocalContext.current

    var deskripsi by remember { mutableStateOf(pengeluaran.deskripsi ?: "") }
    var totalAmount by remember {
        mutableStateOf(
            ValidationUtils.formatThousandSeparator(
                pengeluaran.total.toInt().toString()
            )
        )
    }
    var selectedKas by remember {
        mutableStateOf<Kas?>(
            pengeluaran.kas ?: activeKas.firstOrNull { it.idKas == pengeluaran.idKas })
    }
    var selectedKategori by remember(categories, pengeluaran.kategori) {
        mutableStateOf(
            pengeluaran.kategori ?: categories.firstOrNull { it.idKategori == pengeluaran.idKategori } ?: categories.firstOrNull()
        )
    }

    // Parse Initial Date
    val initialDate = remember(pengeluaran.createdAt) {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            parser.parse(pengeluaran.createdAt?.substring(0, 19) ?: "") ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }
    var selectedDate by remember { mutableStateOf(initialDate) }

    var expandedKas by remember { mutableStateOf(false) }
    var expandedKategori by remember { mutableStateOf(false) }

    var deskripsiError by remember { mutableStateOf<String?>(null) }
    var totalError by remember { mutableStateOf<String?>(null) }

    // Date Picker Dialog Setup
    val calendar = remember { Calendar.getInstance().apply { time = selectedDate } }
    val datePickerDialog = remember {
        DatePickerDialog(
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

    val isChanged = remember(deskripsi, totalAmount, selectedKas, selectedDate, selectedKategori) {
        val oldAmountFormatted =
            ValidationUtils.formatThousandSeparator(pengeluaran.total.toInt().toString())
        deskripsi != (pengeluaran.deskripsi ?: "") ||
                totalAmount != oldAmountFormatted ||
                selectedKas?.idKas != pengeluaran.idKas ||
                selectedDate.time != initialDate.time ||
                selectedKategori?.idKategori != pengeluaran.idKategori
    }

    val isEnabled = remember(
        deskripsi,
        totalAmount,
        selectedKas,
        selectedKategori,
        deskripsiError,
        totalError,
        isChanged,
        isLoading
    ) {
        deskripsi.isNotBlank() && totalAmount.isNotBlank() && selectedKas != null && selectedKategori != null &&
                deskripsiError == null && totalError == null && isChanged && !isLoading
    }

    // Mock Transaction ID
    val mockTransactionId = remember(pengeluaran.idPengeluaran) {
        "EXP-" + SimpleDateFormat(
            "yyyyMMdd",
            Locale.US
        ).format(initialDate) + "-" + (pengeluaran.idPengeluaran ?: "0").padStart(4, '0')
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
                        text = "Edit Pengeluaran",
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
                // Main Form Card
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
                        // ID Transaksi row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFE6F4EA), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.riwayat_transaksi),
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "ID Transaksi",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Normal
                                )
                                Text(
                                    text = mockTransactionId,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0x33BECABE),
                            thickness = 1.dp
                        )

                        // Deskripsi Pengeluaran Input
                        AppTextField(
                            value = deskripsi,
                            onValueChange = {
                                deskripsi = it
                                deskripsiError =
                                    if (it.isBlank()) "Keterangan tidak boleh kosong." else null
                            },
                            externalLabel = "Deskripsi Pengeluaran",
                            placeholder = "Masukkan deskripsi pengeluaran",
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isError = deskripsiError != null,
                            errorMessage = deskripsiError
                        )

                        // Jumlah Nominal Input
                        AppTextField(
                            value = totalAmount,
                            onValueChange = { input ->
                                val digits = input.filter { it.isDigit() }
                                totalAmount =
                                    if (digits.isEmpty()) "" else ValidationUtils.formatThousandSeparator(
                                        digits
                                    )
                                totalError =
                                    if (totalAmount.isBlank()) "Total nominal tidak boleh kosong." else null
                            },
                            externalLabel = "Jumlah Nominal (Rp)",
                            placeholder = "Rp 0",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isError = totalError != null,
                            errorMessage = totalError
                        )

                        // Pilih Kategori Dropdown
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
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .background(Color.White)
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

                        // Pilih Sumber Dana Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AppTextField(
                                value = selectedKas?.nama ?: "",
                                onValueChange = {},
                                externalLabel = "Pilih Sumber Dana",
                                placeholder = "Pilih Kas Pembayaran",
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
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .background(Color.White)
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

                        // Tanggal Pengeluaran Field
                        val formattedDate =
                            SimpleDateFormat("dd/MM/yyyy", Locale.US).format(selectedDate)
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AppTextField(
                                value = formattedDate,
                                onValueChange = {},
                                externalLabel = "Tanggal Pengeluaran",
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
                        text = "Membatalkan pengeluaran akan mengembalikan saldo ke akun kas terkait. Tindakan ini bersifat permanen dan akan dicatat dalam riwayat audit sistem.",
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
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Button: Simpan Perubahan
                Button(
                    onClick = {
                        if (deskripsi.isNotBlank() && totalAmount.isNotBlank() && selectedKas != null && selectedKategori != null) {
                            val amountDouble = ValidationUtils.parseThousandSeparator(totalAmount)
                            onSaveClick(
                                deskripsi,
                                amountDouble,
                                selectedKas!!.idKas!!,
                                selectedKas,
                                selectedDate,
                                selectedKategori!!
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
                            text = "Simpan Perubahan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.28.sp
                        )
                    }
                }

                // Button: Batalkan Pengeluaran
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    border = BorderStroke(1.dp, DangerRed),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
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
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Batalkan Pengeluaran",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
