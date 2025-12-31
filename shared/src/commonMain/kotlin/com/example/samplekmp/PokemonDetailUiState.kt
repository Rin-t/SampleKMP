package com.example.samplekmp

sealed class PokemonDetailUiState {
    data object Loading : PokemonDetailUiState()
    data class Success(val pokemonDetail: PokemonDetail) : PokemonDetailUiState()
    data class Error(val message: String) : PokemonDetailUiState()
}
