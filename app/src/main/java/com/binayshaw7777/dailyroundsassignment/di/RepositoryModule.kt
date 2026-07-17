package com.binayshaw7777.dailyroundsassignment.di

import com.binayshaw7777.dailyroundsassignment.data.repository.LocalQuizRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.data.repository.QuizResultRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.data.repository.RemoteQuizRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    @LocalQuiz
    abstract fun bindLocalQuizRepository(impl: LocalQuizRepositoryImpl): QuizRepository

    @Binds
    @Singleton
    @RemoteQuiz
    abstract fun bindRemoteQuizRepository(impl: RemoteQuizRepositoryImpl): QuizRepository

    @Binds
    @Singleton
    abstract fun bindQuizResultRepository(impl: QuizResultRepositoryImpl): QuizResultRepository
}
