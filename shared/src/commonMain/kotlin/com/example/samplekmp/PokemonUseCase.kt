package com.example.samplekmp

import com.example.samplekmp.graphql.PokemonCollectionPageQuery

class PokemonUseCase {
    private val apolloClient = ApolloClientProvider.apolloClient

    suspend fun fetchPokemonList(limit: Int, offset: Int): List<PokemonListItem> {
        val response = apolloClient.query(
            PokemonCollectionPageQuery(limit = limit, offset = offset)
        ).execute()

        return response.data?.pokemon?.map { it.toPokemonListItem() } ?: emptyList()
    }
}
