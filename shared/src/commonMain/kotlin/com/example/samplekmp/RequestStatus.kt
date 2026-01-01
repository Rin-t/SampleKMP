package com.example.samplekmp

sealed class RequestStatus {
    data object Fetching : RequestStatus()
    data object Success : RequestStatus()
    data class Failed(val message: String) : RequestStatus()
}
