package com.example.samplekmp

import com.example.samplekmp.graphql.PokemonCollectionPageQuery

data class PokemonListItem(
    val id: Int,
    val name: String,
    val spriteUrl: String?
)

fun PokemonCollectionPageQuery.Pokemon.toPokemonListItem(): PokemonListItem {
    return PokemonListItem(
        id = id,
        name = name,
        spriteUrl = pokemonsprites.firstOrNull()?.sprites as? String
    )
}