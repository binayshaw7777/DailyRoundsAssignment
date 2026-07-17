package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLatestQuizResultUseCase @Inject constructor(private val repository: QuizResultRepository) {
    operator fun invoke(): Flow<QuizResult?> {
        return repository.getAll().map { it.firstOrNull() }
    }
}
