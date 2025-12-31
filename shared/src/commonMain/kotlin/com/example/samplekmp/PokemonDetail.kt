package com.example.samplekmp

import com.example.samplekmp.graphql.PokemonDetailQuery

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int?,
    val weight: Int?,
    val spriteUrl: String?,
    val types: List<String>,
    val stats: List<PokemonStat>,
    val abilities: List<PokemonAbility>
)

data class PokemonStat(
    val name: String,
    val baseStat: Int
)

data class PokemonAbility(
    val name: String,
    val isHidden: Boolean
)

fun PokemonDetailQuery.Pokemon.toPokemonDetail(): PokemonDetail {
    return PokemonDetail(
        id = id,
        name = name,
        height = height,
        weight = weight,
        spriteUrl = pokemonsprites.firstOrNull()?.sprites as? String,
        types = pokemontypes.mapNotNull { it.type?.name },
        stats = pokemonstats.mapNotNull { stat ->
            stat.stat?.name?.let { name ->
                PokemonStat(name = name, baseStat = stat.base_stat)
            }
        },
        abilities = pokemonabilities.mapNotNull { ability ->
            ability.ability?.name?.let { name ->
                PokemonAbility(name = name, isHidden = ability.is_hidden)
            }
        }
    )
}
