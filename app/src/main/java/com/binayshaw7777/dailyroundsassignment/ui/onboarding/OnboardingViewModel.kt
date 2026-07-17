package com.binayshaw7777.dailyroundsassignment.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = Channel<OnboardingEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            OnboardingUiEvent.Next -> {
                val state = _uiState.value
                if (state.isLastPage) {
                    viewModelScope.launch { _effects.send(OnboardingEffect.NavigateToUserDetails) }
                } else {
                    _uiState.update { it.copy(currentPage = it.currentPage + 1) }
                }
            }
            OnboardingUiEvent.Skip -> viewModelScope.launch {
                _effects.send(OnboardingEffect.NavigateToUserDetails)
            }
        }
    }
}
