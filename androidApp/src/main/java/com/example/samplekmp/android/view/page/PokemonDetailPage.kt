package com.example.samplekmp.android.view.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.samplekmp.PokemonDetail
import com.example.samplekmp.PokemonDetailUiState
import com.example.samplekmp.PokemonDetailUseCase
import com.example.samplekmp.android.navigation.LocalNavigator
import com.example.samplekmp.android.view.components.ErrorMessage
import com.example.samplekmp.android.view.components.LoadingIndicator
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailPage(
    pokemonId: Int,
    modifier: Modifier = Modifier
) {
    var uiState by remember { mutableStateOf<PokemonDetailUiState>(PokemonDetailUiState.Loading) }
    val navigator = LocalNavigator.current
    val useCase: PokemonDetailUseCase = koinInject { parametersOf(navigator, pokemonId) }
    val scope = rememberCoroutineScope()

    val loadPokemonDetail: () -> Unit = {
        scope.launch {
            uiState = PokemonDetailUiState.Loading
            try {
                val detail = useCase.fetchPokemonDetail()
                if (detail != null) {
                    uiState = PokemonDetailUiState.Success(detail)
                } else {
                    uiState = PokemonDetailUiState.Error("Pokemon not found")
                }
            } catch (e: Exception) {
                uiState = PokemonDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    LaunchedEffect(pokemonId) {
        loadPokemonDetail()
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("詳細") },
            navigationIcon = {
                IconButton(onClick = { useCase.navigateBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "戻る"
                    )
                }
            }
        )

        when (val state = uiState) {
            is PokemonDetailUiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is PokemonDetailUiState.Success -> {
                PokemonDetailContent(
                    pokemonDetail = state.pokemonDetail,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is PokemonDetailUiState.Error -> {
                ErrorMessage(
                    message = state.message,
                    onRetry = loadPokemonDetail,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun PokemonDetailContent(
    pokemonDetail: PokemonDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with image
        AsyncImage(
            model = pokemonDetail.spriteUrl,
            contentDescription = pokemonDetail.name,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "#${pokemonDetail.id} ${pokemonDetail.name}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Types
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            pokemonDetail.types.forEach { type ->
                Card {
                    Text(
                        text = type,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Height & Weight
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Height", style = MaterialTheme.typography.labelMedium)
                Text(text = "${(pokemonDetail.height ?: 0) / 10.0} m")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Weight", style = MaterialTheme.typography.labelMedium)
                Text(text = "${(pokemonDetail.weight ?: 0) / 10.0} kg")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats
        Text(
            text = "Base Stats",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        pokemonDetail.stats.forEach { stat ->
            StatRow(statName = stat.name, statValue = stat.baseStat)
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Abilities
        Text(
            text = "Abilities",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        pokemonDetail.abilities.forEach { ability ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = ability.name)
                if (ability.isHidden) {
                    Text(
                        text = "(Hidden)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun StatRow(
    statName: String,
    statValue: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            modifier = Modifier.width(100.dp),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = statValue.toString(),
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        LinearProgressIndicator(
            progress = (statValue / 255f).coerceIn(0f, 1f),
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
        )
    }
}
