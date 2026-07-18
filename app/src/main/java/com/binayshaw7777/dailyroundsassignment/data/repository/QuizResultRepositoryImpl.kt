package com.binayshaw7777.dailyroundsassignment.data.repository

import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import com.binayshaw7777.dailyroundsassignment.data.local.db.QuizResultDao
import com.binayshaw7777.dailyroundsassignment.data.local.db.QuizResultEntity
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * Room-backed implementation of [QuizResultRepository].
 *
 * Handles bidirectional mapping between the persistence entity
 * ([QuizResultEntity]) and the domain model ([QuizResult]).
 *
 * @param dao The Room DAO used for all database operations.
 */
class QuizResultRepositoryImpl @Inject constructor(private val dao: QuizResultDao) : QuizResultRepository {

    /**
     * Emits all quiz results from the database, newest first.
     *
     * Each emission maps raw [QuizResultEntity] rows to domain [QuizResult] objects.
     */
    override fun getAll(): Flow<List<QuizResult>> = dao.getAllResults().map { entities ->
        Timber.d("DB getAll() returned %d results", entities.size)
        entities.map { it.toDomain() }
    }

    /**
     * Inserts a quiz result into the database.
     *
     * @param result The [QuizResult] to persist.
     */
    override suspend fun save(result: QuizResult) {
        Timber.d("DB save() — correct=%d/%d, streak=%d, win=%b", result.correctCount, result.totalQuestions, result.longestStreak, result.isWin)
        dao.insert(result.toEntity())
        Timber.d("DB save() complete")
    }

    /**
     * Clears all rows from the quiz_results table.
     */
    override suspend fun clearAll() {
        Timber.d("DB clearAll() called")
        dao.clearAll()
        Timber.d("DB clearAll() complete")
    }

    /**
     * Deletes an individual quiz result from the database by ID.
     */
    override suspend fun deleteById(id: Long) {
        Timber.d("DB deleteById() called with id=%d", id)
        dao.deleteById(id)
        Timber.d("DB deleteById() complete")
    }

    /**
     * Returns the maximum streak achieved across all quiz sessions, defaulting to 0.
     */
    override fun getMaxStreak(): Flow<Int> = dao.getMaxStreak().map { it ?: 0 }

    /** Maps a [QuizResultEntity] to its domain [QuizResult] equivalent. */
    private fun QuizResultEntity.toDomain() = QuizResult(
        id = id,
        correctCount = correctCount,
        totalQuestions = totalQuestions,
        longestStreak = longestStreak,
        skippedCount = skippedCount,
        timestamp = timestamp,
        isWin = isWin,
    )

    /** Maps a domain [QuizResult] to its persistence [QuizResultEntity] equivalent. */
    private fun QuizResult.toEntity() = QuizResultEntity(
        correctCount = correctCount,
        totalQuestions = totalQuestions,
        longestStreak = longestStreak,
        skippedCount = skippedCount,
        timestamp = timestamp,
        isWin = isWin,
    )
}
