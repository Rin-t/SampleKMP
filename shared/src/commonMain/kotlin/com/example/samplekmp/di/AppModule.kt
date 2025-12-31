package com.example.samplekmp.di

import com.apollographql.apollo.ApolloClient
import com.example.samplekmp.PokemonUseCase
import org.koin.dsl.module

val appModule = module {
    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl("https://graphql.pokeapi.co/v1beta2")
            .build()
    }
    factory { PokemonUseCase(get()) }
}
