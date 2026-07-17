package com.binayshaw7777.dailyroundsassignment.data.repository

import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import com.binayshaw7777.dailyroundsassignment.data.local.db.QuizResultDao
import com.binayshaw7777.dailyroundsassignment.data.local.db.QuizResultEntity
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuizResultRepositoryImpl(private val dao: QuizResultDao) : QuizResultRepository {

    override fun getAll(): Flow<List<QuizResult>> = dao.getAllResults().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun save(result: QuizResult) = dao.insert(result.toEntity())

    override suspend fun clearAll() = dao.clearAll()

    private fun QuizResultEntity.toDomain() = QuizResult(
        id = id,
        correctCount = correctCount,
        totalQuestions = totalQuestions,
        longestStreak = longestStreak,
        skippedCount = skippedCount,
        timestamp = timestamp,
        isWin = isWin,
    )

    private fun QuizResult.toEntity() = QuizResultEntity(
        correctCount = correctCount,
        totalQuestions = totalQuestions,
        longestStreak = longestStreak,
        skippedCount = skippedCount,
        timestamp = timestamp,
        isWin = isWin,
    )
}
