package com.example.firstprojectapplication

data class PlayerData(
    val id: String = "", // Firebase generates unique IDs
    val playerName: String = "",
    val mode: String = "", // "Truth" or "Dare"
    val challenge: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    // Empty constructor needed for Firebase
    constructor() : this("", "", "", "", 0L)
}