package com.binayshaw7777.dailyroundsassignment.data.model

import com.binayshaw7777.dailyroundsassignment.util.calculateAccuracy

/**
 * Immutable domain model representing the outcome of a completed quiz session.
 *
 * Persisted via [com.binayshaw7777.dailyroundsassignment.data.local.db.QuizResultEntity]
 * and surfaced to the UI through [com.binayshaw7777.dailyroundsassignment.ui.leaderboard.LeaderboardUiState].
 *
 * @property id Auto-generated primary key. Defaults to `0` for new (unsaved) results.
 * @property correctCount Number of questions answered correctly.
 * @property totalQuestions Total number of questions in the quiz session.
 * @property longestStreak The highest consecutive-correct-answer streak achieved during the quiz.
 * @property skippedCount Number of questions the user skipped.
 * @property timestamp Epoch millis when the quiz was completed.
 * @property isWin `true` when [correctCount] >= 50% of [totalQuestions].
 *
 * @see com.binayshaw7777.dailyroundsassignment.domain.usecase.SaveQuizResultUseCase for
 *   how [isWin] is determined.
 */
data class QuizResult(
    val id: Long = 0,
    val correctCount: Int,
    val totalQuestions: Int,
    val longestStreak: Int,
    val skippedCount: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isWin: Boolean = correctCount >= totalQuestions / 2,
) {
    /** Accuracy percentage (0–100) derived from [correctCount] / [totalQuestions]. */
    val accuracy: Int get() = calculateAccuracy(correctCount, totalQuestions)
}
