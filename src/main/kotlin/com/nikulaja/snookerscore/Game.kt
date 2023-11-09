package com.nikulaja.snookerscore

import java.time.Instant

data class Game(
    val player1Name: String,
    val player2Name: String,
    var player1Score: Int = 0,
    var player2Score: Int = 0,
    val timestamp: Instant = Instant.now()
)
