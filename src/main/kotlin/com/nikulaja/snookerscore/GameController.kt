package com.nikulaja.snookerscore

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class GameController(private val repository: GameRepository) {

    @PostMapping("/games")
    fun startGame(@Valid @RequestBody newGameRequest: NewGameRequest): ResponseEntity<Game> {
        val game = newGameRequest.toGame()
        repository.save(game)
        return ResponseEntity.ok(game)
    }

    @GetMapping("/games")
    fun getGames(): ResponseEntity<List<Game?>> {
        return ResponseEntity.ok(repository.findAll().toList())
    }

    @GetMapping("/games/{id}")
    fun getGameById(@PathVariable id: String): ResponseEntity<Game> {
        val game = repository.findById(UUID.fromString(id))
        return if (game.isPresent) {
            ResponseEntity.ok(game.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

data class NewGameRequest(
    @field:NotBlank
    val player1Name: String,
    @field:NotBlank
    val player2Name: String
) {
    fun toGame(): Game {
        return Game(
            UUID.randomUUID(),
            player1Name,
            player2Name,
            true,
        )
    }
}
