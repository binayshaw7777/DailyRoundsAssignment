package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult

data class LeaderboardUiState(
    val results: List<QuizResult> = emptyList(),
    val isLoading: Boolean = true,
) {
    val totalWins: Int get() = results.count { it.isWin }
    val totalLosses: Int get() = results.count { !it.isWin }
}
