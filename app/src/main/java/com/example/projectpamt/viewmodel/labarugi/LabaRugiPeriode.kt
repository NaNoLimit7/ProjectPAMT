package com.example.projectpamt.viewmodel.labarugi

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class LabaRugiPeriode {
    BULAN_INI,
    BULAN_LALU,
    TIGA_BULAN_TERAKHIR,
    TAHUN_INI,
    SEMUA_WAKTU;

    fun getDisplayName(currentDate: Date = Date()): String {
        val cal = Calendar.getInstance().apply { time = currentDate }
        return when (this) {
            BULAN_INI -> {
                val monthName = SimpleDateFormat("MMMM yyyy", Locale("in", "ID")).format(currentDate)
                "$monthName (Bulan Ini)"
            }
            BULAN_LALU -> {
                cal.add(Calendar.MONTH, -1)
                val monthName = SimpleDateFormat("MMMM yyyy", Locale("in", "ID")).format(cal.time)
                "$monthName (Bulan Lalu)"
            }
            TIGA_BULAN_TERAKHIR -> "3 Bulan Terakhir"
            TAHUN_INI -> "Tahun ${cal.get(Calendar.YEAR)} (Tahun Ini)"
            SEMUA_WAKTU -> "Semua Waktu"
        }
    }
}
