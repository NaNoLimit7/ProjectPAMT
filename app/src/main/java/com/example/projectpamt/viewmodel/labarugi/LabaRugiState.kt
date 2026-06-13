package com.example.projectpamt.viewmodel.labarugi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LabaRugiState(
    @SerialName("total_penjualan") val totalPenjualan: Double = 0.0,
    @SerialName("total_pengeluaran") val totalPengeluaran: Double = 0.0,
    @SerialName("net_profit") val netProfit: Double = 0.0,
    @SerialName("total_penjualan_produk") val totalPenjualanProduk: Double = 0.0,
    @SerialName("count_penjualan_produk") val countPenjualanProduk: Int = 0,
    @SerialName("total_jasa_layanan") val totalJasaLayanan: Double = 0.0,
    @SerialName("count_jasa_layanan") val countJasaLayanan: Int = 0,
    @SerialName("grouped_expenses") val groupedExpenses: List<ExpenseGroup> = emptyList()
)

@Serializable
data class ExpenseGroup(
    @SerialName("category_name") val categoryName: String,
    val total: Double
)
