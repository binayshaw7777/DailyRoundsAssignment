package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult

/**
 * Immutable UI state for the leaderboard screen.
 *
 * @property results Full list of quiz results ordered newest-first.
 * @property isLoading `true` while the initial database read is in progress.
 */
data class LeaderboardUiState(
    val results: List<QuizResult> = emptyList(),
    val isLoading: Boolean = true,
) {
    /** Total number of quiz results where [QuizResult.isWin] is `true`. */
    val totalWins: Int get() = results.count { it.isWin }

    /** Total number of quiz results where [QuizResult.isWin] is `false`. */
    val totalLosses: Int get() = results.count { !it.isWin }
}
