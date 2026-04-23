
### 🔹 `Formulario.kt`
Modelo de datos serializable.

### 🔹 `SupabaseManager.kt`
Inicializa el cliente Supabase con:

- Project URL
- anon public key
- Postgrest

### 🔹 `MainActivity.kt`
Contiene:
- Pantalla del formulario
- Pantalla "Mis Solicitudes"
- Navegación simple con estado
- Inserción en base de datos
- Consulta de registros

---

## 📄 Pantallas implementadas

### 1️⃣ Formulario
- Introducción de datos
- Validación automática
- Inserción en Supabase

### 2️⃣ Mis Solicitudes
- Consulta con `select()`
- Lista dinámica con `LazyColumn`
- Indicador de carga
- Botón para volver al formulario

---

## 🔄 Flujo de la aplicación

1. El usuario completa el formulario.
2. Se validan los campos.
3. Si todo es correcto:
   - Se muestra estado "Enviando..."
   - Se insertan datos en Supabase.
4. El usuario puede acceder a "Mis Solicitudes".
5. Se recuperan y muestran los registros almacenados.

---

## 📦 Dependencias principales

```kotlin
implementation("io.github.jan-tennert.supabase:postgrest-kt:2.5.0")
implementation("io.ktor:ktor-client-android:2.3.7")
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
```text

Plugin necesario:

```kotlin
id("org.jetbrains.kotlin.plugin.serialization")
```text

Permiso en `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```text

---

## ✅ Estado actual del proyecto

✔ Inserción de datos funcional  
✔ Consulta de registros funcional  
✔ Validaciones completas  
✔ Navegación entre pantallas  
✔ Gestión de estado visual  
✔ Integración correcta con Supabase  

---

## 👨‍💻 Autor

Proyecto desarrollado como práctica de integración de Android + Supabase.

---
