package com.example.samplekmp.di

import com.example.samplekmp.PokemonDetailUseCase
import com.example.samplekmp.PokemonUseCase
import com.example.samplekmp.navigation.Navigator
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

object KoinProvider : KoinComponent {
    fun init() {
        startKoin {
            modules(appModule)
        }
    }

    fun getPokemonUseCase(navigator: Navigator): PokemonUseCase {
        return get { parametersOf(navigator) }
    }

    fun getPokemonDetailUseCase(navigator: Navigator, pokemonId: Int): PokemonDetailUseCase {
        return get { parametersOf(navigator, pokemonId) }
    }
}
