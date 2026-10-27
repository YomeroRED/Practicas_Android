package com.example.practica12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practica12.presentation.*
import com.example.practica12.ui.theme.Practica12Theme

class MainActivity : ComponentActivity() {
    // El ViewModel sobrevive a los cambios de pantalla
    private val viewModel: PokeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica12Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "tipos") {
                        composable("tipos") {
                            TiposScreen(viewModel) { id ->
                                navController.navigate("pokemons/$id")
                            }
                        }
                        composable("pokemons/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            PokemonsByTypeScreen(viewModel, id) { pokeId ->
                                navController.navigate("detalle/$pokeId")
                            }
                        }
                        composable("detalle/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            PokemonDetailScreen(viewModel, id)
                        }
                    }
                }
            }
        }
    }
}