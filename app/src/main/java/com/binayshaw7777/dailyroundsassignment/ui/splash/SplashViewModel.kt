package com.binayshaw7777.dailyroundsassignment.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = AppPreferences(application)
    private val _effects = Channel<SplashEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(2100)
            val completed = prefs.hasCompletedOnboarding.first()
            _effects.send(if (completed) SplashEffect.NavigateToHome else SplashEffect.NavigateToOnboarding)
        }
    }
}
