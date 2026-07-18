package com.binayshaw7777.dailyroundsassignment.ui.results

import com.binayshaw7777.dailyroundsassignment.util.calculateAccuracy

/**
 * Immutable UI state for the results screen.
 *
 * All fields default to `0` so the screen can render immediately
 * before the latest quiz result is loaded from the database.
 *
 * @property correctCount Number of correctly answered questions.
 * @property totalQuestions Total questions in the completed quiz.
 * @property longestStreak Highest consecutive-correct streak.
 * @property skippedCount Questions skipped by the user.
 */
data class ResultsUiState(
    val correctCount: Int = 0,
    val totalQuestions: Int = 0,
    val longestStreak: Int = 0,
    val skippedCount: Int = 0,
) {
    /** Accuracy percentage (0–100) computed from [correctCount] / [totalQuestions]. */
    val accuracy: Int get() = calculateAccuracy(correctCount, totalQuestions)
}
