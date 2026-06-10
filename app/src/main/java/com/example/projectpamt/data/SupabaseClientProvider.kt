package com.example.projectpamt.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install

object SupabaseClientProvider {
    val client = createSupabaseClient(
        supabaseUrl = "https://vlztibrknxtahkhaymgp.supabase.co",
        supabaseKey = "sb_publishable_634vuzEDNyyuy6trrAcL7A_jCeBZs6t"
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}