package com.example.samplekmp

import com.apollographql.apollo.ApolloClient

object ApolloClientProvider {
    val apolloClient: ApolloClient by lazy {
        ApolloClient.Builder()
            .serverUrl("https://graphql.pokeapi.co/v1beta2")
            .build()
    }
}
