package com.example.samplekmp

import com.apollographql.apollo.ApolloClient
import com.example.samplekmp.graphql.PokemonDetailQuery
import com.example.samplekmp.navigation.Navigator

class PokemonDetailUseCase(
    private val apolloClient: ApolloClient,
    private val navigator: Navigator,
    private val pokemonId: Int
) {
    suspend fun fetchPokemonDetail(): PokemonDetail? {
        val response = apolloClient.query(
            PokemonDetailQuery(id = pokemonId)
        ).execute()

        return response.data?.pokemon?.firstOrNull()?.toPokemonDetail()
    }

    fun navigateBack() {
        navigator.navigateBack()
    }
}
