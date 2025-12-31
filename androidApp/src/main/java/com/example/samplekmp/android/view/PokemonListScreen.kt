package com.example.samplekmp.android.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.samplekmp.android.view.components.ErrorMessage
import com.example.samplekmp.android.view.components.LoadingIndicator
import com.example.samplekmp.android.view.components.PokemonGrid
import com.example.samplekmp.android.viewModel.PokemonListUiState
import com.example.samplekmp.android.viewModel.PokemonListViewModel

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                onRetry = { viewModel.loadPokemonList() },
                modifier = modifier
            )
        }
    }
}
