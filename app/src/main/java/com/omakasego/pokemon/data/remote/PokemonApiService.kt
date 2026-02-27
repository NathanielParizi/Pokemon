package com.omakasego.pokemon.data.remote

import com.omakasego.pokemon.data.remote.dto.PokemonPageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonPage(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonPageDto
}
