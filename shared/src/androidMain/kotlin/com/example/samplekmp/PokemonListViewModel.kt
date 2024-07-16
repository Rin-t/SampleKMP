package com.example.samplekmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel: ViewModel() {
    private val pokemonUseCase = PokemonUseCase()

    private val _pokemon = MutableStateFlow<Pokemon?>(null)
    val pokemon: StateFlow<Pokemon?> = _pokemon.asStateFlow()

    fun onAppear() {
        println("onAppear")
        viewModelScope.launch {
            try {
                println("fetching Pokemon")
                val pokemon = pokemonUseCase.fetchPokemon(1)
                print(pokemon.name)
                _pokemon.emit(pokemon)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}