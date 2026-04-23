package com.example.formulariobbddsupabase

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var pantallaActual by remember { mutableStateOf("formulario") }

            if (pantallaActual == "formulario") {
                FormularioScreen(
                    onVerSolicitudes = { pantallaActual = "lista" }
                )
            } else {
                MisSolicitudesScreen(
                    onVolver = { pantallaActual = "formulario" }
                )
            }
        }

    }
}

@Composable
fun FormularioScreen(onVerSolicitudes: () -> Unit) {

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val tituloValido = titulo.length in 5..60
    val descripcionValida = descripcion.length in 20..500
    val emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val prioridadValida = prioridad.toIntOrNull()?.let { it in 1..5 } == true

    val formularioValido =
        tituloValido && descripcionValida && emailValido && prioridadValida && categoria.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            isError = !tituloValido && titulo.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            isError = !descripcionValida && descripcion.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = prioridad,
            onValueChange = { prioridad = it },
            label = { Text("Prioridad (1-5)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = !prioridadValida && prioridad.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email destinatario") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = !emailValido && email.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                enviando = true

                scope.launch {
                    try {
                        val formulario = Formulario(
                            titulo = titulo,
                            descripcion = descripcion,
                            categoria = categoria,
                            prioridad = prioridad.toInt(),
                            email_destinatario = email,
                            estado = "Enviado"
                        )


                        SupabaseManager.client.postgrest["formularios"]
                            .insert(formulario)

                        // Limpiar formulario
                        titulo = ""
                        descripcion = ""
                        categoria = ""
                        prioridad = ""
                        email = ""

                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        enviando = false
                    }
                }
            },
            enabled = formularioValido && !enviando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (enviando) "Enviando..." else "Enviar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVerSolicitudes,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver mis solicitudes")
        }

    }
}

suspend fun obtenerSolicitudes(): List<Formulario> {
    return SupabaseManager.client
        .postgrest["formularios"]
        .select()
        .decodeList<Formulario>()
}

@Composable
fun MisSolicitudesScreen(onVolver: () -> Unit) {

    var solicitudes by remember { mutableStateOf<List<Formulario>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                solicitudes = obtenerSolicitudes()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cargando = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text("Mis Solicitudes", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("<- Volver al formulario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(solicitudes) { solicitud ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Título: ${solicitud.titulo}")
                            Text("Categoría: ${solicitud.categoria}")
                            Text("Prioridad: ${solicitud.prioridad}")
                            Text("Estado: ${solicitud.estado}")
                        }
                    }
                }
            }
        }
    }
}

