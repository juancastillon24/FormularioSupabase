package com.example.formulariobbddsupabase

import kotlinx.serialization.Serializable

@Serializable
data class Formulario(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: Int,
    val email_destinatario: String,
    val estado: String = "Enviado"
)
