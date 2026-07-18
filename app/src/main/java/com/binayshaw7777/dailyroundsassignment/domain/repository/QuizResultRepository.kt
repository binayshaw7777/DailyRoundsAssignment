package com.binayshaw7777.dailyroundsassignment.domain.repository

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import kotlinx.coroutines.flow.Flow

/**
 * Contract for reading and writing quiz result history.
 *
 * Implemented by [com.binayshaw7777.dailyroundsassignment.data.repository.QuizResultRepositoryImpl]
 * which delegates to Room via [com.binayshaw7777.dailyroundsassignment.data.local.db.QuizResultDao].
 */
interface QuizResultRepository {

    /**
     * Returns all persisted quiz results as a reactive [Flow], ordered newest-first.
     *
     * @return [Flow] emitting the full list of [QuizResult] each time the underlying
     *   data changes.
     */
    fun getAll(): Flow<List<QuizResult>>

    /**
     * Persists a completed quiz result.
     *
     * @param result The [QuizResult] to save.
     */
    suspend fun save(result: QuizResult)

    /**
     * Deletes all persisted quiz results.
     */
    suspend fun clearAll()
}
