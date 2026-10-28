package com.example.miniproyecto01.data

data class Estudiante(
    val matricula: String = "",
    val nombre: String = "",
    val carrera: String = "",
    val turno: String = "Matutino",
    val activo: Boolean = true
)