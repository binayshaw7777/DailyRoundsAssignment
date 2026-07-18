package com.binayshaw7777.dailyroundsassignment.di

import com.binayshaw7777.dailyroundsassignment.data.repository.QuizResultRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt [Module] that binds repository interfaces to their concrete implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {



    /**
     * Binds [QuizResultRepositoryImpl] as the [QuizResultRepository].
     *
     * @param impl The Room-backed implementation.
     */
    @Binds
    @Singleton
    abstract fun bindQuizResultRepository(impl: QuizResultRepositoryImpl): QuizResultRepository
}
