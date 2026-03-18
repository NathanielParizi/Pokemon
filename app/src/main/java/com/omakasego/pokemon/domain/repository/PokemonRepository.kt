package com.omakasego.pokemon.domain.repository

import androidx.paging.PagingData
import com.omakasego.pokemon.domain.model.PokemonPage
import com.omakasego.pokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemonPage(offset: Int, limit: Int): PokemonPage
    fun getPokemonPagingData(
        pageSize: Int,
        prefetchDistance: Int,
        maxSize: Int
    ): Flow<PagingData<Pokemon>>
}
