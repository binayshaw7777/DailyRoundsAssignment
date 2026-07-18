package com.binayshaw7777.dailyroundsassignment.ui.settings

/**
 * Immutable UI state for the settings screen.
 *
 * @property useRemoteApi Whether questions are fetched from the remote API.
 * @property isDarkTheme Whether the app is in dark theme mode.
 * @property appVersion Displayed version string (e.g., "1.0").
 * @property userName The user's saved display name.
 * @property shuffleQuestions Whether question order is randomized each session.
 */
data class SettingsUiState(
    val useRemoteApi: Boolean = false,
    val isDarkTheme: Boolean = true,
    val appVersion: String = "1.0",
    val userName: String = "",
    val shuffleQuestions: Boolean = false,
)
