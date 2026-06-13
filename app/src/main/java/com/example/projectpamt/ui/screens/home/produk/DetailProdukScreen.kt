package com.example.projectpamt.ui.screens.home.produk

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.ui.navigation.EditProduk
import com.example.projectpamt.ui.screens.home.produk.components.DetailBottomBar
import com.example.projectpamt.ui.screens.home.produk.components.DetailDescriptionSection
import com.example.projectpamt.ui.screens.home.produk.components.DetailHargaStokSection
import com.example.projectpamt.ui.screens.home.produk.components.DetailOverviewSection
import com.example.projectpamt.ui.screens.home.produk.components.MetricCard
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.produk.ProdukUiState
import com.example.projectpamt.viewmodel.produk.ProdukViewModel
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun DetailProdukScreen(
    modifier: Modifier = Modifier,
    produk: Produk,
    viewModel: ProdukViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState is ProdukUiState.Loading

    var showDeleteDialog by remember { mutableStateOf(false) }

    DetailProdukContent(
        modifier = modifier,
        produk = produk,
        isLoading = isLoading,
        onBackClick = { navController.popBackStack() },
        onDeleteClick = { showDeleteDialog = true },
        onEditClick = { navController.navigate(EditProduk(produk)) }
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
                            viewModel.nonaktifkanProduk(id) {
                                navController.popBackStack()
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
private fun DetailProdukContent(
    modifier: Modifier = Modifier,
    produk: Produk,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val hargaModalDouble = produk.detailProduk?.jsonObject?.get("harga_modal")?.jsonPrimitive?.doubleOrNull ?: 0.0
    val skuText = produk.detailProduk?.jsonObject?.get("sku")?.jsonPrimitive?.contentOrNull ?: "-"
    val descText = produk.detailProduk?.jsonObject?.get("deskripsi")?.jsonPrimitive?.contentOrNull ?: ""

    // Dynamic Margin Calculation
    val marginPercent = if (produk.harga > 0) {
        val diff = produk.harga - hargaModalDouble
        ((diff / produk.harga) * 100).toInt()
    } else 0

    // Description specs parsing
    val lines = descText.split("\n").map { it.trim() }
    val mainParagraphs = lines.filter { !it.startsWith("-") && !it.startsWith("*") && !it.startsWith("•") }
    val specBullets = lines.filter { it.startsWith("-") || it.startsWith("*") || it.startsWith("•") }
        .map { it.removePrefix("-").removePrefix("*").removePrefix("•").trim() }

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
                    text = "Detail Produk",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // SCROLLABLE CONTENT AREA (LazyColumn)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. PRODUCT OVERVIEW CARD
            item {
                DetailOverviewSection(
                    produk = produk,
                    skuText = skuText
                )
            }

            // 2. METRICS CARDS (2x2 Grid)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MetricCard(
                            iconRes = R.drawable.stok_produk,
                            label = "Stok Saat Ini",
                            value = produk.stok.toInt().toString(),
                            valueSuffix = produk.namaSatuan,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            iconRes = R.drawable.terjual,
                            label = "Total Terjual",
                            value = "0",
                            valueSuffix = produk.namaSatuan,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MetricCard(
                            iconRes = R.drawable.harga_modal,
                            label = "Harga Modal",
                            value = formatRupiah(hargaModalDouble),
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            iconRes = R.drawable.margin,
                            label = "Margin",
                            value = "$marginPercent%",
                            modifier = Modifier.weight(1f),
                            backgroundColor = Color(0xFFE8F5E9),
                            textColor = Color(0xFF007A45),
                            labelColor = Color(0xFF007A45),
                            iconTint = Color(0xFF007A45)
                        )
                    }
                }
            }

            // 3. READ-ONLY HARGA & STOK CARD
            item {
                DetailHargaStokSection(
                    produk = produk,
                    hargaModalDouble = hargaModalDouble
                )
            }

            // 4. DESCRIPTION CARD
            item {
                DetailDescriptionSection(
                    mainParagraphs = mainParagraphs,
                    specBullets = specBullets,
                    createdAt = produk.createdAt,
                    descText = descText
                )
            }
        }

        // 5. FIXED FOOTER ACTION BAR
        DetailBottomBar(
            onDeleteClick = onDeleteClick,
            onEditClick = onEditClick,
            modifier = modifier
        )
    }
}
