package com.example.samplekmp.android.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplekmp.Pokemon
import com.example.samplekmp.PokemonUseCase
import com.example.samplekmp.Sprity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel: ViewModel() {
    private val pokemonUseCase = PokemonUseCase()

    private val _pokemon = MutableStateFlow<Pokemon?>(null)
    val pokemon: StateFlow<Pokemon?> = _pokemon.asStateFlow()

    fun onClickSearchButton(id: Int) {
        println("onClickSearchButton")
        viewModelScope.launch {
            try {
                println("fetching Pokemon")
                val pokemon = pokemonUseCase.fetchPokemon(id)
                _pokemon.emit(pokemon)
                println("pokemon name")
                println(pokemon.name)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}