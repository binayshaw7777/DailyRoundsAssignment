package com.binayshaw7777.dailyroundsassignment.ui.quiz

/**
 * User-initiated events for the quiz screen, consumed by [QuizViewModel.onQuizEvent].
 */
sealed interface QuizUiEvent {
    /**
     * The user tapped an option.
     *
     * @property index Zero-based index of the selected option.
     */
    data class SelectOption(val index: Int) : QuizUiEvent

    /** The user swiped left or tapped the "Skip Question" button. */
    data object Skip : QuizUiEvent

    /** One-shot event consumed after navigation to the results screen has occurred. */
    data object OnNavigated : QuizUiEvent

    /** One-shot event consumed after a [QuizHaptic] has been played by the view layer. */
    data object OnHapticConsumed : QuizUiEvent
}
