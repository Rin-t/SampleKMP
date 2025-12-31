package com.example.samplekmp.android.view.page

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

@Composable
fun PokemonListPage(modifier: Modifier = Modifier) {
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

    when (val state = uiState) {
        is PokemonListUiState.Loading -> {
            LoadingIndicator(modifier = modifier)
        }
        is PokemonListUiState.Success -> {
            PokemonGrid(
                pokemonList = state.pokemonList,
                modifier = modifier
            )
        }
        is PokemonListUiState.Error -> {
            ErrorMessage(
                message = state.message,
                onRetry = loadPokemonList,
                modifier = modifier
            )
        }
    }
}
