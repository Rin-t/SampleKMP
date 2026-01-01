package com.example.samplekmp

import com.apollographql.apollo.ApolloClient
import com.example.samplekmp.graphql.PokemonDetailQuery
import com.example.samplekmp.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PokemonDetailUseCase(
    private val apolloClient: ApolloClient,
    private val navigator: Navigator,
    private val pokemonId: Int
) {
    private val _state = MutableStateFlow(PokemonDetailState())
    val state: StateFlow<PokemonDetailState> = _state.asStateFlow()

    suspend fun onAppear() {
        _state.value = _state.value.copy(status = RequestStatus.Fetching)
        try {
            val response = apolloClient.query(
                PokemonDetailQuery(id = pokemonId)
            ).execute()
            val detail = response.data?.pokemon?.firstOrNull()?.toPokemonDetail()
            if (detail != null) {
                _state.value = _state.value.copy(
                    status = RequestStatus.Success,
                    pokemonDetail = detail
                )
            } else {
                _state.value = _state.value.copy(
                    status = RequestStatus.Failed("Pokemon not found")
                )
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                status = RequestStatus.Failed(e.message ?: "Unknown error")
            )
        }
    }

    fun navigateBack() {
        navigator.navigateBack()
    }
}
