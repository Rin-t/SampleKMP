package com.example.samplekmp

class PokemonUseCase {

    suspend fun fetchPokemonList(from: Int = 1, to: Int = 151): List<Pokemon> {
        val pokemonList = PokemonClient().fetchPokemonList(from, to)
        return pokemonList
    }

    suspend fun fetchPokemon(id: Int): Pokemon {
        val pokemon = PokemonClient().fetchPokemon(id)
        return pokemon
    }
}
