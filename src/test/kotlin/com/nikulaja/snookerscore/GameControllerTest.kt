package com.nikulaja.snookerscore

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class GameControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `test startGame`() {
        // Create a JSON payload with the game request parameters
        val json = """
            {
                "player1Name": "Player 1",
                "player2Name": "Player 2"
            }
        """.trimIndent()

        // Send a POST request to the /api/games endpoint with the JSON payload
        mockMvc.perform(
            post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.player1Name").value("Player 1"))
            .andExpect(jsonPath("$.player2Name").value("Player 2"))
    }

    @Test
    fun `test startGame with wrong parameters`() {
        // Create a JSON payload with the wrong game request parameters
        val json = """
            {
                "player1Name": "",
                "player2Name": "John"
            }
        """.trimIndent()

        // Send a POST request to the /api/games endpoint with the JSON payload
        mockMvc.perform(
            post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Player 1 name cannot be empty"))
    }
}
