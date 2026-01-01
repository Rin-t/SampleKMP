package com.example.samplekmp.navigation

sealed class Destination {
    data object PokemonList : Destination()
    data object Menu : Destination()
    data class PokemonDetail(val pokemonId: Int) : Destination()
}
