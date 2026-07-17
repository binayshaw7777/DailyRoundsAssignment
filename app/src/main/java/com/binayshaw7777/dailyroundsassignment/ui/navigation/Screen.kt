package com.binayshaw7777.dailyroundsassignment.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object UserDetails : Screen("userdetails")
    data object Home : Screen("home")
    data object Quiz : Screen("quiz")
    data object Results : Screen("results")
}
