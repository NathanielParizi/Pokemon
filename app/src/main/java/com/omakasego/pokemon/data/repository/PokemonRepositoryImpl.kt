package com.omakasego.pokemon.data.repository

import com.omakasego.pokemon.data.mapper.toDomain
import com.omakasego.pokemon.data.remote.PokemonApiService
import com.omakasego.pokemon.domain.model.PokemonPage
import com.omakasego.pokemon.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val apiService: PokemonApiService
) : PokemonRepository {
    override suspend fun getPokemonPage(offset: Int, limit: Int): PokemonPage {
        val response = apiService.getPokemonPage(offset = offset, limit = limit)
        return PokemonPage(
            totalCount = response.count,
            items = response.results.map { it.toDomain() }
        )
    }
}
