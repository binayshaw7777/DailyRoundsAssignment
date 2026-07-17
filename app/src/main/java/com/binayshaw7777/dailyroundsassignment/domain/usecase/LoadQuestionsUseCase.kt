package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import kotlinx.coroutines.flow.first

class LoadQuestionsUseCase(
    private val localRepo: QuizRepository,
    private val remoteRepo: QuizRepository,
    private val prefs: AppPreferences,
) {
    suspend operator fun invoke(): Result<List<Question>> {
        val useRemote = prefs.useRemoteApi.first()
        val repo = if (useRemote) remoteRepo else localRepo
        return repo.loadQuestions().map { questions ->
            questions.filter { q ->
                q.options.size == 4 &&
                    q.correctOptionIndex in q.options.indices &&
                    q.question.isNotBlank()
            }
        }
    }
}
