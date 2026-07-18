package com.binayshaw7777.dailyroundsassignment.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizResultTest {

    @Test
    fun accuracy_calculatesCorrectly() {
        val result = QuizResult(
            correctCount = 8,
            totalQuestions = 10,
            longestStreak = 5,
            skippedCount = 1
        )
        assertEquals(80, result.accuracy)
    }

    @Test
    fun accuracy_zeroQuestions_returnsZero() {
        val result = QuizResult(
            correctCount = 0,
            totalQuestions = 0,
            longestStreak = 0,
            skippedCount = 0
        )
        assertEquals(0, result.accuracy)
    }

    @Test
    fun isWin_moreThanOrEqualHalf_returnsTrue() {
        val winAtHalf = QuizResult(
            correctCount = 5,
            totalQuestions = 10,
            longestStreak = 3,
            skippedCount = 0
        )
        assertTrue(winAtHalf.isWin)

        val winMoreThanHalf = QuizResult(
            correctCount = 6,
            totalQuestions = 10,
            longestStreak = 4,
            skippedCount = 0
        )
        assertTrue(winMoreThanHalf.isWin)
    }

    @Test
    fun isWin_lessThanHalf_returnsFalse() {
        val loseResult = QuizResult(
            correctCount = 4,
            totalQuestions = 10,
            longestStreak = 2,
            skippedCount = 0
        )
        assertFalse(loseResult.isWin)
    }
}
