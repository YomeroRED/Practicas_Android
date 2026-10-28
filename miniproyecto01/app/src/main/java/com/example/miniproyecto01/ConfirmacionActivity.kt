package com.example.miniproyecto01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.miniproyecto01.data.Estudiante
import com.example.miniproyecto01.ui.theme.Miniproyecto01Theme

class ConfirmacionActivity : ComponentActivity() {

    companion object {
        const val EXTRA_MATRICULA = "EXTRA_MATRICULA"
        const val EXTRA_NOMBRE = "EXTRA_NOMBRE"
        const val EXTRA_CARRERA = "EXTRA_CARRERA"
        const val EXTRA_TURNO = "EXTRA_TURNO"
        const val EXTRA_ACTIVO = "EXTRA_ACTIVO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Traemos los valores del Intent
        val estudiante = Estudiante(
            matricula = intent.getStringExtra(EXTRA_MATRICULA) ?: "Sin matrícula",
            nombre = intent.getStringExtra(EXTRA_NOMBRE) ?: "Sin nombre",
            carrera = intent.getStringExtra(EXTRA_CARRERA) ?: "Sin carrera",
            turno = intent.getStringExtra(EXTRA_TURNO) ?: "Sin turno",
            activo = intent.getBooleanExtra(EXTRA_ACTIVO, true)
        )

        setContent {
            Miniproyecto01Theme {
                ConfirmacionScreen(
                    estudiante = estudiante,
                    onVolver = { finish() }
                )
            }
        }
    }
}