package com.example.projectpamt.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    private const val ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val ISO_WITHOUT_Z_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    fun parseIso(dateStr: String?): Date? {
        if (dateStr == null) return null
        return try {
            SimpleDateFormat(ISO_FORMAT, Locale("id", "ID")).parse(dateStr)
        } catch (e: Exception) {
            try {
                SimpleDateFormat(ISO_WITHOUT_Z_FORMAT, Locale.US).parse(dateStr)
            } catch (ex: Exception) {
                null
            }
        }
    }

    fun format(date: Date?, pattern: String): String {
        if (date == null) return ""
        return try {
            SimpleDateFormat(pattern, Locale("id", "ID")).format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun formatIso(dateStr: String?, pattern: String): String {
        if (dateStr == null) return ""
        val date = parseIso(dateStr) ?: return dateStr
        return format(date, pattern)
    }
}
