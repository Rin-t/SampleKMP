package com.example.samplekmp

data class PokemonDetailState(
    val status: RequestStatus = RequestStatus.Fetching,
    val pokemonDetail: PokemonDetail? = null
) {
    constructor() : this(RequestStatus.Fetching, null)
}
