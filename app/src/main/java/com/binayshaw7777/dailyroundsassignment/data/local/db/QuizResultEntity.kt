package com.binayshaw7777.dailyroundsassignment.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

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
