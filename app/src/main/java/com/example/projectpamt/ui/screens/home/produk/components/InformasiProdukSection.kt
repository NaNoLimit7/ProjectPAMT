package com.example.projectpamt.ui.screens.home.produk.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.ui.theme.GreenPrimary

@Composable
fun InformasiProdukSection(
    nama: String,
    onNamaChange: (String) -> Unit,
    namaError: String?,
    sku: String,
    onSkuChange: (String) -> Unit,
    skuError: String?,
    kategori: String,
    onKategoriChange: (String) -> Unit,
    categories: List<Kategori>,
    onAddCategoryClick: () -> Unit,
    deskripsi: String,
    onDeskripsiChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showKategoriDropdown by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Informasi Produk",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181D18)
            )

            HorizontalDivider(color = Color(0x1ABECABE), thickness = 1.dp)

            AppTextField(
                value = nama,
                onValueChange = onNamaChange,
                externalLabel = "Nama Produk",
                placeholder = "Contoh: Kopi Susu Aren",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                isError = namaError != null,
                errorMessage = namaError
            )

            AppTextField(
                value = sku,
                onValueChange = onSkuChange,
                externalLabel = "SKU (Stock Keeping Unit)",
                placeholder = "Contoh: KSA-01",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next
                ),
                isError = skuError != null,
                errorMessage = skuError
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Kategori",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF3E4940),
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                BorderStroke(1.dp, Color(0xFFBECABE)),
                                RoundedCornerShape(12.dp)
                            )
                            .background(Color.White)
                            .clickable { showKategoriDropdown = true }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = kategori.ifBlank { "Pilih Kategori" },
                            color = if (kategori.isBlank()) Color(0xFF6B7280) else Color.Black,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFFBECABE),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showKategoriDropdown,
                        onDismissRequest = { showKategoriDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    onKategoriChange(cat.name)
                                    showKategoriDropdown = false
                                }
                            )
                        }

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "+ Tambah Kategori Baru",
                                    color = GreenPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            onClick = {
                                showKategoriDropdown = false
                                onAddCategoryClick()
                            }
                        )
                    }
                }
            }

            AppTextField(
                value = deskripsi,
                onValueChange = onDeskripsiChange,
                externalLabel = "Deskripsi",
                placeholder = "Tulis deskripsi produk...",
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Default
                )
            )
        }
    }
}
