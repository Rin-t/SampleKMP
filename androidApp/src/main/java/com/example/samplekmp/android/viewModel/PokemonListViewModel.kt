package com.example.samplekmp.android.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplekmp.Pokemon
import com.example.samplekmp.PokemonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel: ViewModel() {
    private val pokemonUseCase = PokemonUseCase()

    private val _pokemon = MutableStateFlow<List<Pokemon>>(listOf())
    val pokemon: StateFlow<List<Pokemon>> = _pokemon.asStateFlow()

    fun onAppear() {
        println("onAppear")
        viewModelScope.launch {
            try {
                println("fetching Pokemon")
                val pokemon = pokemonUseCase.fetchPokemonList()
                _pokemon.emit(pokemon)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}