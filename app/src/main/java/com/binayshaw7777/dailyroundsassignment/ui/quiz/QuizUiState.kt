package com.binayshaw7777.dailyroundsassignment.ui.quiz

import com.binayshaw7777.dailyroundsassignment.data.model.Question

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,  // null = not answered yet
    val isAnswered: Boolean = false,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val correctCount: Int = 0,
    val skippedCount: Int = 0,
    val isFinished: Boolean = false,
    val isLoading: Boolean = true
) {
    val currentQuestion: Question? get() = questions.getOrNull(currentIndex)
    val totalQuestions: Int get() = questions.size
    val progress: Float get() = if (totalQuestions == 0) 0f else (currentIndex + 1).toFloat() / totalQuestions
    val streakActive: Boolean get() = currentStreak >= 3
}
