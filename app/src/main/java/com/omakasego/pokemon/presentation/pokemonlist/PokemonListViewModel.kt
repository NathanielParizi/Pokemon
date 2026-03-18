package com.omakasego.pokemon.presentation.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omakasego.pokemon.domain.model.Pokemon
import com.omakasego.pokemon.domain.usecase.GetPokemonPagingDataUseCase
import kotlinx.coroutines.flow.Flow

class PokemonListViewModel(
    getPokemonPagingDataUseCase: GetPokemonPagingDataUseCase
) : ViewModel() {

    // Strategy-based manual pagination was replaced by Paging 3.
    // If needed later, the strategy-driven state/loading methods can be restored.
    val pokemonPagingData: Flow<PagingData<Pokemon>> =
        getPokemonPagingDataUseCase(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            maxSize = MAX_SIZE
        ).cachedIn(viewModelScope)

    companion object {
        // Conservative loading strategy for clearer paging behavior.
        // Small page size + low prefetch + bounded max size limits total loaded data.
        private const val PAGE_SIZE = 10
        private const val PREFETCH_DISTANCE = 2
        private const val MAX_SIZE = 30

        fun provideFactory(
            getPokemonPagingDataUseCase: GetPokemonPagingDataUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) {
                    return PokemonListViewModel(getPokemonPagingDataUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
