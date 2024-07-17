package com.example.samplekmp

class PokemonUseCase {
    suspend fun fetchPokemon(id: Int): Pokemon {
        val pokemon = PokemonClient().fetchPokemon(id)
        return pokemon
    }
}
