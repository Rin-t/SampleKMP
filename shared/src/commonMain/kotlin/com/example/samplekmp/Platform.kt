package com.example.samplekmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform