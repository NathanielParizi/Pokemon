package com.omakasego.pokemon.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omakasego.pokemon.data.mapper.toDomain
import com.omakasego.pokemon.data.remote.PokemonApiService
import com.omakasego.pokemon.domain.model.Pokemon
import kotlinx.coroutines.delay

class PokemonPagingSource(
    private val apiService: PokemonApiService,
    private val networkPageSize: Int
) : PagingSource<Int, Pokemon>() {

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition)
        return page?.prevKey?.plus(state.config.pageSize)
            ?: page?.nextKey?.minus(state.config.pageSize)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val startTime = System.currentTimeMillis()
        val result = try {
            val offset = params.key ?: 0
            // Keep each network request intentionally small so paging behavior is visible.
            val requestLimit = minOf(params.loadSize, networkPageSize)
            val response = apiService.getPokemonPage(offset = offset, limit = requestLimit)
            val items = response.results.map { it.toDomain() }
            val nextKey = if (items.isEmpty() || offset + items.size >= response.count) {
                null
            } else {
                offset + items.size
            }

            LoadResult.Page(
                data = items,
                prevKey = if (offset == 0) null else maxOf(offset - requestLimit, 0),
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }

        // Exaggerate loading feedback so spinner states are easy to observe.
        val elapsed = System.currentTimeMillis() - startTime
        if (elapsed < MIN_LOADING_MS) {
            delay(MIN_LOADING_MS - elapsed)
        }

        return result
    }

    companion object {
        private const val MIN_LOADING_MS = 1_000L
    }
}
