package com.example.samplekmp.di

import com.example.samplekmp.PokemonDetailUseCase
import com.example.samplekmp.PokemonUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

class KoinHelper : KoinComponent {
    private val _pokemonUseCase: PokemonUseCase by inject()

    fun getPokemonUseCase(): PokemonUseCase = _pokemonUseCase

    fun getPokemonDetailUseCase(pokemonId: Int): PokemonDetailUseCase {
        return get { parametersOf(pokemonId) }
    }

    companion object {
        val shared = KoinHelper()
    }
}
