package com.omakasego.pokemon.presentation.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.omakasego.pokemon.domain.usecase.GetPokemonPageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val getPokemonPageUseCase: GetPokemonPageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    private val pageSize = 30
    private var totalCount: Int = Int.MAX_VALUE

    init {
        loadNextPage()
    }

    fun retry() {
        if (uiState.value.items.isEmpty()) {
            _uiState.update { it.copy(isLoadingInitial = true, errorMessage = null) }
        }
        loadNextPage()
    }

    fun loadNextPage() {
        val currentState = uiState.value
        if (
            currentState.isLoadingInitial ||
            currentState.isLoadingMore ||
            currentState.hasReachedEnd
        ) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingInitial = it.items.isEmpty(),
                    isLoadingMore = it.items.isNotEmpty(),
                    errorMessage = null
                )
            }

            runCatching {
                getPokemonPageUseCase(offset = uiState.value.items.size, limit = pageSize)
            }.onSuccess { page ->
                totalCount = page.totalCount
                _uiState.update { oldState ->
                    val newItems = oldState.items + page.items
                    oldState.copy(
                        items = newItems,
                        isLoadingInitial = false,
                        isLoadingMore = false,
                        hasReachedEnd = newItems.size >= totalCount
                    )
                }
            }.onFailure {
                _uiState.update { oldState ->
                    oldState.copy(
                        isLoadingInitial = false,
                        isLoadingMore = false,
                        errorMessage = "Could not load Pokemon. Please try again."
                    )
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            getPokemonPageUseCase: GetPokemonPageUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) {
                    return PokemonListViewModel(getPokemonPageUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
