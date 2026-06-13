package com.example.projectpamt.ui.screens.home.produk.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.utils.ValidationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahKategoriBottomSheet(
    categories: List<com.example.projectpamt.data.model.Kategori>,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCategoryName by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf<String?>(null) }

    val isCategorySaveEnabled = newCategoryName.isNotBlank() && categoryError == null

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tambah Kategori Baru",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            AppTextField(
                value = newCategoryName,
                onValueChange = { input ->
                    newCategoryName = input
                    val currentNames = categories.map { it.name }
                    categoryError = ValidationUtils.validateCategoryName(input, currentNames).errorMessage
                },
                externalLabel = "Nama Kategori *",
                placeholder = "Masukkan nama kategori baru",
                isError = categoryError != null,
                errorMessage = categoryError,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF1F5F9),
                        contentColor = Color(0xFF6B7280)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Batal", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val currentNames = categories.map { it.name }
                        val validation = ValidationUtils.validateCategoryName(newCategoryName, currentNames)
                        if (validation.isValid) {
                            onSave(newCategoryName.trim())
                        } else {
                            categoryError = validation.errorMessage
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = isCategorySaveEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007A45),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF007A45).copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.8f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Simpan", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
