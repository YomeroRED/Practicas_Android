package com.example.practica02

import java.util.Locale
import kotlin.math.abs

object LogicaCalc {

    fun calcular(num1: String, num2: String, operation: String): String {
        val n1 = num1.toDoubleOrNull() ?: return ""
        val n2 = num2.toDoubleOrNull() ?: return ""

        val result = when (operation) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "x" -> n1 * n2
            "÷" -> if (n2 != 0.0) n1 / n2 else return "Error" // división entre cero
            else -> return ""
        }
        return formatear(result)
    }

    fun calcularPorcentaje(num: String): String {
        val n = num.toDoubleOrNull() ?: return ""
        return formatear(n / 100)
    }

    // Quita ceros sobrantes y evita errores de redondeo (0.1 + 0.2 = 0.3)
    private fun formatear(valor: Double): String {
        if (valor.isNaN() || valor.isInfinite() || abs(valor) >= 1e15) return "Error"
        if (valor % 1.0 == 0.0) return valor.toLong().toString()
        return String.format(Locale.US, "%.8f", valor).trimEnd('0').trimEnd('.')
    }
}