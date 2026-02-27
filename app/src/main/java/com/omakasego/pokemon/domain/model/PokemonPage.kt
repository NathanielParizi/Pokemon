package com.omakasego.pokemon.domain.model

data class PokemonPage(
    val totalCount: Int,
    val items: List<Pokemon>
)
