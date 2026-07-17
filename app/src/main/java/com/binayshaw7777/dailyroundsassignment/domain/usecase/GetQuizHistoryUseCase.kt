package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import kotlinx.coroutines.flow.Flow

class GetQuizHistoryUseCase(private val repository: QuizResultRepository) {
    operator fun invoke(): Flow<List<QuizResult>> = repository.getAll()
}
