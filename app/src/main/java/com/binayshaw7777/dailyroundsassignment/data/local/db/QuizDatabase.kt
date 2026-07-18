package com.binayshaw7777.dailyroundsassignment.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database that stores quiz result history.
 *
 * Currently contains a single entity ([QuizResultEntity]) at schema version 1.
 * Schema export is disabled (`exportSchema = false`) for simplicity.
 *
 * A thread-safe singleton is available via [getInstance].
 *
 * @see QuizResultEntity
 * @see QuizResultDao
 */
@Database(entities = [QuizResultEntity::class], version = 1, exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {

    /** Provides access to [QuizResultDao] for CRUD operations on quiz results. */
    abstract fun quizResultDao(): QuizResultDao

    companion object {
        @Volatile private var INSTANCE: QuizDatabase? = null

        /**
         * Returns the singleton [QuizDatabase] instance, creating it on first access.
         *
         * @param context Application context used to build the database.
         */
        fun getInstance(context: Context): QuizDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                ).build().also { INSTANCE = it }
            }
    }
}
