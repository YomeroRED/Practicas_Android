package com.example.miniproyecto02.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniproyecto02.data.Tarea
import com.example.miniproyecto02.domain.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Filtro(val etiqueta: String) {
    TODAS("Todas"),
    PENDIENTES("Pendientes"),
    COMPLETADAS("Completadas")
}

enum class Orden { FECHA, ESTADO }

class TareaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).tareaDao()

    private val _filtro = MutableStateFlow(Filtro.TODAS)
    val filtro: StateFlow<Filtro> = _filtro.asStateFlow()

    private val _orden = MutableStateFlow(Orden.FECHA)
    val orden: StateFlow<Orden> = _orden.asStateFlow()

    val tareas: StateFlow<List<Tarea>> =
        combine(dao.obtenerTodas(), _filtro, _orden) { lista, filtro, orden ->
            val filtradas = when (filtro) {
                Filtro.TODAS -> lista
                Filtro.PENDIENTES -> lista.filter { !it.completada }
                Filtro.COMPLETADAS -> lista.filter { it.completada }
            }
            when (orden) {
                Orden.FECHA -> filtradas.sortedByDescending { it.fechaCreacion }
                Orden.ESTADO -> filtradas.sortedWith(
                    compareBy<Tarea> { it.completada }.thenByDescending { it.fechaCreacion }
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarFiltro(nuevo: Filtro) { _filtro.value = nuevo }
    fun cambiarOrden(nuevo: Orden) { _orden.value = nuevo }

    fun agregar(titulo: String) {
        viewModelScope.launch { dao.insertar(Tarea(titulo = titulo)) }
    }

    fun editar(tarea: Tarea, nuevoTitulo: String) {
        viewModelScope.launch { dao.actualizar(tarea.copy(titulo = nuevoTitulo)) }
    }

    fun alternarCompletada(tarea: Tarea) {
        viewModelScope.launch { dao.actualizar(tarea.copy(completada = !tarea.completada)) }
    }

    fun eliminar(tarea: Tarea) {
        viewModelScope.launch { dao.eliminar(tarea) }
    }

    fun borrarCompletadas() {
        viewModelScope.launch { dao.eliminarCompletadas() }
    }
}