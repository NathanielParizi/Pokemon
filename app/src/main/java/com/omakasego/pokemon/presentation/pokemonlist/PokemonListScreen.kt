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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel,
    paginationStrategy: PaginationStrategy
) {
    val uiState by viewModel.uiState.collectAsState()
    val errorMessage = uiState.errorMessage
    val listState = rememberLazyListState()

    LaunchedEffect(listState, uiState.items.size, uiState.isLoadingMore, uiState.hasReachedEnd) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            paginationStrategy.shouldLoadNextPage(
                lastVisibleItemIndex = lastVisibleItem,
                totalItemsCount = totalItemsCount,
                isLoading = uiState.isLoadingMore || uiState.isLoadingInitial,
                hasReachedEnd = uiState.hasReachedEnd
            )
        }.map { shouldLoad -> shouldLoad && !uiState.hasReachedEnd }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.loadNextPage()
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when {
            uiState.isLoadingInitial -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null && uiState.items.isEmpty() -> {
                ErrorContent(
                    message = errorMessage,
                    onRetryClick = viewModel::retry,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    state = listState,
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = uiState.items,
                        key = { pokemon -> pokemon.id }
                    ) { pokemon ->
                        PokemonRow(
                            id = pokemon.id,
                            name = pokemon.name
                        )
                        HorizontalDivider()
                    }

                    if (uiState.isLoadingMore) {
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

                    if (errorMessage != null && uiState.items.isNotEmpty()) {
                        item {
                            ErrorContent(
                                message = errorMessage,
                                onRetryClick = viewModel::retry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
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
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
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
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = onRetryClick) {
            Text(text = "Retry")
        }
    }
}
