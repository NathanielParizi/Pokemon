package com.omakasego.pokemon.presentation.pokemonlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PagingDebugOverlay(
    itemCount: Int,
    cachedCount: Int,
    firstId: Int?,
    lastId: Int?,
    isPrependLoading: Boolean,
    isAppendLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Paging debug (memory window)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Total loaded in list: $itemCount",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Currently cached snapshot: $cachedCount items",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Cached id range: #${firstId ?: "-"} to #${lastId ?: "-"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Load states -> prepend: ${if (isPrependLoading) "Loading" else "Idle"}, append: ${if (isAppendLoading) "Loading" else "Idle"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
