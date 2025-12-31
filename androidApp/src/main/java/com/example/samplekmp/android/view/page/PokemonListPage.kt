package com.example.samplekmp.android.view.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.samplekmp.PokemonListUiState
import com.example.samplekmp.PokemonUseCase
import com.example.samplekmp.android.view.components.ErrorMessage
import com.example.samplekmp.android.view.components.LoadingIndicator
import com.example.samplekmp.android.view.components.PokemonGrid
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListPage(
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var uiState by remember { mutableStateOf<PokemonListUiState>(PokemonListUiState.Loading) }
    val useCase: PokemonUseCase = koinInject()
    val scope = rememberCoroutineScope()

    val loadPokemonList: () -> Unit = {
        scope.launch {
            uiState = PokemonListUiState.Loading
            try {
                val list = useCase.fetchPokemonList(50, 0)
                uiState = PokemonListUiState.Success(list)
            } catch (e: Exception) {
                uiState = PokemonListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    LaunchedEffect(Unit) {
        loadPokemonList()
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("ポケモン図鑑") }
        )

        when (val state = uiState) {
            is PokemonListUiState.Loading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
            is PokemonListUiState.Success -> {
                PokemonGrid(
                    pokemonList = state.pokemonList,
                    onItemClick = { pokemon -> onNavigateToDetail(pokemon.id) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is PokemonListUiState.Error -> {
                ErrorMessage(
                    message = state.message,
                    onRetry = loadPokemonList,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
