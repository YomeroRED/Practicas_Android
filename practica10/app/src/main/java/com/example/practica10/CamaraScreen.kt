package com.example.practica10

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamaraScreen() {
    val context = LocalContext.current

    // Lista observable de fotos (Bitmaps)
    var listaImagenes by remember { mutableStateOf(listOf<Bitmap>()) }

    // Para mostrar el estado del permiso en pantalla
    var permisoConcedido by remember {
        mutableStateOf(tienePermisoCamara(context))
    }

    // Si el permiso se pidió desde el FAB, abrir la cámara apenas se conceda
    var abrirCamaraAlConceder by remember { mutableStateOf(false) }

    // Lanzador de la cámara: devuelve el Bitmap capturado
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            listaImagenes = listaImagenes + bitmap
        }
    }

    // Lanzador del permiso en tiempo de ejecución
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido: Boolean ->
        permisoConcedido = concedido
        if (concedido) {
            Toast.makeText(context, "Permiso concedido", Toast.LENGTH_SHORT).show()
            if (abrirCamaraAlConceder) camaraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
        abrirCamaraAlConceder = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cámara y Galería") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // RETO: verificar el permiso antes de abrir la cámara
                if (tienePermisoCamara(context)) {
                    camaraLauncher.launch(null)
                } else {
                    abrirCamaraAlConceder = true
                    permisoLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Tomar foto")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (permisoConcedido) "Permiso de cámara: concedido"
                else "Permiso de cámara: no concedido"
            )

            // Botón explícito para solicitar el permiso
            Button(
                onClick = {
                    if (tienePermisoCamara(context)) {
                        permisoConcedido = true
                        Toast.makeText(context, "El permiso ya estaba concedido", Toast.LENGTH_SHORT).show()
                    } else {
                        permisoLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Solicitar permiso de cámara")
            }

            if (listaImagenes.isEmpty()) {
                Text("No hay fotos en la galería. ¡Toma una!")
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaImagenes) { bitmap ->
                        ImagenCapturadaItem(bitmap = bitmap)
                    }
                }
            }
        }
    }
}

private fun tienePermisoCamara(context: android.content.Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED

@Composable
fun ImagenCapturadaItem(bitmap: Bitmap) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Convertimos el Bitmap a ImageBitmap de Compose
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Foto capturada",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}