package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import javax.inject.Inject

class SaveQuizResultUseCase @Inject constructor(private val repository: QuizResultRepository) {
    suspend operator fun invoke(result: QuizResult) = repository.save(result)
}
