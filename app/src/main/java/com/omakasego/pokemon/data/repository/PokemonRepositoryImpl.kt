package com.omakasego.pokemon.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omakasego.pokemon.data.mapper.toDomain
import com.omakasego.pokemon.data.paging.PokemonPagingSource
import com.omakasego.pokemon.data.remote.PokemonApiService
import com.omakasego.pokemon.domain.model.Pokemon
import com.omakasego.pokemon.domain.model.PokemonPage
import com.omakasego.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

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

    override fun getPokemonPagingData(
        pageSize: Int,
        prefetchDistance: Int,
        maxSize: Int
    ): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                initialLoadSize = pageSize,
                prefetchDistance = prefetchDistance,
                maxSize = maxSize
            ),
            pagingSourceFactory = {
                PokemonPagingSource(
                    apiService = apiService,
                    networkPageSize = pageSize
                )
            }
        ).flow
    }
}
