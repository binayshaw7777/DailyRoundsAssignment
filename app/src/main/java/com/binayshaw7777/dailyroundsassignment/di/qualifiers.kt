package com.binayshaw7777.dailyroundsassignment.di

import javax.inject.Qualifier

/**
 * Hilt qualifier that distinguishes the **local** (asset-based) quiz repository
 * from the remote one.
 *
 * Used in conjunction with [com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository]
 * so that Hilt can inject the correct implementation based on the qualifier.
 *
 * @see RemoteQuiz
 * @see com.binayshaw7777.dailyroundsassignment.di.RepositoryModule.bindLocalQuizRepository
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalQuiz

/**
 * Hilt qualifier that distinguishes the **remote** (network-based) quiz repository
 * from the local one.
 *
 * Used in conjunction with [com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository]
 * so that Hilt can inject the correct implementation based on the qualifier.
 *
 * @see LocalQuiz
 * @see com.binayshaw7777.dailyroundsassignment.di.RepositoryModule.bindRemoteQuizRepository
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteQuiz
