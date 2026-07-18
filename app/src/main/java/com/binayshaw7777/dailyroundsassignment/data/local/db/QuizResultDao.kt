package com.binayshaw7777.dailyroundsassignment.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object for [QuizResultEntity] operations.
 *
 * Provides reactive ([Flow]-based) reads and suspend-based writes for the
 * `quiz_results` table.
 *
 * @see QuizResultEntity
 * @see QuizDatabase
 */
@Dao
interface QuizResultDao {

    /**
     * Inserts or replaces a quiz result row.
     *
     * @param entity The [QuizResultEntity] to persist.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QuizResultEntity)

    /**
     * Returns all quiz results ordered by timestamp descending (newest first).
     *
     * Emits a new list whenever the underlying table changes.
     *
     * @return [Flow] of [QuizResultEntity] list.
     */
    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    /**
     * Deletes every row from the `quiz_results` table.
     */
    @Query("DELETE FROM quiz_results")
    suspend fun clearAll()
}
