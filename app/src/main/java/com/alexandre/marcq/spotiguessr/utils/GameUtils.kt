package com.alexandre.marcq.spotiguessr.utils

import java.util.*

enum class GameScoreStatus { NONE, NAME, ARTIST }

enum class GameDifficulty { EASY, MEDIUM, HARD }

object GameScore {
    fun sanitizeInput(input: String): String {
        return input.toLowerCase(Locale.ROOT).replace(Regex("[^\\w()-]|\\(.+\\)|ft.+|-.+"), "")
    }

    fun doesMatch(given: String, expected: String) : Boolean {
        return given == expected
    }
}

