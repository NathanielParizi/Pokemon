package com.omakasego.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omakasego.pokemon.data.remote.NetworkModule
import com.omakasego.pokemon.data.repository.PokemonRepositoryImpl
import com.omakasego.pokemon.domain.usecase.GetPokemonPageUseCase
import com.omakasego.pokemon.presentation.pokemonlist.ConservativePaginationStrategy
import com.omakasego.pokemon.presentation.pokemonlist.DefaultPaginationStrategy
import com.omakasego.pokemon.presentation.pokemonlist.PokemonListScreen
import com.omakasego.pokemon.presentation.pokemonlist.PaginationStrategy
import com.omakasego.pokemon.presentation.pokemonlist.PokemonListViewModel
import com.omakasego.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                PokemonApp()
            }
        }
    }
}

@Composable
fun PokemonApp(
) {
    val repository = PokemonRepositoryImpl(NetworkModule.pokemonApiService)
    val useCase = GetPokemonPageUseCase(repository)
    // Strategy Pattern in action:
    // Swap this one line to change pagination behavior app-wide.
    val paginationStrategy: PaginationStrategy = DefaultPaginationStrategy()
    // val paginationStrategy: PaginationStrategy = AggressivePaginationStrategy()
    // val paginationStrategy: PaginationStrategy = ConservativePaginationStrategy()
    val viewModel: PokemonListViewModel = viewModel(
        factory = PokemonListViewModel.provideFactory(
            getPokemonPageUseCase = useCase,
            paginationStrategy = paginationStrategy
        )
    )
    PokemonListScreen(
        viewModel = viewModel,
        paginationStrategy = paginationStrategy
    )
}