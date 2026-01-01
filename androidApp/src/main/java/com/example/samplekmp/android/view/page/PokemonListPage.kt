package com.example.samplekmp.android.view.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.samplekmp.PokemonUseCase
import com.example.samplekmp.RequestStatus
import com.example.samplekmp.android.navigation.LocalNavigator
import com.example.samplekmp.android.view.components.ErrorMessage
import com.example.samplekmp.android.view.components.LoadingIndicator
import com.example.samplekmp.android.view.components.PokemonGrid
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListPage(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.current
    val useCase: PokemonUseCase = koinInject { parametersOf(navigator) }
    val state by useCase.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        useCase.onAppear()
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("ポケモン図鑑") }
        )

        when (val status = state.status) {
            is RequestStatus.Fetching -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
            is RequestStatus.Success -> {
                PokemonGrid(
                    pokemonList = state.pokemonList,
                    onItemClick = { pokemon -> useCase.onTapGrid(pokemon.id) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is RequestStatus.Failed -> {
                ErrorMessage(
                    message = status.message,
                    onRetry = { scope.launch { useCase.onTapRetryButton() } },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
