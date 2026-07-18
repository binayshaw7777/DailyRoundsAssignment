package com.binayshaw7777.dailyroundsassignment.di

import android.content.Context
import androidx.room.Room
import com.binayshaw7777.dailyroundsassignment.data.local.db.QuizDatabase
import com.binayshaw7777.dailyroundsassignment.data.local.db.QuizResultDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt [Module] that provides the Room database and DAO instances.
 *
 * Installed in [SingletonComponent] so the database lives for the app's entire lifecycle.
 *
 * @see QuizDatabase
 * @see QuizResultDao
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton [QuizDatabase].
     *
     * @param context Application-scoped context provided by Hilt.
     */
    @Provides
    @Singleton
    fun provideQuizDatabase(@ApplicationContext context: Context): QuizDatabase =
        Room.databaseBuilder(context, QuizDatabase::class.java, "quiz_database").build()

    /**
     * Provides a [QuizResultDao] from the database instance.
     *
     * @param db The Room database backing the DAO.
     */
    @Provides
    fun provideQuizResultDao(db: QuizDatabase): QuizResultDao = db.quizResultDao()
}
