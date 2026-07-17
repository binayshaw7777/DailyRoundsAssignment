package com.binayshaw7777.dailyroundsassignment.ui.userdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val prefs: AppPreferences) : ViewModel() {
    private val _uiState = MutableStateFlow(UserDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<UserDetailsEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: UserDetailsUiEvent) {
        when (event) {
            is UserDetailsUiEvent.NameChanged -> _uiState.update { it.copy(name = event.name) }
            UserDetailsUiEvent.Submit -> {
                val state = _uiState.value
                if (!state.isNameValid) return
                _uiState.update { it.copy(isSubmitting = true) }
                viewModelScope.launch {
                    prefs.setUserName(state.name.trim())
                    prefs.setHasCompletedOnboarding(true)
                    _effects.send(UserDetailsEffect.NavigateToHome)
                }
            }
        }
    }
}
