package com.example.practica04

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica04.components.*

@Composable
fun FormScreen() {
    val context = LocalContext.current

    // Estado de cada control
    var switchActivo by remember { mutableStateOf(false) }
    var opcionRadio by remember { mutableStateOf("Opción 1") }
    var aceptaTerminos by remember { mutableStateOf(false) }
    var elementoSpinner by remember { mutableStateOf("Seleccionar Opción") }
    var volumen by remember { mutableFloatStateOf(50f) }
    var fecha by remember { mutableStateOf("Seleccionar Fecha") }

    // Último evento capturado en tiempo real y resumen final
    var ultimoEvento by remember { mutableStateOf("Ninguno") }
    var resumen by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Práctica 4: Componentes Avanzados",
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider()

        // 1. Switch
        Text("1. Switch", style = MaterialTheme.typography.titleMedium)
        CustomSwitch(activado = switchActivo) {
            switchActivo = it
            ultimoEvento = "Switch → " + if (it) "activado" else "desactivado"
        }

        HorizontalDivider()

        // 2. RadioButton
        Text("2. RadioButton", style = MaterialTheme.typography.titleMedium)
        CustomRadioButton(
            seleccionada = opcionRadio,
            opciones = listOf("Opción 1", "Opción 2")
        ) {
            opcionRadio = it
            ultimoEvento = "RadioButton → $it"
        }

        HorizontalDivider()

        // 3. Checkbox
        Text("3. Checkbox", style = MaterialTheme.typography.titleMedium)
        CustomCheckbox(marcado = aceptaTerminos) {
            aceptaTerminos = it
            ultimoEvento = "Checkbox → " + if (it) "marcado" else "desmarcado"
        }

        HorizontalDivider()

        // 4. Spinner
        Text("4. Spinner (DropdownMenu)", style = MaterialTheme.typography.titleMedium)
        CustomSpinner(
            seleccionado = elementoSpinner,
            opciones = listOf("Elemento A", "Elemento B", "Elemento C")
        ) {
            elementoSpinner = it
            ultimoEvento = "Spinner → $it"
        }

        HorizontalDivider()

        // 5. Slider (SeekBar)
        Text("5. Slider (SeekBar)", style = MaterialTheme.typography.titleMedium)
        CustomSlider(valor = volumen) {
            volumen = it
            ultimoEvento = "Slider → ${it.toInt()}"
        }

        HorizontalDivider()

        // 6. DatePicker
        Text("6. DatePicker", style = MaterialTheme.typography.titleMedium)
        CustomDatePicker(fecha = fecha) {
            fecha = it
            ultimoEvento = "DatePicker → $it"
        }

        HorizontalDivider()

        // Evento en tiempo real
        Text("Último evento: $ultimoEvento", style = MaterialTheme.typography.bodyMedium)

        // Botón de procesamiento
        Button(
            onClick = {
                resumen = listOf(
                    "Switch: " + if (switchActivo) "Activado" else "Desactivado",
                    "RadioButton: $opcionRadio",
                    "Checkbox: " + if (aceptaTerminos) "Acepta los términos" else "No acepta los términos",
                    "Spinner: $elementoSpinner",
                    "Slider: ${volumen.toInt()}",
                    "Fecha: $fecha"
                ).joinToString("\n")
                Toast.makeText(context, "Formulario completo", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Probar Formulario")
        }

        // Resumen de los datos capturados
        if (resumen.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen del formulario", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(resumen)
                }
            }
        }
    }
}