package com.example.practica04.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun CustomSlider(valor: Float, onCambio: (Float) -> Unit) {
    Column {
        Text("Volumen: ${valor.toInt()}", fontSize = 16.sp)
        Slider(
            value = valor,
            onValueChange = onCambio,
            valueRange = 0f..100f
        )
    }
}