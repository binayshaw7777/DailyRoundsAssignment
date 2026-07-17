package com.binayshaw7777.dailyroundsassignment.ui.onboarding

sealed interface OnboardingUiEvent {
    data object Next : OnboardingUiEvent
    data object Skip : OnboardingUiEvent
}
