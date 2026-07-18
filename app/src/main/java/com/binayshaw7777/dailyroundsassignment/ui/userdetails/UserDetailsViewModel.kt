package com.binayshaw7777.dailyroundsassignment.ui.userdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the user-details (name entry) screen.
 *
 * Validates the entered name (minimum 2 characters), persists it along with
 * the onboarding-completion flag, and triggers navigation to the home screen.
 *
 * @property prefs [AppPreferences] used to store the user name and mark
 *   onboarding as completed.
 */
@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val prefs: AppPreferences) : ViewModel() {
    private val _uiState = MutableStateFlow(UserDetailsUiState())

    /** Observable UI state for the user-details screen. */
    val uiState = _uiState.asStateFlow()

    /**
     * Dispatches user events to update the name field or trigger submission.
     *
     * @param event The [UserDetailsUiEvent] to process.
     */
    fun onEvent(event: UserDetailsUiEvent) {
        when (event) {
            is UserDetailsUiEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.name) }
                Timber.d("UserDetails — name changed, length=%d", event.name.length)
            }
            UserDetailsUiEvent.Submit -> {
                val state = _uiState.value
                if (!state.isNameValid) {
                    Timber.w("UserDetails — submit ignored, name invalid: '%s'", state.name)
                    return
                }
                Timber.d("UserDetails — submitting name='%s'", state.name.trim())
                _uiState.update { it.copy(isSubmitting = true) }
                viewModelScope.launch {
                    prefs.setUserName(state.name.trim())
                    prefs.setHasCompletedOnboarding(true)
                    Timber.d("UserDetails — prefs saved, navigating to Home")
                    _uiState.update { it.copy(isSubmitting = false, navigateToHome = true) }
                }
            }
            UserDetailsUiEvent.OnNavigated -> {
                _uiState.update { it.copy(navigateToHome = false) }
            }
        }
    }
}
