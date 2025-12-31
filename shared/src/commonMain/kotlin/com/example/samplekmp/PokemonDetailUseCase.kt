package com.example.samplekmp

import com.apollographql.apollo.ApolloClient
import com.example.samplekmp.graphql.PokemonDetailQuery

class PokemonDetailUseCase(
    private val apolloClient: ApolloClient,
    private val pokemonId: Int
) {
    suspend fun fetchPokemonDetail(): PokemonDetail? {
        val response = apolloClient.query(
            PokemonDetailQuery(id = pokemonId)
        ).execute()

        return response.data?.pokemon?.firstOrNull()?.toPokemonDetail()
    }
}
