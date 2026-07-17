package com.binayshaw7777.dailyroundsassignment.ui.quiz

import com.binayshaw7777.dailyroundsassignment.data.model.Question

sealed interface QuizUiState {
    data object Loading : QuizUiState

    data class Error(val message: String) : QuizUiState

    data class Content(
        val currentIndex: Int,
        val totalQuestions: Int,
        val currentQuestion: Question,
        val selectedOptionIndex: Int?,
        val isAnswered: Boolean,
        val currentStreak: Int,
        val longestStreak: Int,
        val correctCount: Int,
        val skippedCount: Int,
    ) : QuizUiState {
        val progress: Float get() = (currentIndex + 1).toFloat() / totalQuestions
        val streakActive: Boolean get() = currentStreak >= 3
    }
}
