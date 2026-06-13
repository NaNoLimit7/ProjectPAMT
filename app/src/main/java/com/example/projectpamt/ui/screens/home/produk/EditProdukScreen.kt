package com.example.projectpamt.ui.screens.home.produk

import android.net.Uri
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.ui.screens.home.produk.components.FotoProdukSection
import com.example.projectpamt.ui.screens.home.produk.components.HargaStokSection
import com.example.projectpamt.ui.screens.home.produk.components.InformasiProdukSection
import com.example.projectpamt.ui.screens.home.produk.components.TambahKategoriBottomSheet
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.utils.ValidationUtils
import com.example.projectpamt.viewmodel.kategori.KategoriUiState
import com.example.projectpamt.viewmodel.kategori.KategoriViewModel
import com.example.projectpamt.viewmodel.produk.ProdukUiState
import com.example.projectpamt.viewmodel.produk.ProdukViewModel
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import androidx.core.net.toUri
import com.example.projectpamt.data.model.Kategori

@Composable
fun EditProdukScreen(
    modifier: Modifier = Modifier,
    produk: Produk,
    produkViewModel: ProdukViewModel,
    kategoriViewModel: KategoriViewModel,
    navController: NavController
) {
    val produkUiState by produkViewModel.uiState.collectAsStateWithLifecycle()
    val kategoriUiState by kategoriViewModel.uiState.collectAsStateWithLifecycle()

    val isLoading = produkUiState is ProdukUiState.Loading

    val categories = remember(kategoriUiState) {
        if (kategoriUiState is KategoriUiState.Success) {
            (kategoriUiState as KategoriUiState.Success).data
        } else {
            emptyList()
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    EditProdukContent(
        modifier = modifier,
        produk = produk,
        isLoading = isLoading,
        categories = categories,
        onBackClick = { navController.popBackStack() },
        onDeleteClick = { showDeleteDialog = true },
        onSaveClick = { nama, sku, kategori, deskripsi, hargaModal, hargaJual, stok, satuan, imageUri ->
            val detailJson = buildJsonObject {
                put("sku", sku)
                put("kategori", kategori)
                put("deskripsi", deskripsi)
                put("harga_modal", hargaModal)
                if (imageUri != null) {
                    put("image_url", imageUri.toString())
                }
            }
            produk.idProduk?.let { id ->
                produkViewModel.updateProduk(
                    id = id,
                    nama = nama,
                    harga = hargaJual,
                    stok = stok,
                    namaSatuan = satuan,
                    detailProduk = detailJson,
                    onSuccess = {
                        navController.previousBackStackEntry?.savedStateHandle?.set("need_refresh", true)
                        navController.popBackStack()
                    }
                )
            }
        },
        onAddCategorySave = { newCategoryName ->
            kategoriViewModel.addKategori(newCategoryName)
        }
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Produk", fontWeight = FontWeight.Bold, color = TextDark) },
            text = { Text("Apakah Anda yakin ingin menghapus produk ini? Produk yang dihapus akan dinonaktifkan dari daftar aktif.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        produk.idProduk?.let { id ->
                            produkViewModel.nonaktifkanProduk(id) {
                                try {
                                    navController.getBackStackEntry<com.example.projectpamt.ui.navigation.ProdukList>().savedStateHandle.set("need_refresh", true)
                                } catch (e: Exception) {
                                    navController.previousBackStackEntry?.savedStateHandle?.set("need_refresh", true)
                                }
                                navController.popBackStack(
                                    route = com.example.projectpamt.ui.navigation.ProdukList,
                                    inclusive = false
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A))
                ) {
                    Text("Hapus", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
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
private fun EditProdukContent(
    modifier: Modifier = Modifier,
    produk: Produk,
    isLoading: Boolean,
    categories: List<Kategori>,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: (
        nama: String,
        sku: String,
        kategori: String,
        deskripsi: String,
        hargaModal: Double,
        hargaJual: Double,
        stok: Double,
        satuan: String,
        imageUri: Uri?
    ) -> Unit,
    onAddCategorySave: (String) -> Unit
) {
    val initialSku = produk.detailProduk?.jsonObject?.get("sku")?.jsonPrimitive?.contentOrNull ?: ""
    val initialKategori = produk.detailProduk?.jsonObject?.get("kategori")?.jsonPrimitive?.contentOrNull ?: ""
    val initialDeskripsi = produk.detailProduk?.jsonObject?.get("deskripsi")?.jsonPrimitive?.contentOrNull ?: ""
    val initialHargaModalDouble = produk.detailProduk?.jsonObject?.get("harga_modal")?.jsonPrimitive?.doubleOrNull ?: 0.0
    val initialHargaModal = if (initialHargaModalDouble > 0) ValidationUtils.formatThousandSeparator(initialHargaModalDouble.toInt().toString()) else ""
    val initialHargaJual = if (produk.harga > 0) ValidationUtils.formatThousandSeparator(produk.harga.toInt().toString()) else ""
    val initialStok = produk.stok.toInt().toString()
    val initialSatuan = produk.namaSatuan
    val initialImageUrl = produk.imageUrl
    val initialImageUri = if (!initialImageUrl.isNullOrBlank()) initialImageUrl.toUri() else null

    var nama by remember { mutableStateOf(produk.nama) }
    var sku by remember { mutableStateOf(initialSku) }
    var kategori by remember { mutableStateOf(initialKategori) }
    var deskripsi by remember { mutableStateOf(initialDeskripsi) }
    var hargaModal by remember { mutableStateOf(initialHargaModal) }
    var hargaJual by remember { mutableStateOf(initialHargaJual) }
    var stok by remember { mutableStateOf(initialStok) }
    var satuan by remember { mutableStateOf(initialSatuan) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(initialImageUri) }

    var namaError by remember { mutableStateOf<String?>(null) }
    var skuError by remember { mutableStateOf<String?>(null) }
    var hargaModalError by remember { mutableStateOf<String?>(null) }
    var hargaJualError by remember { mutableStateOf<String?>(null) }
    var stokError by remember { mutableStateOf<String?>(null) }

    var showBottomSheet by remember { mutableStateOf(false) }

    val isChanged = remember(
        nama, sku, kategori, deskripsi, hargaModal, hargaJual, stok, satuan, selectedImageUri
    ) {
        val cleanHargaModal = hargaModal.replace(".", "").replace(",", "")
        val cleanHargaJual = hargaJual.replace(".", "").replace(",", "")
        
        nama.trim() != produk.nama.trim() ||
                sku.trim() != initialSku.trim() ||
                kategori.trim() != initialKategori.trim() ||
                deskripsi.trim() != initialDeskripsi.trim() ||
                cleanHargaModal != (if (initialHargaModalDouble > 0) initialHargaModalDouble.toLong().toString() else "") ||
                cleanHargaJual != (if (produk.harga > 0) produk.harga.toLong().toString() else "") ||
                stok.toDoubleOrNull() != produk.stok ||
                satuan != produk.namaSatuan ||
                selectedImageUri?.toString() != initialImageUrl
    }

    val isEnabled = nama.isNotBlank() &&
            sku.isNotBlank() &&
            kategori.isNotBlank() &&
            hargaModal.isNotBlank() &&
            hargaJual.isNotBlank() &&
            stok.isNotBlank() &&
            satuan.isNotBlank() &&
            namaError == null &&
            skuError == null &&
            hargaModalError == null &&
            hargaJualError == null &&
            stokError == null &&
            isChanged &&
            !isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // HEADER SECTION (Fixed)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = GreenPrimary,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit Produk",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // SCROLLABLE FORM
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. FOTO PRODUK SECTION
            FotoProdukSection(
                selectedImageUri = selectedImageUri,
                onImageSelected = { selectedImageUri = it }
            )

            // 2. INFORMASI PRODUK SECTION
            InformasiProdukSection(
                nama = nama,
                onNamaChange = {
                    nama = it
                    namaError = ValidationUtils.validateProductName(it).errorMessage
                },
                namaError = namaError,
                sku = sku,
                onSkuChange = {
                    sku = it
                    skuError = ValidationUtils.validateSKU(it).errorMessage
                },
                skuError = skuError,
                kategori = kategori,
                onKategoriChange = { kategori = it },
                categories = categories,
                onAddCategoryClick = { showBottomSheet = true },
                deskripsi = deskripsi,
                onDeskripsiChange = { deskripsi = it }
            )

            // 3. HARGA & STOK SECTION (stokLabel set to "Stok Saat Ini *")
            HargaStokSection(
                hargaModal = hargaModal,
                onHargaModalChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    if (digits.isEmpty()) {
                        hargaModal = ""
                        hargaModalError = ValidationUtils.validatePrice("").errorMessage
                    } else {
                        val formatted = ValidationUtils.formatThousandSeparator(digits)
                        hargaModal = formatted
                        hargaModalError = ValidationUtils.validatePrice(formatted).errorMessage
                    }
                },
                hargaModalError = hargaModalError,
                hargaJual = hargaJual,
                onHargaJualChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    if (digits.isEmpty()) {
                        hargaJual = ""
                        hargaJualError = ValidationUtils.validatePrice("").errorMessage
                    } else {
                        val formatted = ValidationUtils.formatThousandSeparator(digits)
                        hargaJual = formatted
                        hargaJualError = ValidationUtils.validatePrice(formatted).errorMessage
                    }
                },
                hargaJualError = hargaJualError,
                stok = stok,
                onStokChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    stok = digits
                    stokError = ValidationUtils.validateStock(digits).errorMessage
                },
                stokError = stokError,
                satuan = satuan,
                onSatuanChange = { satuan = it },
                stokLabel = "Stok Saat Ini *"
            )
        }

        // FIXED FOOTER ACTION BAR
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
                // Delete Button (Hapus)
                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.height(48.dp),
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

                // Save Button (Simpan Perubahan)
                Button(
                    onClick = {
                        val nVal = ValidationUtils.validateProductName(nama)
                        val sVal = ValidationUtils.validateSKU(sku)
                        val hmVal = ValidationUtils.validatePrice(hargaModal)
                        val hjVal = ValidationUtils.validatePrice(hargaJual)
                        val stVal = ValidationUtils.validateStock(stok)

                        namaError = nVal.errorMessage
                        skuError = sVal.errorMessage
                        hargaModalError = hmVal.errorMessage
                        hargaJualError = hjVal.errorMessage
                        stokError = stVal.errorMessage

                        if (nVal.isValid && sVal.isValid && hmVal.isValid && hjVal.isValid && stVal.isValid) {
                            onSaveClick(
                                nama,
                                sku,
                                kategori,
                                deskripsi,
                                ValidationUtils.parseThousandSeparator(hargaModal),
                                ValidationUtils.parseThousandSeparator(hargaJual),
                                stok.toDoubleOrNull() ?: 0.0,
                                satuan,
                                selectedImageUri
                            )
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

    // CUSTOM CATEGORY ADDITION BOTTOM SHEET
    if (showBottomSheet) {
        TambahKategoriBottomSheet(
            categories = categories,
            onDismiss = { showBottomSheet = false },
            onSave = { newCategoryName ->
                onAddCategorySave(newCategoryName)
                kategori = newCategoryName
                showBottomSheet = false
            }
        )
    }
}
