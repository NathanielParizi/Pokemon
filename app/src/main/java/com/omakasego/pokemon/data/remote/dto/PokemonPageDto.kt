package com.omakasego.pokemon.data.remote.dto

data class PokemonPageDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemDto>
)
