package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.viewmodel.labarugi.LabaRugiState
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class LabaRugiRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getLabaRugi(startDateIso: String, endDateIso: String): LabaRugiState {
        return supabase.postgrest.rpc(
            "get_laba_rugi",
            buildJsonObject {
                put("p_start_date", startDateIso)
                put("p_end_date", endDateIso)
            }
        ).decodeAs<LabaRugiState>()
    }
}

