package com.nikulaja.snookerscore

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest
class GameControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var gameRepository: GameRepository

    @Test
    fun `test startGame`() {
        val player1Name = "Player 1"
        val player2Name = "Player 2"
        val game = Game(UUID.randomUUID(), player1Name, player2Name, true)
        every { gameRepository.save(any()) } returns game

        val json = """
            {
                "player1Name": "$player1Name",
                "player2Name": "$player2Name"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json),
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.player1Name").value(player1Name))
            .andExpect(jsonPath("$.player2Name").value(player2Name))

        verify(exactly = 1) { gameRepository.save(any()) }
        verify {
            gameRepository.save(
                withArg {
                    assertTrue(player1Name == it.player1Name)
                    assertTrue(player2Name == it.player2Name)
                },
            )
        }
    }

    @Test
    fun `test startGame with wrong parameters`() {
        val json = """
            {
                "player1Name": "",
                "player2Name": "John"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json),
        )
            .andExpect(status().isBadRequest())
    }

    @Test
    fun `test startGame with missing parameters`() {
        val json = """
            {
                "player2Name": "John"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json),
        )
            .andExpect(status().isBadRequest())
    }
}
