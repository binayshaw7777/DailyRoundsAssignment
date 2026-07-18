package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the leaderboard screen.
 *
 * Observes the full quiz history via [QuizResultRepository] and exposes it
 * as [LeaderboardUiState]. Also handles the "clear history" action.
 *
 * @property repository Provides the reactive database access to quiz results.
 */
@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: QuizResultRepository,
) : ViewModel() {

    /** Observable UI state for the leaderboard screen. */
    val uiState: StateFlow<LeaderboardUiState> = combine(
        repository.getAll(),
        repository.getMaxStreak()
    ) { results, maxStreak ->
        LeaderboardUiState(
            results = results,
            bestStreak = maxStreak,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LeaderboardUiState(isLoading = true)
    )

    /**
     * Dispatches user events to the appropriate handler.
     *
     * @param event The [LeaderboardUiEvent] to process.
     */
    fun onEvent(event: LeaderboardUiEvent) {
        when (event) {
            LeaderboardUiEvent.ClearHistory -> viewModelScope.launch { repository.clearAll() }
            is LeaderboardUiEvent.DeleteResult -> viewModelScope.launch { repository.deleteById(event.id) }
        }
    }
}
