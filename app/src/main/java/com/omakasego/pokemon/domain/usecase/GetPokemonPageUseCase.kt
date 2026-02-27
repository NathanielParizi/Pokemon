package com.omakasego.pokemon.domain.usecase

import com.omakasego.pokemon.domain.model.PokemonPage
import com.omakasego.pokemon.domain.repository.PokemonRepository

class GetPokemonPageUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(offset: Int, limit: Int): PokemonPage {
        return repository.getPokemonPage(offset, limit)
    }
}
