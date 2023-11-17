package com.nikulaja.snookerscore

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant
import java.util.UUID

@Entity
class Game(
    @Id
    val id: UUID,
    val player1Name: String,
    val player2Name: String,
    val ongoing: Boolean,
    var player1Score: Int = 0,
    var player2Score: Int = 0,
    var timestamp: Instant = Instant.now(),
)
