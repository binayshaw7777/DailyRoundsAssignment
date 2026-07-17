package com.binayshaw7777.dailyroundsassignment.ui.results

sealed interface ResultsUiEvent {
    data object RestartQuiz : ResultsUiEvent
}
