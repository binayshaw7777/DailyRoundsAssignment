package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

/**
 * User-initiated events for the leaderboard screen, consumed by [LeaderboardViewModel].
 */
sealed interface LeaderboardUiEvent {
    /** The user confirmed clearing all quiz history via the dialog. */
    data object ClearHistory : LeaderboardUiEvent

    /** The user requested to delete a specific quiz result. */
    data class DeleteResult(val id: Long) : LeaderboardUiEvent
}
