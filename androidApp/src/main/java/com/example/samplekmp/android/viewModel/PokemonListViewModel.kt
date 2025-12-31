package com.example.samplekmp.android.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplekmp.PokemonListItem
import com.example.samplekmp.PokemonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PokemonListUiState {
    data object Loading : PokemonListUiState()
    data class Success(val pokemonList: List<PokemonListItem>) : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}

class PokemonListViewModel : ViewModel() {
    private val pokemonUseCase = PokemonUseCase()

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        loadPokemonList()
    }

    fun loadPokemonList() {
        viewModelScope.launch {
            _uiState.emit(PokemonListUiState.Loading)
            try {
                val pokemonList = pokemonUseCase.fetchPokemonList(limit = 50, offset = 0)
                _uiState.emit(PokemonListUiState.Success(pokemonList))
            } catch (e: Exception) {
                _uiState.emit(PokemonListUiState.Error(e.message ?: "Unknown error"))
            }
        }
    }
}