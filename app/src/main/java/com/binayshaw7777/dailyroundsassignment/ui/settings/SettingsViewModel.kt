package com.binayshaw7777.dailyroundsassignment.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the settings screen.
 *
 * Combines preference flows ([AppPreferences.useRemoteApi],
 * [AppPreferences.isDarkTheme], [AppPreferences.userName],
 * [AppPreferences.shuffleQuestions]) into a single [SettingsUiState] and
 * forwards toggle events back to [AppPreferences].
 *
 * This ViewModel is shared across the navigation graph so that theme changes
 * take effect immediately on all screens.
 *
 * @property prefs The [AppPreferences] DataStore wrapper.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: AppPreferences) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        prefs.useRemoteApi,
        prefs.isDarkTheme,
        prefs.userName,
        prefs.shuffleQuestions
    ) { useRemote, isDark, name, shuffle ->
        Timber.d("SettingsUiState updated — useRemoteApi=%b, isDarkTheme=%b, shuffleQuestions=%b", useRemote, isDark, shuffle)
        SettingsUiState(
            useRemoteApi = useRemote,
            isDarkTheme = isDark,
            userName = name,
            shuffleQuestions = shuffle
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    /**
     * Dispatches user events to the appropriate preference setter.
     *
     * @param event The [SettingsUiEvent] to process.
     */
    fun onEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.SetUseRemoteApi -> {
                    Timber.d("Toggle remote API: %b", event.enabled)
                    prefs.setUseRemoteApi(event.enabled)
                }
                is SettingsUiEvent.SetDarkTheme -> {
                    Timber.d("Toggle dark theme: %b", event.enabled)
                    prefs.setDarkTheme(event.enabled)
                }
                is SettingsUiEvent.SetShuffleQuestions -> {
                    Timber.d("Toggle shuffle questions: %b", event.enabled)
                    prefs.setShuffleQuestions(event.enabled)
                }
            }
        }
    }
}
