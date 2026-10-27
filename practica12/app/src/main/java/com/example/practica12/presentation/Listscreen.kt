package com.example.practica12.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TiposScreen(viewModel: PokeViewModel, onTypeClick: (String) -> Unit) {
    val tipos by viewModel.tipos
    val error by viewModel.error

    if (tipos.isEmpty() && error != null) {
        MensajeError(error!!)
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            items(tipos) { tipo ->
                ListItemPoke(
                    texto = tipo.name.uppercase(),
                    onClick = { onTypeClick(tipo.id) }
                )
            }
        }
    }
}

@Composable
fun PokemonsByTypeScreen(
    viewModel: PokeViewModel,
    typeId: String,
    onPokeClick: (String) -> Unit
) {
    LaunchedEffect(typeId) { viewModel.cargarPokemonsPorTipo(typeId) }

    val pokemons by viewModel.pokemonsPorTipo
    val error by viewModel.error

    if (pokemons.isEmpty() && error != null) {
        MensajeError(error!!)
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            items(pokemons) { poke ->
                ListItemPoke(
                    texto = "#${poke.id} - ${poke.name.uppercase()}",
                    onClick = { onPokeClick(poke.id) }
                )
            }
        }
    }
}

@Composable
fun MensajeError(mensaje: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(mensaje, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ListItemPoke(texto: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pokébola simulada
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.Red, CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.White, CircleShape)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = texto, style = MaterialTheme.typography.titleMedium)
        }
    }
}