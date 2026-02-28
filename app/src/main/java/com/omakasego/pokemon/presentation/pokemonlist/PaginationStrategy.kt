package com.omakasego.pokemon.presentation.pokemonlist

/**
 * Strategy Pattern:
 * This interface defines "how pagination should behave".
 *
 * Why this is a strategy:
 * - The ViewModel and Screen depend on this abstraction.
 * - Different pagination behaviors can be swapped without changing those classes.
 */
interface PaginationStrategy {
    fun pageSize(): Int

    fun nextOffset(currentItemCount: Int): Int

    fun shouldLoadNextPage(
        lastVisibleItemIndex: Int,
        totalItemsCount: Int,
        isLoading: Boolean,
        hasReachedEnd: Boolean
    ): Boolean
}

/**
 * Default strategy used by the app.
 * - Loads 30 items per page
 * - Starts loading when user is close to the end of the list
 */
class DefaultPaginationStrategy(
    private val pageSize: Int = 30,
    private val prefetchThreshold: Int = 5
) : PaginationStrategy {

    override fun pageSize(): Int = pageSize

    override fun nextOffset(currentItemCount: Int): Int = currentItemCount

    override fun shouldLoadNextPage(
        lastVisibleItemIndex: Int,
        totalItemsCount: Int,
        isLoading: Boolean,
        hasReachedEnd: Boolean
    ): Boolean {
        if (isLoading || hasReachedEnd || totalItemsCount == 0) return false
        return lastVisibleItemIndex >= totalItemsCount - prefetchThreshold
    }
}

/**
 * Alternative strategy:
 * - Requests larger pages
 * - Prefetches earlier
 *
 * This can make scrolling feel smoother on fast networks, but may use more data.
 */
class AggressivePaginationStrategy(
    private val pageSize: Int = 50,
    private val prefetchThreshold: Int = 12
) : PaginationStrategy {

    override fun pageSize(): Int = pageSize

    override fun nextOffset(currentItemCount: Int): Int = currentItemCount

    override fun shouldLoadNextPage(
        lastVisibleItemIndex: Int,
        totalItemsCount: Int,
        isLoading: Boolean,
        hasReachedEnd: Boolean
    ): Boolean {
        if (isLoading || hasReachedEnd || totalItemsCount == 0) return false
        return lastVisibleItemIndex >= totalItemsCount - prefetchThreshold
    }
}

/**
 * Conservative strategy:
 * - Requests smaller pages
 * - Waits longer before requesting the next page
 *
 * This can reduce network/data usage, but loading can feel less immediate.
 */
class ConservativePaginationStrategy(
    private val pageSize: Int = 15,
    private val prefetchThreshold: Int = 2
) : PaginationStrategy {

    override fun pageSize(): Int = pageSize

    override fun nextOffset(currentItemCount: Int): Int = currentItemCount

    override fun shouldLoadNextPage(
        lastVisibleItemIndex: Int,
        totalItemsCount: Int,
        isLoading: Boolean,
        hasReachedEnd: Boolean
    ): Boolean {
        if (isLoading || hasReachedEnd || totalItemsCount == 0) return false
        return lastVisibleItemIndex >= totalItemsCount - prefetchThreshold
    }
}
