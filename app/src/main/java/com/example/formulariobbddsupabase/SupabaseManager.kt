package com.example.formulariobbddsupabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseManager {

    private const val SUPABASE_URL = "https://cihmwhydvxhmzbuphvry.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNpaG13aHlkdnhobXpidXBodnJ5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzY4MTU2MzUsImV4cCI6MjA5MjM5MTYzNX0.OcAF64Y2k-YxogKZmKNOpl_ISzxKhPPtFZU0GoT73ow"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
    }
}
