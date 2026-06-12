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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.ui.components.AppTextField

@Composable
fun HargaStokSection(
    hargaModal: String,
    onHargaModalChange: (String) -> Unit,
    hargaModalError: String?,
    hargaJual: String,
    onHargaJualChange: (String) -> Unit,
    hargaJualError: String?,
    stok: String,
    onStokChange: (String) -> Unit,
    stokError: String?,
    satuan: String,
    onSatuanChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSatuanDropdown by remember { mutableStateOf(false) }

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
                text = "Harga & Stok",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181D18)
            )

            HorizontalDivider(color = Color(0x1ABECABE), thickness = 1.dp)

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Harga Modal Input
                    Column(modifier = Modifier.weight(1f)) {
                        AppTextField(
                            value = hargaModal,
                            onValueChange = onHargaModalChange,
                            externalLabel = "Harga Modal",
                            placeholder = "0",
                            leadingIcon = null,
                            trailingIcon = {
                                Text(
                                    text = "Rp",
                                    color = Color(0xFF3E4940),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isError = hargaModalError != null,
                            errorMessage = hargaModalError
                        )
                    }

                    // Harga Jual Input
                    Column(modifier = Modifier.weight(1f)) {
                        AppTextField(
                            value = hargaJual,
                            onValueChange = onHargaJualChange,
                            externalLabel = "Harga Jual",
                            placeholder = "0",
                            leadingIcon = null,
                            trailingIcon = {
                                Text(
                                    text = "Rp",
                                    color = Color(0xFF3E4940),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isError = hargaJualError != null,
                            errorMessage = hargaJualError
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Stok Awal Input
                    Column(modifier = Modifier.weight(1f)) {
                        AppTextField(
                            value = stok,
                            onValueChange = onStokChange,
                            externalLabel = "Stok Awal",
                            placeholder = "0",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            isError = stokError != null,
                            errorMessage = stokError
                        )
                    }

                    // Satuan Dropdown
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Satuan",
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
                                    .clickable { showSatuanDropdown = true }
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = satuan,
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color(0xFFBECABE),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            val unitOptions = listOf("Pcs", "Unit", "Box", "Kg", "Liter", "Meter")
                            DropdownMenu(
                                expanded = showSatuanDropdown,
                                onDismissRequest = { showSatuanDropdown = false },
                                modifier = Modifier.fillMaxWidth(0.4f)
                            ) {
                                unitOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            onSatuanChange(option)
                                            showSatuanDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
