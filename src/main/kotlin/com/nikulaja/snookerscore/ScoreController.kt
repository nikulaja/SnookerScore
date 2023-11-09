package com.nikulaja.snookerscore

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ScoreController(private val scoreService: ScoreService) {

    @GetMapping("/score")
    fun getScore(@RequestParam userId: Long): ResponseEntity<Int> {
        val score = scoreService.getScore(userId)
        return ResponseEntity.ok(score)
    }

    @PutMapping("/score")
    fun modifyScore(@RequestParam userId: Long, @RequestParam score: Int): ResponseEntity<Unit> {
        scoreService.addScore(userId, score)
        return ResponseEntity.noContent().build()
    }
}
