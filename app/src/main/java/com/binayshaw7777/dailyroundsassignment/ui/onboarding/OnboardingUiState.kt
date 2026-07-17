package com.binayshaw7777.dailyroundsassignment.ui.onboarding

data class OnboardingUiState(
    val currentPage: Int = 0,
    val totalPages: Int = 3,
) {
    val isLastPage: Boolean get() = currentPage == totalPages - 1
}
