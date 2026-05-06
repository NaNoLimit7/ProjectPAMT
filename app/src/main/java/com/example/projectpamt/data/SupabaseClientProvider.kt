package com.example.projectpamt.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install

object SupabaseClientProvider {

    /*
     * object digunakan agar Supabase client cukup dibuat satu kali.
     * Ini mirip singleton sederhana di Kotlin.
     */
//    val client = createSupabaseClient(
//        supabaseUrl = "https://vlztibrknxtahkhaymgp.supabase.co",
//        supabaseKey = "sb_publishable_634vuzEDNyyuy6trrAcL7A_jCeBZs6t"
//    ) {
//        /*
//         * install(Auth) digunakan agar aplikasi bisa memakai fitur autentikasi,
//         * seperti login, register, logout, dan membaca session user.
//         */
//        install(Auth)
//    }

    val client = createSupabaseClient(
        supabaseUrl = "https://cgtckjopukymmnbgycpo.supabase.co",
        supabaseKey = "sb_publishable_PVimPrwNrYYG7BS4GkEvpg_Ud4x_5vc"
    ) {
        install(Auth)
    }
}