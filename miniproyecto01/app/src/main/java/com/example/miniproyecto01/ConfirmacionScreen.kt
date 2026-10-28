package com.example.miniproyecto01

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miniproyecto01.data.Estudiante

@Composable
fun ConfirmacionScreen(estudiante: Estudiante, onVolver: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Registro Exitoso",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DatoFila("Matrícula", estudiante.matricula)
                    DatoFila("Nombre", estudiante.nombre)
                    DatoFila("Carrera", estudiante.carrera)
                    DatoFila("Turno", estudiante.turno)
                    DatoFila("Estatus", if (estudiante.activo) "Activo" else "Inactivo")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}

@Composable
private fun DatoFila(etiqueta: String, valor: String) {
    Column {
        Text(text = etiqueta, style = MaterialTheme.typography.labelLarge)
        Text(text = valor, fontSize = 18.sp)
    }
}