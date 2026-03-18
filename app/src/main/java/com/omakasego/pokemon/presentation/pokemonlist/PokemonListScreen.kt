package com.omakasego.pokemon.presentation.pokemonlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel
) {
    val pagingItems = viewModel.pokemonPagingData.collectAsLazyPagingItems()
    val cachedItems = pagingItems.itemSnapshotList.items
    val cachedFirstId = cachedItems.firstOrNull()?.id
    val cachedLastId = cachedItems.lastOrNull()?.id

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when (val refreshState = pagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                ErrorContent(
                    message = refreshState.error.localizedMessage
                        ?: "Could not load Pokemon. Please try again.",
                    onRetryClick = { pagingItems.retry() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 140.dp,
                            bottom = 8.dp
                        )
                    ) {
                        if (pagingItems.loadState.prepend is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Loading earlier items (prepend)...",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                        }

                        if (pagingItems.loadState.prepend is LoadState.Error) {
                            item {
                                val prependError = (pagingItems.loadState.prepend as LoadState.Error).error
                                ErrorContent(
                                    message = prependError.localizedMessage
                                        ?: "Could not load previous Pokemon.",
                                    onRetryClick = { pagingItems.retry() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }

                        items(
                            count = pagingItems.itemCount,
                            key = pagingItems.itemKey { it.id }
                        ) { index ->
                            val pokemon = pagingItems[index]
                            if (pokemon != null) {
                                PokemonRow(
                                    id = pokemon.id,
                                    name = pokemon.name
                                )
                                HorizontalDivider()
                            }
                        }

                        when (val appendState = pagingItems.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            is LoadState.Error -> {
                                item {
                                    ErrorContent(
                                        message = appendState.error.localizedMessage
                                            ?: "Could not load more Pokemon.",
                                        onRetryClick = { pagingItems.retry() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                            }

                            else -> Unit
                        }

                        if (pagingItems.loadState.append is LoadState.NotLoading &&
                            pagingItems.itemCount > 0 &&
                            pagingItems.loadState.append.endOfPaginationReached
                        ) {
                            item {
                                Text(
                                    text = "You reached the end of the Pokemon list.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }

                    PagingDebugOverlay(
                        itemCount = pagingItems.itemCount,
                        cachedCount = cachedItems.size,
                        firstId = cachedFirstId,
                        lastId = cachedLastId,
                        isPrependLoading = pagingItems.loadState.prepend is LoadState.Loading,
                        isAppendLoading = pagingItems.loadState.append is LoadState.Loading,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonRow(
    id: Int,
    name: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "#$id",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium
        )
        Button(onClick = onRetryClick) {
            Text(
                text = "Retry",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
            )
        }
    }
}
