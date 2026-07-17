package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.domain.usecase.ClearQuizHistoryUseCase
import com.binayshaw7777.dailyroundsassignment.domain.usecase.GetQuizHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getHistory: GetQuizHistoryUseCase,
    private val clearHistory: ClearQuizHistoryUseCase,
) : ViewModel() {

    val uiState = getHistory().map { results ->
        LeaderboardUiState(results = results, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, LeaderboardUiState())

    fun onEvent(event: LeaderboardUiEvent) {
        when (event) {
            LeaderboardUiEvent.ClearHistory -> viewModelScope.launch { clearHistory() }
        }
    }
}
