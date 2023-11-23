package com.nikulaja.snookerscore

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import java.time.Instant

@Controller
class HtmlController(private val gameController: GameController) {

    @GetMapping("/")
    fun home(model: Model): String {
        model["title"] = "SnookerScore"
        model["serverTime"] = Instant.now().toString()
        model["newGameForm"] = NewGameForm()
        model["games"] = gameController.getGames().body
        return "index"
    }

    @PostMapping("/games")
    fun games(@Valid newGameForm: NewGameForm, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return "index"
        }
        gameController.startGame(NewGameRequest(newGameForm.player1Name!!, newGameForm.player2Name!!))
        return "redirect:/"
    }
}

class NewGameForm {
    @NotBlank
    @Size(min = 2, max = 30, message = "Player name must be between 2 and 30 characters")
    var player1Name: String? = null

    @NotBlank
    @Size(min = 2, max = 30, message = "Player name must be between 2 and 30 characters")
    var player2Name: String? = null
}
