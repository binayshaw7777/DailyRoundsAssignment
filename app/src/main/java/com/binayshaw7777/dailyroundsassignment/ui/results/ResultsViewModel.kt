package com.binayshaw7777.dailyroundsassignment.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the post-quiz results screen.
 *
 * Loads the most recent [QuizResult] from the database and exposes
 * it as [ResultsUiState] for the screen to render stat cards.
 *
 * @property repository Database repository for quiz results.
 */
@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val repository: QuizResultRepository,
) : ViewModel() {

    /** Observable UI state for the results screen. */
    val uiState: StateFlow<ResultsUiState> = repository.getAll()
        .map { results ->
            val result = results.firstOrNull()
            if (result != null) {
                ResultsUiState(
                    correctCount = result.correctCount,
                    totalQuestions = result.totalQuestions,
                    longestStreak = result.longestStreak,
                    skippedCount = result.skippedCount
                )
            } else {
                ResultsUiState()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResultsUiState()
        )
}

