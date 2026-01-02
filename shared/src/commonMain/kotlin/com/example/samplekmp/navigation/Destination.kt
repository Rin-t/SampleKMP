package com.example.samplekmp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    data object PokemonList : Destination()

    @Serializable
    data object Menu : Destination()

    @Serializable
    data class PokemonDetail(val pokemonId: Int) : Destination()
}
