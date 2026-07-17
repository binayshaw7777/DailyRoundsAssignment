package com.binayshaw7777.dailyroundsassignment.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SettingsViewModel"

@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: AppPreferences) : ViewModel() {

    val uiState = combine(prefs.useRemoteApi, prefs.isDarkTheme, prefs.userName) { useRemote, isDark, name ->
        Log.d(TAG, "UI state updated: useRemoteApi=$useRemote, isDarkTheme=$isDark")
        SettingsUiState(useRemoteApi = useRemote, isDarkTheme = isDark, userName = name)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState())

    fun onEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.SetUseRemoteApi -> {
                    Log.d(TAG, "Toggle remote API: ${event.enabled}")
                    prefs.setUseRemoteApi(event.enabled)
                }
                is SettingsUiEvent.SetDarkTheme -> {
                    Log.d(TAG, "Toggle dark theme: ${event.enabled}")
                    prefs.setDarkTheme(event.enabled)
                }
            }
        }
    }
}
