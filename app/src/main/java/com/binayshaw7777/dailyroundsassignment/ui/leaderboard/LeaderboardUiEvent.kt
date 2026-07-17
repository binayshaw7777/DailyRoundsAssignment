package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

sealed interface LeaderboardUiEvent {
    data object ClearHistory : LeaderboardUiEvent
}
