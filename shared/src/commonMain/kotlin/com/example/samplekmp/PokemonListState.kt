package com.example.samplekmp

data class PokemonListState(
    val status: RequestStatus = RequestStatus.Fetching,
    val pokemonList: List<PokemonListItem> = emptyList()
) {
    constructor() : this(RequestStatus.Fetching, emptyList())
}
