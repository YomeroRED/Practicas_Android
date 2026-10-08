package com.example.miniproyecto02.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miniproyecto02.data.Tarea
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TareaViewModel = viewModel()) {
    val context = LocalContext.current
    val tareas by viewModel.tareas.collectAsState()
    val filtroActual by viewModel.filtro.collectAsState()
    val ordenActual by viewModel.orden.collectAsState()

    var menuFiltro by remember { mutableStateOf(false) }
    var menuOpciones by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var tareaEnEdicion by remember { mutableStateOf<Tarea?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrador de Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    // Menú de filtro
                    Box {
                        IconButton(onClick = { menuFiltro = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                        }
                        DropdownMenu(
                            expanded = menuFiltro,
                            onDismissRequest = { menuFiltro = false }
                        ) {
                            Filtro.values().forEach { filtro ->
                                DropdownMenuItem(
                                    text = { Text(filtro.etiqueta) },
                                    trailingIcon = {
                                        if (filtro == filtroActual) {
                                            Icon(Icons.Default.Check, contentDescription = null)
                                        }
                                    },
                                    onClick = {
                                        viewModel.cambiarFiltro(filtro)
                                        menuFiltro = false
                                    }
                                )
                            }
                        }
                    }

                    // Menú de opciones: ordenar y borrar completadas
                    Box {
                        IconButton(onClick = { menuOpciones = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                        }
                        DropdownMenu(
                            expanded = menuOpciones,
                            onDismissRequest = { menuOpciones = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Ordenar por fecha") },
                                trailingIcon = {
                                    if (ordenActual == Orden.FECHA) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                },
                                onClick = {
                                    viewModel.cambiarOrden(Orden.FECHA)
                                    menuOpciones = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Ordenar por estado") },
                                trailingIcon = {
                                    if (ordenActual == Orden.ESTADO) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                },
                                onClick = {
                                    viewModel.cambiarOrden(Orden.ESTADO)
                                    menuOpciones = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Borrar completadas") },
                                onClick = {
                                    viewModel.borrarCompletadas()
                                    menuOpciones = false
                                    Toast.makeText(
                                        context,
                                        "Tareas completadas eliminadas",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                tareaEnEdicion = null
                mostrarDialogo = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { innerPadding ->
        if (tareas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay tareas. Toca + para agregar una.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tareas, key = { it.id }) { tarea ->
                    TareaItem(
                        tarea = tarea,
                        onToggle = { viewModel.alternarCompletada(tarea) },
                        onEditar = {
                            tareaEnEdicion = tarea
                            mostrarDialogo = true
                        },
                        onEliminar = { viewModel.eliminar(tarea) }
                    )
                }
            }
        }
    }

    if (mostrarDialogo) {
        TareaDialog(
            tarea = tareaEnEdicion,
            onDismiss = { mostrarDialogo = false },
            onGuardar = { titulo ->
                val actual = tareaEnEdicion
                if (actual == null) viewModel.agregar(titulo) else viewModel.editar(actual, titulo)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
fun TareaItem(
    tarea: Tarea,
    onToggle: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val formato = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = tarea.completada, onCheckedChange = { onToggle() })

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else null
                )
                Text(
                    text = formato.format(Date(tarea.fechaCreacion)),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Composable
fun TareaDialog(
    tarea: Tarea?,
    onDismiss: () -> Unit,
    onGuardar: (String) -> Unit
) {
    var titulo by remember { mutableStateOf(tarea?.titulo ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (tarea == null) "Nueva tarea" else "Editar tarea") },
        text = {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (titulo.isNotBlank()) onGuardar(titulo.trim()) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}