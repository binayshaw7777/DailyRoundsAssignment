package com.binayshaw7777.dailyroundsassignment.ui.results

/**
 * User-initiated events for the results screen, consumed by [ResultsViewModel].
 */
sealed interface ResultsUiEvent {
    /** The user taps "Play Again" or the close button to return to the home screen. */
    data object RestartQuiz : ResultsUiEvent
}
