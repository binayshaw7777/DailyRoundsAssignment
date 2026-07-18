package com.binayshaw7777.dailyroundsassignment.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for the splash screen.
 *
 * Holds the splash for a 2-second delay, reads the onboarding-completion
 * preference, and sets the navigation [SplashDestination].
 *
 * @property prefs User preferences used to determine whether onboarding has
 *   been completed.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(private val prefs: AppPreferences) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())

    /** Observable UI state for the splash screen. */
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2.seconds)
            val completed = prefs.hasCompletedOnboarding.first()
            Timber.d("Splash — hasCompletedOnboarding=%b, navigating to %s", completed, if (completed) "Home" else "Onboarding")
            _uiState.update {
                it.copy(destination = if (completed) SplashDestination.Home else SplashDestination.Onboarding)
            }
        }
    }

    /**
     * Dispatches user events.
     *
     * @param event The [SplashUiEvent] to process.
     */
    fun onEvent(event: SplashUiEvent) {
        when (event) {
            SplashUiEvent.OnNavigated -> _uiState.update { it.copy(destination = null) }
        }
    }
}
