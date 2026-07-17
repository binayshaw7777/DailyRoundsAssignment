package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository

class ClearQuizHistoryUseCase(private val repository: QuizResultRepository) {
    suspend operator fun invoke() = repository.clearAll()
}
