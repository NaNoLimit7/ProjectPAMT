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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.projectpamt.ui.screens.home.produk.components.FotoProdukSection
import com.example.projectpamt.ui.screens.home.produk.components.HargaStokSection
import com.example.projectpamt.ui.screens.home.produk.components.InformasiProdukSection
import com.example.projectpamt.ui.screens.home.produk.components.TambahKategoriBottomSheet
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.utils.ValidationUtils
import com.example.projectpamt.viewmodel.kategori.KategoriUiState
import com.example.projectpamt.viewmodel.kategori.KategoriViewModel
import com.example.projectpamt.viewmodel.produk.ProdukUiState
import com.example.projectpamt.viewmodel.produk.ProdukViewModel
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Composable
fun TambahProdukScreen(
    modifier: Modifier = Modifier,
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

    TambahProdukContent(
        modifier = modifier,
        isLoading = isLoading,
        categories = categories,
        onBackClick = { navController.popBackStack() },
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
            produkViewModel.addProduk(
                nama = nama,
                harga = hargaJual,
                stok = stok,
                namaSatuan = satuan,
                detailProduk = detailJson,
                onSuccess = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "need_refresh",
                        true
                    )
                    navController.popBackStack()
                }
            )
        },
        onAddCategorySave = { newCategoryName ->
            kategoriViewModel.addKategori(newCategoryName)
        }
    )
}

@Composable
private fun TambahProdukContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    categories: List<com.example.projectpamt.data.model.Kategori>,
    onBackClick: () -> Unit,
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
    var nama by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var hargaModal by remember { mutableStateOf("") }
    var hargaJual by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var satuan by remember { mutableStateOf("Pcs") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var namaError by remember { mutableStateOf<String?>(null) }
    var skuError by remember { mutableStateOf<String?>(null) }
    var hargaModalError by remember { mutableStateOf<String?>(null) }
    var hargaJualError by remember { mutableStateOf<String?>(null) }
    var stokError by remember { mutableStateOf<String?>(null) }

    var showBottomSheet by remember { mutableStateOf(false) }

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
            !isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {

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
                    text = "Tambah Produk",
                    fontSize = 24.sp,
                    fontWeight = FontWeight(700),
                    color = Color.White
                )
            }
        }


        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            FotoProdukSection(
                selectedImageUri = selectedImageUri,
                onImageSelected = { selectedImageUri = it }
            )


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
                onSatuanChange = { satuan = it }
            )
        }


        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(BorderStroke(1.dp, Color(0x33BECABE)))
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
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
                    .height(48.dp),
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
                        text = "Simpan Produk",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.28.sp
                    )
                }
            }
        }
    }


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
