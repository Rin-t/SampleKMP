package com.example.samplekmp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    @SerialName("name")
    val name: String,
    @SerialName("sprites")
    val sprities: Sprity
)

@Serializable
data class Sprity(
    @SerialName("front_default")
    val normal: String,
    @SerialName("front_shiny")
    val shiny: String
)