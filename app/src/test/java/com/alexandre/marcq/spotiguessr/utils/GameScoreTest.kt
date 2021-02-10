package com.alexandre.marcq.spotiguessr.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class GameScoreTest {

    private val input = "test String ! (feat some artist) ft. some guy"

    @Test
    fun sanitizeInput() {
        val expected = "teststring"

        assertThat(GameScore.sanitizeInput(input))
            .matches(expected)
    }
}