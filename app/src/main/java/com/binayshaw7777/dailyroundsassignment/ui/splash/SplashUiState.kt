package com.binayshaw7777.dailyroundsassignment.ui.splash

/**
 * Possible navigation targets after the splash screen.
 */
sealed interface SplashDestination {
    /** User has completed onboarding — go to the home screen. */
    data object Home : SplashDestination
    /** User has NOT completed onboarding — go to the onboarding flow. */
    data object Onboarding : SplashDestination
}

/**
 * Immutable UI state for the splash screen.
 *
 * @property destination `null` while the timer is running; set to a
 *   [SplashDestination] once the 2-second delay completes and the preference
 *   has been read.
 */
data class SplashUiState(
    val destination: SplashDestination? = null
)
