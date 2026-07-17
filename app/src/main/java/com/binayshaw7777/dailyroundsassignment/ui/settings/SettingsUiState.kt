package com.binayshaw7777.dailyroundsassignment.ui.settings

data class SettingsUiState(
    val useRemoteApi: Boolean = false,
    val isDarkTheme: Boolean = true,
    val appVersion: String = "1.0",
    val userName: String = "",
)
