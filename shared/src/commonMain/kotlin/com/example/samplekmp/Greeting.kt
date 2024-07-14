package com.example.samplekmp

import kotlinx.serialization.Serializable

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
