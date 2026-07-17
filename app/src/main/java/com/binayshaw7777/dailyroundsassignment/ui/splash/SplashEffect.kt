package com.binayshaw7777.dailyroundsassignment.ui.splash

sealed interface SplashEffect {
    data object NavigateToOnboarding : SplashEffect
    data object NavigateToHome : SplashEffect
}
