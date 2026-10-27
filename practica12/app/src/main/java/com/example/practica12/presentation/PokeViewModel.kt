package com.example.practica12.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica12.data.NamedResource
import com.example.practica12.data.PokeRepository
import com.example.practica12.data.PokemonDetailResponse
import kotlinx.coroutines.launch

class PokeViewModel : ViewModel() {
    private val repository = PokeRepository()

    val tipos = mutableStateOf<List<NamedResource>>(emptyList())
    val pokemonsPorTipo = mutableStateOf<List<NamedResource>>(emptyList())
    val pokemonDetalle = mutableStateOf<PokemonDetailResponse?>(null)
    val error = mutableStateOf<String?>(null)

    init {
        // Cargar las categorías apenas se abra la app
        viewModelScope.launch {
            try {
                tipos.value = repository.getTypes()
            } catch (e: Exception) {
                error.value = "No se pudo cargar. Revisa tu conexión a internet."
            }
        }
    }

    fun cargarPokemonsPorTipo(id: String) {
        error.value = null
        pokemonsPorTipo.value = emptyList()
        viewModelScope.launch {
            try {
                pokemonsPorTipo.value = repository.getPokemonsByType(id)
            } catch (e: Exception) {
                error.value = "No se pudo cargar. Revisa tu conexión a internet."
            }
        }
    }

    fun cargarDetallePokemon(id: String) {
        error.value = null
        pokemonDetalle.value = null // Limpiamos la vista anterior
        viewModelScope.launch {
            try {
                pokemonDetalle.value = repository.getPokemonDetail(id)
            } catch (e: Exception) {
                error.value = "No se pudo cargar. Revisa tu conexión a internet."
            }
        }
    }
}