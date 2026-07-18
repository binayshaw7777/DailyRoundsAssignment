package com.binayshaw7777.dailyroundsassignment.ui.quiz

import com.binayshaw7777.dailyroundsassignment.data.model.Question

/**
 * Haptic feedback types emitted during quiz interactions.
 *
 * @see HapticEvent
 */
sealed interface QuizHaptic {
    /** Triggered when the user selects the correct answer. */
    data object Success : QuizHaptic
    /** Triggered when the user selects the wrong answer. */
    data object Failure : QuizHaptic
    /** Triggered when the user skips a question. */
    data object Skip : QuizHaptic
}

/**
 * Wraps a [QuizHaptic] type with a unique [nonce] so that repeated identical
 * haptic events (e.g., two consecutive correct answers) are still observed
 * by [LaunchedEffect].
 *
 * @property nonce Monotonically increasing counter.
 * @property type The haptic pattern to play.
 */
data class HapticEvent(val nonce: Int, val type: QuizHaptic)

/**
 * Immutable UI state for the quiz screen, modeled as a sealed interface to
 * represent Loading / Error / Content as distinct states.
 */
sealed interface QuizUiState {

    /** Questions are being loaded from the data source. */
    data object Loading : QuizUiState

    /**
     * An error occurred while loading questions.
     *
     * @property message Human-readable error description.
     */
    data class Error(val message: String) : QuizUiState

    /**
     * The quiz is actively in progress.
     *
     * @property currentIndex Zero-based index of the current question.
     * @property totalQuestions Total number of questions in the session.
     * @property currentQuestion The [Question] being displayed.
     * @property selectedOptionIndex Index of the option the user selected, or `null`.
     * @property isAnswered `true` after the user selects an option or skips.
     * @property currentStreak Consecutive correct answers in the current run.
     * @property longestStreak Highest streak achieved this session.
     * @property correctCount Running total of correct answers.
     * @property skippedCount Running total of skipped questions.
     * @property pendingHaptic A [HapticEvent] waiting to be consumed by the view layer.
     * @property navigateToResults One-shot flag to trigger navigation to the results screen.
     */
    data class Content(
        val currentIndex: Int,
        val totalQuestions: Int,
        val currentQuestion: Question,
        val selectedOptionIndex: Int?,
        val isAnswered: Boolean,
        val currentStreak: Int,
        val longestStreak: Int,
        val correctCount: Int,
        val skippedCount: Int,
        val pendingHaptic: HapticEvent? = null,
        val navigateToResults: Boolean = false,
    ) : QuizUiState {
        /** Linear progress from 0f to 1f based on [currentIndex] / [totalQuestions]. */
        val progress: Float get() = (currentIndex + 1).toFloat() / totalQuestions

        /** `true` when the current streak is 3 or higher (triggers glow effects). */
        val streakActive: Boolean get() = currentStreak >= 3
    }
}
