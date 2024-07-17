package com.example.samplekmp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
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
        val urlString = "https://pokeapi.co/api/v2/pokemon/$id"
        println("urlはこれ")
        println(message = urlString)
        val response = httpClient.get(urlString)
        println(response.toString())
        return response.body()
    }
}

