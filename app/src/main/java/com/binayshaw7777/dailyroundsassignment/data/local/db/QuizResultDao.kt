package com.binayshaw7777.dailyroundsassignment.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QuizResultEntity)

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    @Query("DELETE FROM quiz_results")
    suspend fun clearAll()
}
