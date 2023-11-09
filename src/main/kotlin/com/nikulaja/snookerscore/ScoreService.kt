package com.nikulaja.snookerscore

import org.springframework.stereotype.Component
import java.util.HashMap

@Component
class ScoreService {

    private val scores = HashMap<Long, Int>()

    fun addScore(userId: Long, score: Int) {
        scores[userId] = score
    }

    fun getScore(userId: Long): Int {
        return scores[userId]?: 0
    }
}
