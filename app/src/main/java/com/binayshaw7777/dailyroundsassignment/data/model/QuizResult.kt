package com.binayshaw7777.dailyroundsassignment.data.model

data class QuizResult(
    val id: Long = 0,
    val correctCount: Int,
    val totalQuestions: Int,
    val longestStreak: Int,
    val skippedCount: Int,
    val timestamp: Long,
    val isWin: Boolean,
) {
    val accuracy: Int get() = if (totalQuestions > 0) correctCount * 100 / totalQuestions else 0
}
