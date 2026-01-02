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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.samplekmp.RequestStatus
import com.example.samplekmp.PokemonDetail
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
    val navigator = LocalNavigator.current
    val useCase: PokemonDetailUseCase = koinInject { parametersOf(navigator, pokemonId) }
    val state by useCase.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(pokemonId) {
        useCase.onAppear()
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

        when (val status = state.status) {
            is RequestStatus.Fetching -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is RequestStatus.Success -> {
                state.pokemonDetail?.let { detail ->
                    PokemonDetailContent(
                        pokemonDetail = detail,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            is RequestStatus.Failed -> {
                ErrorMessage(
                    message = status.message,
                    onRetry = { scope.launch { useCase.onAppear() } },
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
        HeaderSection(pokemonDetail)
        Spacer(modifier = Modifier.height(8.dp))
        TypesSection(pokemonDetail)
        Spacer(modifier = Modifier.height(16.dp))
        PhysicalSection(pokemonDetail)
        Spacer(modifier = Modifier.height(24.dp))
        StatsSection(pokemonDetail)
        Spacer(modifier = Modifier.height(24.dp))
        AbilitiesSection(pokemonDetail)
    }
}

@Composable
private fun HeaderSection(pokemonDetail: PokemonDetail) {
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
}

@Composable
private fun TypesSection(pokemonDetail: PokemonDetail) {
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
}

@Composable
private fun PhysicalSection(pokemonDetail: PokemonDetail) {
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
}

@Composable
private fun StatsSection(pokemonDetail: PokemonDetail) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Base Stats",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        pokemonDetail.stats.forEach { stat ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stat.name,
                    modifier = Modifier.width(100.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stat.baseStat.toString(),
                    modifier = Modifier.width(40.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                LinearProgressIndicator(
                    progress = (stat.baseStat / 255f).coerceIn(0f, 1f),
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AbilitiesSection(pokemonDetail: PokemonDetail) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Abilities",
            style = MaterialTheme.typography.titleMedium
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
