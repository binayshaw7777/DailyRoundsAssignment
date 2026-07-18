package com.binayshaw7777.dailyroundsassignment.ui.onboarding

/**
 * User-initiated events for the onboarding screen, consumed by [OnboardingViewModel].
 */
sealed interface OnboardingUiEvent {
    /** Advance to the next page, or navigate to UserDetails if on the last page. */
    data object Next : OnboardingUiEvent
    /** Skip the remaining pages and navigate directly to UserDetails. */
    data object Skip : OnboardingUiEvent
    /** One-shot event consumed after navigation has occurred. */
    data object OnNavigated : OnboardingUiEvent
}
