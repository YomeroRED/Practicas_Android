package com.example.miniproyecto01.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("RegistroEstudiantes", Context.MODE_PRIVATE)

    companion object {
        const val KEY_MATRICULA = "key_matricula"
        const val KEY_NOMBRE = "key_nombre"
        const val KEY_CARRERA = "key_carrera"
        const val KEY_TURNO = "key_turno"
        const val KEY_ACTIVO = "key_activo"
    }

    // Guarda el último registro
    fun guardarEstudiante(estudiante: Estudiante) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_MATRICULA, estudiante.matricula)
        editor.putString(KEY_NOMBRE, estudiante.nombre)
        editor.putString(KEY_CARRERA, estudiante.carrera)
        editor.putString(KEY_TURNO, estudiante.turno)
        editor.putBoolean(KEY_ACTIVO, estudiante.activo)
        editor.apply()
    }

    // Recupera el último registro (o valores por defecto si no hay nada)
    fun obtenerEstudiante(): Estudiante = Estudiante(
        matricula = sharedPreferences.getString(KEY_MATRICULA, "") ?: "",
        nombre = sharedPreferences.getString(KEY_NOMBRE, "") ?: "",
        carrera = sharedPreferences.getString(KEY_CARRERA, "") ?: "",
        turno = sharedPreferences.getString(KEY_TURNO, "Matutino") ?: "Matutino",
        activo = sharedPreferences.getBoolean(KEY_ACTIVO, true)
    )

    fun limpiar() {
        sharedPreferences.edit().clear().apply()
    }
}