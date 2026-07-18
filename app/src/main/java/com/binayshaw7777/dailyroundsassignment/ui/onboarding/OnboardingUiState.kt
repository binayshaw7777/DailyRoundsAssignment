package com.binayshaw7777.dailyroundsassignment.ui.onboarding

/**
 * Possible navigation targets after the onboarding flow.
 */
sealed interface OnboardingDestination {
    /** Navigate to the user-name entry screen. */
    data object UserDetails : OnboardingDestination
}

/**
 * Immutable UI state for the onboarding pager screen.
 *
 * @property currentPage Zero-based index of the currently visible page.
 * @property totalPages Total number of onboarding pages (currently 3).
 * @property destination `null` while the user is swiping; set once navigation should occur.
 */
data class OnboardingUiState(
    val currentPage: Int = 0,
    val totalPages: Int = 3,
    val destination: OnboardingDestination? = null,
) {
    /** `true` when the user is on the last page. */
    val isLastPage: Boolean get() = currentPage == totalPages - 1
}
