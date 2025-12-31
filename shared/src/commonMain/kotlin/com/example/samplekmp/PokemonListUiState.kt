package com.example.samplekmp

sealed class PokemonListUiState {
    data object Loading : PokemonListUiState()
    data class Success(val pokemonList: List<PokemonListItem>) : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}
