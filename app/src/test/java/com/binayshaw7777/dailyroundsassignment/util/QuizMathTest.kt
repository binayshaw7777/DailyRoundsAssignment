package com.binayshaw7777.dailyroundsassignment.util

import org.junit.Assert.assertEquals
import org.junit.Test

class QuizMathTest {

    @Test
    fun calculateAccuracy_correctCalculations() {
        assertEquals(70, calculateAccuracy(7, 10))
        assertEquals(100, calculateAccuracy(5, 5))
        assertEquals(0, calculateAccuracy(0, 10))
    }

    @Test
    fun calculateAccuracy_totalZero_returnsZero() {
        assertEquals(0, calculateAccuracy(5, 0))
        assertEquals(0, calculateAccuracy(0, 0))
    }

    @Test
    fun calculateAccuracy_handlesRounding() {
        // 1 / 3 = 33.333% -> 33% (integer division: 1 * 100 / 3 = 33)
        assertEquals(33, calculateAccuracy(1, 3))
        // 2 / 3 = 66.666% -> 66% (integer division: 2 * 100 / 3 = 66)
        assertEquals(66, calculateAccuracy(2, 3))
    }
}
