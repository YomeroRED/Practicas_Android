package com.example.practica04.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(
    seleccionada: String,
    opciones: List<String>,
    onSeleccion: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        opciones.forEach { opcion ->
            RadioButton(
                selected = seleccionada == opcion,
                onClick = { onSeleccion(opcion) }
            )
            Text(
                text = opcion,
                modifier = Modifier.clickable { onSeleccion(opcion) }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}