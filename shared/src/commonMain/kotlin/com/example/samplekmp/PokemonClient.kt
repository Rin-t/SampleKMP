package com.example.samplekmp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

interface APIClient {
    val httpClient: HttpClient
        get() = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
}


class PokemonClient: APIClient {

    suspend fun fetchPokemon(id: Int): Pokemon {
        val pokemon: Pokemon = httpClient.get("https://pokeapi.co/api/v2/pokemon/1").body()
        return pokemon
    }

    suspend fun fetchPokemonList(from: Int, to: Int): List<Pokemon> = coroutineScope {
        val requests = (from..to).map { id ->
            async {
                fetchPokemon(id)
            }
        }
        requests.awaitAll()
    }
}

