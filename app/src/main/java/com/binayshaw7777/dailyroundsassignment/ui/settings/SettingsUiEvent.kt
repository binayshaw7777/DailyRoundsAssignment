package com.binayshaw7777.dailyroundsassignment.ui.settings

sealed interface SettingsUiEvent {
    data class SetUseRemoteApi(val enabled: Boolean) : SettingsUiEvent
    data class SetDarkTheme(val enabled: Boolean) : SettingsUiEvent
}
