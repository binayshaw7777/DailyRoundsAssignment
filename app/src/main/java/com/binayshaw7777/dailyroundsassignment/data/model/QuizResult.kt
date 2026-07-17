package com.binayshaw7777.dailyroundsassignment.data.model

import com.binayshaw7777.dailyroundsassignment.util.calculateAccuracy

data class QuizResult(
    val id: Long = 0,
    val correctCount: Int,
    val totalQuestions: Int,
    val longestStreak: Int,
    val skippedCount: Int,
    val timestamp: Long,
    val isWin: Boolean,
) {
    val accuracy: Int get() = calculateAccuracy(correctCount, totalQuestions)
}
