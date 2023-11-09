package com.nikulaja.snookerscore.integration

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nikulaja.snookerscore.Game
import com.nikulaja.snookerscore.GameRepository
import com.nikulaja.snookerscore.NewGameRequest
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.sql.DataSource


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GameControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var gameRepository: GameRepository

    private val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @BeforeAll
    fun setUp() {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("INSERT INTO game (player1name, player2name, ongoing, player1score, player2score, timestamp, id) VALUES ('Player 1', 'Player 2', true, 0, 0, '2022-01-01T00:00:00Z', '123e4567-e89b-12d3-a456-426655440000')")
                statement.execute("INSERT INTO game (player1name, player2name, ongoing, player1score, player2score, timestamp, id) VALUES ('Player 3', 'Player 4', false, 5, 10, '2022-01-02T00:00:00Z', '234e5678-f90a-1b2c-d345-678901234567')")
            }
        }
    }

    @AfterAll
    fun tearDown() {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("DROP TABLE game")
            }
        }
    }

    @Test
    fun `start game`() {
        val newGameRequest = NewGameRequest("Player 5", "Player 6")

        val responseBody = mockMvc.perform(
            post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newGameRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.player1Name", equalTo(newGameRequest.player1Name)))
            .andExpect(jsonPath("$.player2Name", equalTo(newGameRequest.player2Name)))
            .andExpect(jsonPath("$.ongoing", equalTo(true)))
            .andReturn().response.contentAsString

        val newGame: Game = mapper.readValue(responseBody)

        val game = gameRepository.findById(newGame.id).get()
        assertThat(game.player1Name).isEqualTo(newGameRequest.player1Name)
        assertThat(game.player2Name).isEqualTo(newGameRequest.player2Name)
        assertThat(game.ongoing).isTrue()
    }

    @Test
    fun `get games`() {
        mockMvc.perform(get("/api/games"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].player1Name", equalTo("Player 1")))
            .andExpect(jsonPath("$[0].player2Name", equalTo("Player 2")))
            .andExpect(jsonPath("$[0].ongoing", equalTo(true)))
            .andExpect(jsonPath("$[1].player1Name", equalTo("Player 3")))
            .andExpect(jsonPath("$[1].player2Name", equalTo("Player 4")))
            .andExpect(jsonPath("$[1].ongoing", equalTo(false)))
    }

    @Test
    fun `get game by id`() {
        mockMvc.perform(get("/api/games/123e4567-e89b-12d3-a456-426655440000"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.player1Name", equalTo("Player 1")))
            .andExpect(jsonPath("$.player2Name", equalTo("Player 2")))
            .andExpect(jsonPath("$.ongoing", equalTo(true)))
    }

    @Test
    fun `get game by non-existent id`() {
        mockMvc.perform(get("/api/games/516e853e-cb2a-4566-8fc3-ca05141fc993"))
            .andExpect(status().isNotFound)
    }
}
