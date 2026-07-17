package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.db.QuizDatabase
import com.binayshaw7777.dailyroundsassignment.data.repository.QuizResultRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.domain.usecase.ClearQuizHistoryUseCase
import com.binayshaw7777.dailyroundsassignment.domain.usecase.GetQuizHistoryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = QuizDatabase.getInstance(application).quizResultDao()
    private val repo = QuizResultRepositoryImpl(dao)
    private val getHistory = GetQuizHistoryUseCase(repo)
    private val clearHistory = ClearQuizHistoryUseCase(repo)

    val uiState = getHistory().map { results ->
        LeaderboardUiState(results = results, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, LeaderboardUiState())

    fun onEvent(event: LeaderboardUiEvent) {
        when (event) {
            LeaderboardUiEvent.ClearHistory -> viewModelScope.launch { clearHistory() }
        }
    }
}
