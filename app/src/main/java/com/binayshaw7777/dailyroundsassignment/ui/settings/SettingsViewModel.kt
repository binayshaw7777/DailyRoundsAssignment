package com.binayshaw7777.dailyroundsassignment.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = AppPreferences(application)

    val uiState = combine(prefs.useRemoteApi, prefs.isDarkTheme, prefs.userName) { useRemote, isDark, name ->
        SettingsUiState(useRemoteApi = useRemote, isDarkTheme = isDark, userName = name)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState())

    fun onEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.SetUseRemoteApi -> prefs.setUseRemoteApi(event.enabled)
                is SettingsUiEvent.SetDarkTheme -> prefs.setDarkTheme(event.enabled)
            }
        }
    }
}
