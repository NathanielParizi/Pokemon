package com.omakasego.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omakasego.pokemon.data.remote.NetworkModule
import com.omakasego.pokemon.data.repository.PokemonRepositoryImpl
import com.omakasego.pokemon.domain.usecase.GetPokemonPagingDataUseCase
import com.omakasego.pokemon.presentation.pokemonlist.PokemonListScreen
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
    val pagingUseCase = GetPokemonPagingDataUseCase(repository)

    // Manual Strategy Pattern paging is intentionally kept for reference,
    // but the app now uses Jetpack Paging 3 instead.
    // val manualUseCase = GetPokemonPageUseCase(repository)
    // val paginationStrategy: PaginationStrategy = DefaultPaginationStrategy()
    // val paginationStrategy: PaginationStrategy = AggressivePaginationStrategy()
    // val paginationStrategy: PaginationStrategy = ConservativePaginationStrategy()
    val viewModel: PokemonListViewModel = viewModel(
        factory = PokemonListViewModel.provideFactory(
            getPokemonPagingDataUseCase = pagingUseCase
        )
    )
    PokemonListScreen(viewModel = viewModel)
}