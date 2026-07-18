package com.binayshaw7777.dailyroundsassignment.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the onboarding pager screen.
 *
 * Manages page navigation and triggers the one-shot navigation event
 * to UserDetails when the user taps "Get Started" on the last page or
 * skips via the "Skip" button.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())

    /** Observable UI state for the onboarding screen. */
    val uiState = _uiState.asStateFlow()

    /**
     * Dispatches user events to advance pages or trigger navigation.
     *
     * @param event The [OnboardingUiEvent] to process.
     */
    fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            OnboardingUiEvent.Next -> {
                val state = _uiState.value
                if (state.isLastPage) {
                    Timber.d("Onboarding — last page, navigating to UserDetails")
                    _uiState.update { it.copy(destination = OnboardingDestination.UserDetails) }
                } else {
                    _uiState.update { it.copy(currentPage = it.currentPage + 1) }
                    Timber.d("Onboarding — page advanced to %d", _uiState.value.currentPage)
                }
            }
            OnboardingUiEvent.Skip -> {
                Timber.d("Onboarding — skipped, navigating to UserDetails")
                _uiState.update { it.copy(destination = OnboardingDestination.UserDetails) }
            }
            OnboardingUiEvent.OnNavigated -> _uiState.update { it.copy(destination = null) }
        }
    }
}
