package com.binayshaw7777.dailyroundsassignment.domain.usecase

import android.util.Log
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.di.LocalQuiz
import com.binayshaw7777.dailyroundsassignment.di.RemoteQuiz
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val TAG = "LoadQuestionsUseCase"

class LoadQuestionsUseCase @Inject constructor(
    @LocalQuiz private val localRepo: QuizRepository,
    @RemoteQuiz private val remoteRepo: QuizRepository,
    private val prefs: AppPreferences,
) {
    suspend operator fun invoke(): Result<List<Question>> {
        Log.d(TAG, "invoke() called")
        val startTime = System.currentTimeMillis()

        val useRemote = prefs.useRemoteApi.first()
        Log.d(TAG, "Data source preference: useRemoteApi=$useRemote")

        val repo = if (useRemote) {
            Log.d(TAG, "Selected REMOTE repository")
            remoteRepo
        } else {
            Log.d(TAG, "Selected LOCAL repository")
            localRepo
        }

        Log.d(TAG, "Loading questions from ${if (useRemote) "remote" else "local"} source...")
        val result = repo.loadQuestions()

        result.onSuccess { questions ->
            Log.d(TAG, "Raw questions loaded: ${questions.size}")
            val filtered = questions.filter { q ->
                q.options.size == 4 &&
                    q.correctOptionIndex in q.options.indices &&
                    q.question.isNotBlank()
            }
            val elapsed = System.currentTimeMillis() - startTime
            Log.d(TAG, "Filtered questions: ${filtered.size} (removed ${questions.size - filtered.size} invalid)")
            Log.d(TAG, "invoke() completed in ${elapsed}ms")
            return Result.success(filtered)
        }

        result.onFailure {
            val elapsed = System.currentTimeMillis() - startTime
            Log.e(TAG, "invoke() FAILED after ${elapsed}ms: ${it.message}", it)
        }

        return result
    }
}
