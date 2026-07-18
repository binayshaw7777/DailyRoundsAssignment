package com.binayshaw7777.dailyroundsassignment.ui.navigation

/**
 * Sealed class defining all navigation destinations in the app.
 *
 * Each subclass maps to a unique route string used by Jetpack Navigation Compose.
 *
 * @property route The Navigation Compose route identifier.
 */
sealed class Screen(val route: String) {
    /** Splash / launch screen shown for ~2 seconds on cold start. */
    data object Splash : Screen("splash")
    /** First-time onboarding pager (3 pages). */
    data object Onboarding : Screen("onboarding")
    /** User name entry shown after onboarding. */
    data object UserDetails : Screen("userdetails")
    /** Main home screen with bottom tab bar (Quiz, Leaderboard, Settings). */
    data object Home : Screen("home")
    /** Active quiz screen with questions and options. */
    data object Quiz : Screen("quiz")
    /** Post-quiz results summary screen. */
    data object Results : Screen("results")
}
