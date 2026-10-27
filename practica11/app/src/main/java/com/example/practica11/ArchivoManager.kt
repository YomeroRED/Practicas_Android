package com.example.practica11

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

enum class Destino(val etiqueta: String) {
    INTERNO("Almacenamiento interno"),
    EXTERNO("SDCard / externo")
}

class ArchivoManager(private val context: Context) {

    companion object {
        const val NOMBRE_ARCHIVO = "notas.txt"
    }

    // Carpeta según el destino elegido
    private fun directorio(destino: Destino): File? = when (destino) {
        Destino.INTERNO -> context.filesDir
        Destino.EXTERNO -> {
            // Si hay una SD física, aparece como segundo elemento; si no, se usa el externo principal
            val dirs = context.getExternalFilesDirs(null).filterNotNull()
            val dir = dirs.getOrNull(1) ?: dirs.firstOrNull()
            if (dir != null && Environment.getExternalStorageState(dir) == Environment.MEDIA_MOUNTED) dir else null
        }
    }

    fun archivo(destino: Destino): File? =
        directorio(destino)?.let { File(it, NOMBRE_ARCHIVO) }

    // Escribe el texto. agregar = true añade al final (modificar); false sobrescribe
    fun guardar(destino: Destino, texto: String, agregar: Boolean): String {
        val file = archivo(destino) ?: throw IOException("El almacenamiento no está disponible")
        val contenido = if (agregar) texto + "\n" else texto
        FileOutputStream(file, agregar).use { it.write(contenido.toByteArray()) }
        return file.absolutePath
    }

    fun leer(destino: Destino): String {
        val file = archivo(destino)
        if (file == null || !file.exists()) throw FileNotFoundException("El archivo no existe todavía")
        return FileInputStream(file).bufferedReader().use { it.readText() }
    }

    fun borrar(destino: Destino): Boolean = archivo(destino)?.delete() ?: false
}