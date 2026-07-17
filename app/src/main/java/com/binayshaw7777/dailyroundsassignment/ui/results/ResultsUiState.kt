package com.binayshaw7777.dailyroundsassignment.ui.results

import com.binayshaw7777.dailyroundsassignment.util.calculateAccuracy

data class ResultsUiState(
    val correctCount: Int = 0,
    val totalQuestions: Int = 0,
    val longestStreak: Int = 0,
    val skippedCount: Int = 0,
) {
    val accuracy: Int get() = calculateAccuracy(correctCount, totalQuestions)
}
