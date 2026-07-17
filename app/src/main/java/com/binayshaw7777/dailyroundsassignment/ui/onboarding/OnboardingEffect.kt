package com.binayshaw7777.dailyroundsassignment.ui.onboarding

sealed interface OnboardingEffect {
    data object NavigateToUserDetails : OnboardingEffect
}
