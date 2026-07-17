package com.binayshaw7777.dailyroundsassignment.util

fun calculateAccuracy(correctCount: Int, totalQuestions: Int): Int {
    return if (totalQuestions > 0) correctCount * 100 / totalQuestions else 0
}
