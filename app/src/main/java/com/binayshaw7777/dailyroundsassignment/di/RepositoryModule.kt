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

/**
 * Hilt [Module] that binds repository interfaces to their concrete implementations.
 *
 * Two [QuizRepository] bindings exist — qualified with [LocalQuiz] and [RemoteQuiz] —
 * so that [com.binayshaw7777.dailyroundsassignment.domain.usecase.LoadQuestionsUseCase]
 * can select the data source at runtime based on user preferences.
 *
 * @see LocalQuiz
 * @see RemoteQuiz
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds [LocalQuizRepositoryImpl] as the asset-based [QuizRepository].
     *
     * @param impl The local (JSON asset) implementation.
     */
    @Binds
    @Singleton
    @LocalQuiz
    abstract fun bindLocalQuizRepository(impl: LocalQuizRepositoryImpl): QuizRepository

    /**
     * Binds [RemoteQuizRepositoryImpl] as the network-based [QuizRepository].
     *
     * @param impl The remote (Ktor HTTP) implementation.
     */
    @Binds
    @Singleton
    @RemoteQuiz
    abstract fun bindRemoteQuizRepository(impl: RemoteQuizRepositoryImpl): QuizRepository

    /**
     * Binds [QuizResultRepositoryImpl] as the [QuizResultRepository].
     *
     * @param impl The Room-backed implementation.
     */
    @Binds
    @Singleton
    abstract fun bindQuizResultRepository(impl: QuizResultRepositoryImpl): QuizResultRepository
}
