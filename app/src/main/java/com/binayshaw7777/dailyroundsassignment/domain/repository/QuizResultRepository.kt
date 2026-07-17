package com.binayshaw7777.dailyroundsassignment.domain.repository

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import kotlinx.coroutines.flow.Flow

interface QuizResultRepository {
    fun getAll(): Flow<List<QuizResult>>
    suspend fun save(result: QuizResult)
    suspend fun clearAll()
}
