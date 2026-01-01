package com.example.samplekmp.di

import com.apollographql.apollo.ApolloClient
import com.example.samplekmp.PokemonDetailUseCase
import com.example.samplekmp.PokemonUseCase
import com.example.samplekmp.navigation.Navigator
import org.koin.dsl.module

val appModule = module {
    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl("https://graphql.pokeapi.co/v1beta2")
            .build()
    }
    factory { (navigator: Navigator) -> PokemonUseCase(get(), navigator) }
    factory { (navigator: Navigator, id: Int) -> PokemonDetailUseCase(get(), navigator, id) }
}
