package com.binayshaw7777.dailyroundsassignment.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.domain.usecase.GetLatestQuizResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    getLatestQuizResultUseCase: GetLatestQuizResultUseCase,
) : ViewModel() {

    val uiState: StateFlow<ResultsUiState> = getLatestQuizResultUseCase()
        .map { result ->
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
