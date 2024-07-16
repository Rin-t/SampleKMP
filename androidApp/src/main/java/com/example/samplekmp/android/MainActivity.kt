package com.example.samplekmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.samplekmp.Pokemon
import com.example.samplekmp.PokemonListViewModel
import com.example.samplekmp.Sprity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    PokemonListPage()
                }
            }
        }
    }
}

@Composable
fun PokemonListPage(viewModel: PokemonListViewModel = PokemonListViewModel()) {
    val pokemon by viewModel.pokemon.collectAsState()
    LaunchedEffect(Unit) {
        println("LaunchedEffect")
        viewModel.onAppear()
    }
    PokemonCardView(pokemon)
}

@Composable
fun PokemonCardView(pokemon: Pokemon?) {
    Text(text = pokemon?.name ?: "Loading...")
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        //PokemonCardView(Pokemon("Pikachu", Sprity("","")))
    }
}
