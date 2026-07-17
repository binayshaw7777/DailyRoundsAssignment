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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideQuizDatabase(@ApplicationContext context: Context): QuizDatabase =
        Room.databaseBuilder(context, QuizDatabase::class.java, "quiz_database").build()

    @Provides
    fun provideQuizResultDao(db: QuizDatabase): QuizResultDao = db.quizResultDao()
}
