package com.binayshaw7777.dailyroundsassignment.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a persisted quiz result row in the `quiz_results` table.
 *
 * This is the persistence-layer counterpart of [com.binayshaw7777.dailyroundsassignment.data.model.QuizResult].
 * The repository layer handles mapping between entity and domain model.
 *
 * @property id Auto-generated primary key.
 * @property correctCount Number of correctly answered questions.
 * @property totalQuestions Total questions in the session.
 * @property longestStreak Highest consecutive-correct streak.
 * @property skippedCount Questions skipped by the user.
 * @property timestamp Epoch millis when the quiz was completed.
 * @property isWin Whether the score qualifies as a win (>= 50%).
 */
@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val correctCount: Int,
    val totalQuestions: Int,
    val longestStreak: Int,
    val skippedCount: Int,
    val timestamp: Long,
    val isWin: Boolean,
)
