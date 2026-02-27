package com.omakasego.pokemon.domain.repository

import com.omakasego.pokemon.domain.model.PokemonPage

interface PokemonRepository {
    suspend fun getPokemonPage(offset: Int, limit: Int): PokemonPage
}
