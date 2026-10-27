package com.example.practica11

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.IOException

// En Android 10+ el permiso de escritura ya no aplica
private fun necesitaPermisoEscritura() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.P

private fun tienePermisoEscritura(context: Context) =
    ContextCompat.checkSelfPermission(
        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

@Composable
fun ArchivosScreen() {
    val context = LocalContext.current
    val manager = remember { ArchivoManager(context) }

    var texto by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf(Destino.INTERNO) }
    var mensaje by remember { mutableStateOf("") }
    var contenidoLeido by remember { mutableStateOf("") }
    var permisoConcedido by remember {
        mutableStateOf(!necesitaPermisoEscritura() || tienePermisoEscritura(context))
    }

    // Solicitud del permiso en tiempo de ejecución
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoConcedido = concedido
        Toast.makeText(
            context,
            if (concedido) "Permiso concedido" else "Permiso denegado",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun guardar(agregar: Boolean) {
        if (texto.isBlank()) {
            Toast.makeText(context, "Escribe un texto primero", Toast.LENGTH_SHORT).show()
            return
        }
        if (destino == Destino.EXTERNO && !permisoConcedido) {
            permisoLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }
        try {
            val ruta = manager.guardar(destino, texto, agregar)
            mensaje = (if (agregar) "Texto agregado en:\n" else "Archivo guardado en:\n") + ruta
        } catch (e: IOException) {
            mensaje = "Error: ${e.message}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Práctica 11: Archivos", style = MaterialTheme.typography.headlineMedium)

        HorizontalDivider()

        // Estado del permiso y botón para solicitarlo
        Text(
            if (necesitaPermisoEscritura()) {
                if (permisoConcedido) "Permiso de escritura: concedido"
                else "Permiso de escritura: no concedido"
            } else {
                "Permiso de escritura: no requerido en esta versión de Android"
            }
        )
        OutlinedButton(
            onClick = {
                if (!necesitaPermisoEscritura()) {
                    Toast.makeText(
                        context,
                        "No es necesario: la app usa su carpeta propia",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (tienePermisoEscritura(context)) {
                    permisoConcedido = true
                    Toast.makeText(context, "El permiso ya estaba concedido", Toast.LENGTH_SHORT).show()
                } else {
                    permisoLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Solicitar permiso de almacenamiento")
        }

        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Texto a guardar") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        // Selector de destino
        Text("Destino:", style = MaterialTheme.typography.titleMedium)
        Destino.entries.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { destino = opcion },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = destino == opcion, onClick = { destino = opcion })
                Text(opcion.etiqueta)
            }
        }

        Button(onClick = { guardar(agregar = false) }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar (sobrescribir)")
        }

        Button(onClick = { guardar(agregar = true) }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar al final (modificar)")
        }

        OutlinedButton(
            onClick = {
                try {
                    contenidoLeido = manager.leer(destino)
                    mensaje = "Archivo leído de:\n${manager.archivo(destino)?.absolutePath}"
                } catch (e: IOException) {
                    contenidoLeido = ""
                    mensaje = "Error: ${e.message}"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Leer archivo")
        }

        TextButton(
            onClick = {
                val eliminado = manager.borrar(destino)
                contenidoLeido = ""
                mensaje = if (eliminado) "Archivo eliminado" else "No había archivo que eliminar"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Borrar archivo", color = MaterialTheme.colorScheme.error)
        }

        HorizontalDivider()

        if (mensaje.isNotEmpty()) {
            Text(mensaje, style = MaterialTheme.typography.bodySmall)
        }

        if (contenidoLeido.isNotEmpty()) {
            Text("Contenido del archivo:", style = MaterialTheme.typography.titleMedium)
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(contenidoLeido, modifier = Modifier.padding(16.dp))
            }
        }
    }
}