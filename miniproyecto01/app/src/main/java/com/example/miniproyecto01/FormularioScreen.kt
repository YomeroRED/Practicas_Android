package com.example.miniproyecto01

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miniproyecto01.data.Estudiante
import com.example.miniproyecto01.data.PreferencesManager

private val carreras = listOf(
    "Ingeniería en Sistemas Computacionales",
    "Ingeniería Civil",
    "Arquitectura",
    "Contaduría Pública",
    "Administración",
    "Derecho"
)

@Composable
fun FormularioScreen() {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    // Al abrir la app se cargan los últimos datos guardados
    val guardado = remember { preferencesManager.obtenerEstudiante() }

    var matricula by remember { mutableStateOf(guardado.matricula) }
    var nombre by remember { mutableStateOf(guardado.nombre) }
    var carrera by remember { mutableStateOf(guardado.carrera) }
    var turno by remember { mutableStateOf(guardado.turno) }
    var activo by remember { mutableStateOf(guardado.activo) }
    var menuAbierto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registro de Estudiantes",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider()

        // Matrícula y nombre
        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it },
            label = { Text("Matrícula") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Carrera (menú desplegable)
        Text("Carrera", style = MaterialTheme.typography.titleMedium)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = carrera,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Seleccionar carrera") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Abrir lista de carreras")
                    }
                }
            )
            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false }
            ) {
                carreras.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            carrera = opcion
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        // Turno (RadioButton)
        Text("Turno", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            listOf("Matutino", "Vespertino").forEach { opcion ->
                RadioButton(
                    selected = turno == opcion,
                    onClick = { turno = opcion }
                )
                Text(
                    text = opcion,
                    modifier = Modifier.clickable { turno = opcion }
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }

        // Estatus (Switch)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Estatus: " + if (activo) "Activo" else "Inactivo",
                fontSize = 16.sp
            )
            Switch(checked = activo, onCheckedChange = { activo = it })
        }

        HorizontalDivider()

        // REGISTRAR: valida, guarda en SharedPreferences y navega con un Intent explícito
        Button(
            onClick = {
                if (matricula.isBlank() || nombre.isBlank() || carrera.isBlank()) {
                    Toast.makeText(
                        context,
                        "Completa matrícula, nombre y carrera",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val estudiante = Estudiante(
                        matricula = matricula.trim(),
                        nombre = nombre.trim(),
                        carrera = carrera,
                        turno = turno,
                        activo = activo
                    )
                    preferencesManager.guardarEstudiante(estudiante)

                    val intent = Intent(context, ConfirmacionActivity::class.java).apply {
                        putExtra(ConfirmacionActivity.EXTRA_MATRICULA, estudiante.matricula)
                        putExtra(ConfirmacionActivity.EXTRA_NOMBRE, estudiante.nombre)
                        putExtra(ConfirmacionActivity.EXTRA_CARRERA, estudiante.carrera)
                        putExtra(ConfirmacionActivity.EXTRA_TURNO, estudiante.turno)
                        putExtra(ConfirmacionActivity.EXTRA_ACTIVO, estudiante.activo)
                    }
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        // LIMPIAR: borra los campos y lo guardado
        OutlinedButton(
            onClick = {
                preferencesManager.limpiar()
                matricula = ""
                nombre = ""
                carrera = ""
                turno = "Matutino"
                activo = true
                Toast.makeText(context, "Formulario limpiado", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar formulario")
        }
    }
}