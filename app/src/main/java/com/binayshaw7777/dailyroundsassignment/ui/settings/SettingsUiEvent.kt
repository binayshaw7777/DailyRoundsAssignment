package com.binayshaw7777.dailyroundsassignment.ui.settings

/**
 * User-initiated events for the settings screen, consumed by [SettingsViewModel].
 */
sealed interface SettingsUiEvent {
    /**
     * Toggles the remote API data source.
     *
     * @param enabled `true` to use the remote gist endpoint.
     */
    data class SetUseRemoteApi(val enabled: Boolean) : SettingsUiEvent

    /**
     * Toggles the app theme.
     *
     * @param enabled `true` for dark theme.
     */
    data class SetDarkTheme(val enabled: Boolean) : SettingsUiEvent

    /**
     * Toggles question shuffling.
     *
     * @param enabled `true` to randomize question order each session.
     */
    data class SetShuffleQuestions(val enabled: Boolean) : SettingsUiEvent
}
