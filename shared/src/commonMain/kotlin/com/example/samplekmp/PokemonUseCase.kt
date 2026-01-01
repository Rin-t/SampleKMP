package com.example.samplekmp

import com.apollographql.apollo.ApolloClient
import com.example.samplekmp.graphql.PokemonCollectionPageQuery
import com.example.samplekmp.navigation.Destination
import com.example.samplekmp.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PokemonUseCase(
    private val apolloClient: ApolloClient,
    private val navigator: Navigator
) {
    private val _state = MutableStateFlow(PokemonListState())
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    suspend fun fetchPokemonList(limit: Int, offset: Int) {
        _state.value = _state.value.copy(status = RequestStatus.Fetching)
        try {
            val response = apolloClient.query(
                PokemonCollectionPageQuery(limit = limit, offset = offset)
            ).execute()
            val list = response.data?.pokemon?.map { it.toPokemonListItem() } ?: emptyList()
            _state.value = _state.value.copy(
                status = RequestStatus.Success,
                pokemonList = list
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                status = RequestStatus.Failed(e.message ?: "Unknown error")
            )
        }
    }

    fun onTapGrid(pokemonId: Int) {
        navigator.navigate(Destination.PokemonDetail(pokemonId))
    }
}
