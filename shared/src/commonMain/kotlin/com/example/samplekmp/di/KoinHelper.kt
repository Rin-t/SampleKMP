package com.example.samplekmp.di

import com.example.samplekmp.PokemonUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

class KoinHelper : KoinComponent {
    private val _pokemonUseCase: PokemonUseCase by inject()

    fun getPokemonUseCase(): PokemonUseCase = _pokemonUseCase

    companion object {
        val shared = KoinHelper()
    }
}
