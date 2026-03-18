package com.omakasego.pokemon.domain.usecase

import androidx.paging.PagingData
import com.omakasego.pokemon.domain.model.Pokemon
import com.omakasego.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use-case for Paging 3 list loading.
 */
class GetPokemonPagingDataUseCase(
    private val repository: PokemonRepository
) {
    operator fun invoke(
        pageSize: Int,
        prefetchDistance: Int,
        maxSize: Int
    ): Flow<PagingData<Pokemon>> {
        return repository.getPokemonPagingData(
            pageSize = pageSize,
            prefetchDistance = prefetchDistance,
            maxSize = maxSize
        )
    }
}
