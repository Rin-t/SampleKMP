package com.example.samplekmp.android.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.samplekmp.PokemonListItem

@Composable
fun PokemonGridItem(
    pokemon: PokemonListItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.spriteUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text = "#${pokemon.id}",
                textAlign = TextAlign.Center
            )
            Text(
                text = pokemon.name,
                textAlign = TextAlign.Center
            )
        }
    }
}
