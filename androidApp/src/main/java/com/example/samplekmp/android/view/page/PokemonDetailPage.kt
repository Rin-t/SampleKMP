package com.example.samplekmp.android.view.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.samplekmp.PokemonDetail
import com.example.samplekmp.PokemonDetailUseCase
import com.example.samplekmp.RequestStatus
import com.example.samplekmp.android.navigation.LocalNavigator
import com.example.samplekmp.android.view.components.ErrorMessage
import com.example.samplekmp.android.view.components.LoadingIndicator
import com.example.samplekmp.android.view.components.PokemonTypeColors
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
                        imageVector = Icons.Filled.ArrowBack,
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
    val primaryTypeColor = pokemonDetail.types.firstOrNull()?.let {
        PokemonTypeColors.getColorForType(it)
    } ?: MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection(pokemonDetail, primaryTypeColor)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            TypesSection(pokemonDetail)
            Spacer(modifier = Modifier.height(20.dp))
            PhysicalSection(pokemonDetail)
            Spacer(modifier = Modifier.height(20.dp))
            StatsSection(pokemonDetail)
            Spacer(modifier = Modifier.height(20.dp))
            AbilitiesSection(pokemonDetail)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeaderSection(pokemonDetail: PokemonDetail, primaryTypeColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        primaryTypeColor.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = pokemonDetail.spriteUrl,
                    contentDescription = pokemonDetail.name,
                    modifier = Modifier.size(160.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "#${pokemonDetail.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = pokemonDetail.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TypesSection(pokemonDetail: PokemonDetail) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        pokemonDetail.types.forEach { type ->
            val typeColor = PokemonTypeColors.getColorForType(type)
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = typeColor)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = type.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun PhysicalSection(pokemonDetail: PokemonDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Height",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(pokemonDetail.height ?: 0) / 10.0} m",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Weight",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(pokemonDetail.weight ?: 0) / 10.0} kg",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun StatsSection(pokemonDetail: PokemonDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Base Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            pokemonDetail.stats.forEach { stat ->
                StatBar(
                    displayName = stat.displayName,
                    value = stat.baseStat,
                    maxValue = 255
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun StatBar(
    displayName: String,
    value: Int,
    maxValue: Int
) {
    val statColor = when {
        value < 50 -> Color(0xFFFF5252)
        value < 80 -> Color(0xFFFFB74D)
        value < 100 -> Color(0xFFFFEB3B)
        value < 120 -> Color(0xFF9CCC65)
        else -> Color(0xFF66BB6A)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayName,
            modifier = Modifier.width(45.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.toString().padStart(3, ' '),
            modifier = Modifier.width(35.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (value.toFloat() / maxValue).coerceIn(0f, 1f))
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(statColor)
            )
        }
    }
}

@Composable
private fun AbilitiesSection(pokemonDetail: PokemonDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Abilities",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            pokemonDetail.abilities.forEachIndexed { index, ability ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (ability.isHidden)
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        MaterialTheme.colorScheme.primary
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = ability.name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (ability.isHidden) {
                        Text(
                            text = "Hidden",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                if (index < pokemonDetail.abilities.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
