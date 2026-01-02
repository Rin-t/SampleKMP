package com.example.samplekmp.android.view.components

import androidx.compose.ui.graphics.Color

object PokemonTypeColors {
    val normal = Color(0xFFA8A878)
    val fire = Color(0xFFF08030)
    val water = Color(0xFF6890F0)
    val electric = Color(0xFFF8D030)
    val grass = Color(0xFF78C850)
    val ice = Color(0xFF98D8D8)
    val fighting = Color(0xFFC03028)
    val poison = Color(0xFFA040A0)
    val ground = Color(0xFFE0C068)
    val flying = Color(0xFFA890F0)
    val psychic = Color(0xFFF85888)
    val bug = Color(0xFFA8B820)
    val rock = Color(0xFFB8A038)
    val ghost = Color(0xFF705898)
    val dragon = Color(0xFF7038F8)
    val dark = Color(0xFF705848)
    val steel = Color(0xFFB8B8D0)
    val fairy = Color(0xFFEE99AC)

    fun getColorForType(typeName: String): Color {
        return when (typeName.lowercase()) {
            "normal" -> normal
            "fire" -> fire
            "water" -> water
            "electric" -> electric
            "grass" -> grass
            "ice" -> ice
            "fighting" -> fighting
            "poison" -> poison
            "ground" -> ground
            "flying" -> flying
            "psychic" -> psychic
            "bug" -> bug
            "rock" -> rock
            "ghost" -> ghost
            "dragon" -> dragon
            "dark" -> dark
            "steel" -> steel
            "fairy" -> fairy
            else -> normal
        }
    }
}
