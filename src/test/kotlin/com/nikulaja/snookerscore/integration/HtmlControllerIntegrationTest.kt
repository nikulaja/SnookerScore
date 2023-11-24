package com.nikulaja.snookerscore.integration

import com.nikulaja.snookerscore.Game
import com.nikulaja.snookerscore.NewGameForm
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

class HtmlControllerIntegrationTest : IntegrationTestBase() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `test home page`() {
        val expectedTitle = "SnookerScore"
        val expectedNewGameForm = NewGameForm()
        val expectedGames = listOf<Game>()

        val model = mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(model().attribute("title", expectedTitle))
            .andExpect(model().attribute("serverTime", notNullValue()))
            .andExpect(model().attribute("newGameForm", notNullValue()))
            .andExpect(model().attribute("games", expectedGames))
            .andExpect(view().name("index"))
            .andReturn().modelAndView!!.model

        assertThat(model["newGameForm"]!! == expectedNewGameForm)
    }

    @Test
    fun `test create new game`() {
        val player1Name = "John"
        val player2Name = "Doe"
        val expectedRedirectUrl = "/"

        val newGameForm = NewGameForm()
        newGameForm.player1Name = player1Name
        newGameForm.player2Name = player2Name

        mockMvc.perform(
            post("/games")
                .param("player1Name", player1Name)
                .param("player2Name", player2Name),
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(expectedRedirectUrl))
    }

    @Test
    fun `test create new game with invalid form data`() {
        val player1Name = ""
        val player2Name = "Doe"

        val newGameForm = NewGameForm()
        newGameForm.player1Name = player1Name
        newGameForm.player2Name = player2Name

        mockMvc.perform(
            post("/games")
                .param("player1Name", player1Name)
                .param("player2Name", player2Name),
        )
            .andExpect(status().isOk)
            .andExpect(model().attributeHasFieldErrors("newGameForm", "player1Name"))
            .andExpect(view().name("index"))
    }
}
