package com.example.projectpamt.utils

import java.text.NumberFormat.getNumberInstance
import java.util.Locale

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object ValidationUtils {
    private val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    private val phoneRegex = "^\\+?[0-9]{9,15}$".toRegex()
    private val nameRegex = "^[A-Za-z\\s']{3,}$".toRegex()

    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(false, "Email tidak boleh kosong.")
        }
        if (!emailRegex.matches(email)) {
            return ValidationResult(false, "Format email tidak valid.")
        }
        return ValidationResult(true)
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(false, "Kata sandi tidak boleh kosong.")
        }
        if (password.length < 6) {
            return ValidationResult(false, "Kata sandi minimal harus 6 karakter.")
        }
        return ValidationResult(true)
    }

    fun validateName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "Nama tidak boleh kosong.")
        }
        if (name.trim().length < 3) {
            return ValidationResult(false, "Nama harus minimal 3 karakter.")
        }
        if (!nameRegex.matches(name)) {
            return ValidationResult(false, "Nama hanya boleh mengandung huruf.")
        }
        return ValidationResult(true)
    }

    fun validatePhone(phone: String): ValidationResult {
        // Hapus karakter pemisah seperti strip sebelum memvalidasi
        val cleanPhone = phone.replace("-", "").replace(" ", "")
        if (cleanPhone.isBlank()) {
            return ValidationResult(false, "Nomor telepon tidak boleh kosong.")
        }
        if (!phoneRegex.matches(cleanPhone)) {
            return ValidationResult(false, "Nomor telepon harus terdiri dari 9-15 digit angka.")
        }
        return ValidationResult(true)
    }

    fun validateKasName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "Nama kas tidak boleh kosong.")
        }
        if (name.trim().length < 3) {
            return ValidationResult(false, "Nama kas harus minimal 3 karakter.")
        }
        return ValidationResult(true)
    }

    fun validateKasSaldo(saldo: String): ValidationResult {
        if (saldo.isBlank()) {
            return ValidationResult(false, "Saldo tidak boleh kosong.")
        }
        val doubleValue = saldo.toDoubleOrNull()
        if (doubleValue == null || doubleValue < 0.0) {
            return ValidationResult(false, "Saldo harus berupa angka non-negatif.")
        }
        return ValidationResult(true)
    }

    fun formatThousandSeparator(input: String): String {
        val clean = input.replace(".", "").replace(",", "").filter { it.isDigit() }
        if (clean.isEmpty()) return ""
        val number = clean.toLongOrNull() ?: return ""
        return getNumberInstance(Locale.Builder().setLanguage("in").setRegion("ID").build()).format(number)
    }

    fun parseThousandSeparator(input: String): Double {
        val clean = input.replace(".", "").replace(",", "").filter { it.isDigit() }
        return clean.toDoubleOrNull() ?: 0.0
    }

    fun validateSKU(sku: String): ValidationResult {
        if (sku.isBlank()) {
            return ValidationResult(false, "SKU tidak boleh kosong.")
        }
        if (sku.trim().length < 3) {
            return ValidationResult(false, "SKU harus minimal 3 karakter.")
        }
        return ValidationResult(true)
    }

    fun validatePrice(price: String): ValidationResult {
        if (price.isBlank()) {
            return ValidationResult(false, "Harga tidak boleh kosong.")
        }
        val clean = price.replace(".", "").replace(",", "")
        val value = clean.toDoubleOrNull()
        if (value == null || value < 0.0) {
            return ValidationResult(false, "Harga harus berupa angka valid.")
        }
        return ValidationResult(true)
    }

    fun validateStock(stock: String): ValidationResult {
        if (stock.isBlank()) {
            return ValidationResult(false, "Stok tidak boleh kosong.")
        }
        val clean = stock.replace(".", "").replace(",", "")
        val value = clean.toDoubleOrNull()
        if (value == null || value < 0.0) {
            return ValidationResult(false, "Stok harus berupa angka valid.")
        }
        return ValidationResult(true)
    }

    fun validateProductName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "Nama produk tidak boleh kosong.")
        }
        if (name.trim().length < 3) {
            return ValidationResult(false, "Nama produk harus minimal 3 karakter.")
        }
        return ValidationResult(true)
    }

    fun validateCategoryName(name: String, existingNames: List<String>): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "Nama kategori tidak boleh kosong.")
        }
        if (name.trim().length < 3) {
            return ValidationResult(false, "Nama kategori harus minimal 3 karakter.")
        }
        if (existingNames.any { it.equals(name.trim(), ignoreCase = true) }) {
            return ValidationResult(false, "Kategori sudah ada.")
        }
        return ValidationResult(true)
    }
}
