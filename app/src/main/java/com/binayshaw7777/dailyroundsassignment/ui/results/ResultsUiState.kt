package com.binayshaw7777.dailyroundsassignment.ui.results

data class ResultsUiState(
    val correctCount: Int = 0,
    val totalQuestions: Int = 0,
    val longestStreak: Int = 0,
    val skippedCount: Int = 0,
) {
    val accuracy: Int get() = if (totalQuestions > 0) correctCount * 100 / totalQuestions else 0
}
