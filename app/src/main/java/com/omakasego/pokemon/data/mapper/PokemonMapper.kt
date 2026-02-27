package com.omakasego.pokemon.data.mapper

import com.omakasego.pokemon.data.remote.dto.PokemonListItemDto
import com.omakasego.pokemon.domain.model.Pokemon

private const val POKEMON_ARTWORK_BASE_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

fun PokemonListItemDto.toDomain(): Pokemon {
    val pokemonId = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: -1
    val imageUrl = if (pokemonId > 0) {
        "$POKEMON_ARTWORK_BASE_URL$pokemonId.png"
    } else {
        ""
    }

    return Pokemon(
        id = pokemonId,
        name = name.replaceFirstChar { it.uppercase() },
        imageUrl = imageUrl
    )
}
