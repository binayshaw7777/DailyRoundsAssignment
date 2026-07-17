package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import javax.inject.Inject

class ClearQuizHistoryUseCase @Inject constructor(private val repository: QuizResultRepository) {
    suspend operator fun invoke() = repository.clearAll()
}
