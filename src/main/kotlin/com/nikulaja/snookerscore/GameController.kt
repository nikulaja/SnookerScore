package com.nikulaja.snookerscore

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class GameController {

    @PostMapping("/games")
    fun startGame(@Valid @RequestBody gameRequest: GameRequest): ResponseEntity<Game> {
        val game = Game(gameRequest.player1Name, gameRequest.player2Name)
        return ResponseEntity.ok(game)
    }
}

data class GameRequest(
    @field:NotBlank(message = "Player 1 name cannot be empty")
    val player1Name: String,
    @field:NotBlank(message = "Player 2 name cannot be empty")
    val player2Name: String)
