package com.binayshaw7777.dailyroundsassignment.ui.splash

/**
 * User-initiated events for the splash screen, consumed by [SplashViewModel].
 */
sealed interface SplashUiEvent {
    /** One-shot event consumed after navigation from the splash screen has occurred. */
    data object OnNavigated : SplashUiEvent
}
