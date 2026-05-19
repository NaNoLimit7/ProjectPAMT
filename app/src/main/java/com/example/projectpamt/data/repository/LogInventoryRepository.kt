package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.LogInventory
import io.github.jan.supabase.postgrest.postgrest

class LogInventoryRepository {
    private val supabase = SupabaseClientProvider.client

    //READ: Mengambil semua data log inventory
    suspend fun getAllLogInventory(): List<LogInventory> {
        return supabase.postgrest["log_inventory"].select().decodeList<LogInventory>()
    }
}