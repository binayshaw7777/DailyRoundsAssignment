package com.binayshaw7777.dailyroundsassignment.domain.repository

import com.binayshaw7777.dailyroundsassignment.data.model.Question

/**
 * Contract for loading quiz questions from a data source.
 *
 * Two implementations exist — one reads from local assets
 * ([com.binayshaw7777.dailyroundsassignment.data.repository.LocalQuizRepositoryImpl])
 * and the other from a remote API
 * ([com.binayshaw7777.dailyroundsassignment.data.repository.RemoteQuizRepositoryImpl]).
 * The active implementation is selected at runtime by
 * [com.binayshaw7777.dailyroundsassignment.domain.usecase.LoadQuestionsUseCase].
 */
interface QuizRepository {

    /**
     * Loads the full list of quiz questions.
     *
     * @return A [Result] wrapping the list of [Question] on success, or the
     *   encapsulated exception on failure.
     */
    suspend fun loadQuestions(): Result<List<Question>>
}
