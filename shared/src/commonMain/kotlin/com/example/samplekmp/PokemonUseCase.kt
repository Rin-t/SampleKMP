package com.example.samplekmp

import com.apollographql.apollo.ApolloClient
import com.example.samplekmp.graphql.PokemonCollectionPageQuery
import com.example.samplekmp.navigation.Destination
import com.example.samplekmp.navigation.Navigator

class PokemonUseCase(
    private val apolloClient: ApolloClient,
    private val navigator: Navigator
) {
    suspend fun fetchPokemonList(limit: Int, offset: Int): List<PokemonListItem> {
        val response = apolloClient.query(
            PokemonCollectionPageQuery(limit = limit, offset = offset)
        ).execute()

        return response.data?.pokemon?.map { it.toPokemonListItem() } ?: emptyList()
    }

    fun onTapGrid(pokemonId: Int) {
        navigator.navigate(Destination.PokemonDetail(pokemonId))
    }
}
