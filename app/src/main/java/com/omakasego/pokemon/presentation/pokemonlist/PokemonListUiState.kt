package com.omakasego.pokemon.presentation.pokemonlist

import com.omakasego.pokemon.domain.model.Pokemon

data class PokemonListUiState(
    val items: List<Pokemon> = emptyList(),
    val isLoadingInitial: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val hasReachedEnd: Boolean = false
)
