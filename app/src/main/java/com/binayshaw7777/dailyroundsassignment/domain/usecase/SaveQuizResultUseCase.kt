package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository

class SaveQuizResultUseCase(private val repository: QuizResultRepository) {
    suspend operator fun invoke(result: QuizResult) = repository.save(result)
}
