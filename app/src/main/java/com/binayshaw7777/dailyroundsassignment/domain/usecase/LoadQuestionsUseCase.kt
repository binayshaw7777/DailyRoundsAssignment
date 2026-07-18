package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.di.LocalQuiz
import com.binayshaw7777.dailyroundsassignment.di.RemoteQuiz
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

/**
 * Single-responsibility use case that loads, validates, and optionally shuffles quiz questions.
 *
 * Selects the data source (local asset vs. remote API) based on the user's
 * `useRemoteApi` preference, then filters out any malformed questions:
 * - Questions with fewer than 4 options
 * - Questions whose [Question.correctOptionIndex] is out of bounds
 * - Questions with blank text
 *
 * If the user has enabled "shuffle questions" in preferences, the filtered list
 * is randomly reordered before being returned.
 *
 * ### Usage
 * ```kotlin
 * val result = loadQuestionsUseCase()
 * result.onSuccess { questions -> /* start quiz */ }
 * result.onFailure { error -> /* show error */ }
 * ```
 *
 * @property localRepo Asset-based repository, qualified with [LocalQuiz].
 * @property remoteRepo Network-based repository, qualified with [RemoteQuiz].
 * @property prefs User preferences used to determine the active data source and shuffle setting.
 */
class LoadQuestionsUseCase @Inject constructor(
    @LocalQuiz private val localRepo: QuizRepository,
    @RemoteQuiz private val remoteRepo: QuizRepository,
    private val prefs: AppPreferences,
) {
    /**
     * Loads questions from the appropriate source, filters invalid entries, and
     * optionally shuffles the result.
     *
     * @return [Result.success] with a validated list of [Question], or
     *   [Result.failure] with the underlying error.
     */
    suspend operator fun invoke(): Result<List<Question>> {
        val startTime = System.currentTimeMillis()
        val useRemote = prefs.useRemoteApi.first()
        val shuffle = prefs.shuffleQuestions.first()
        Timber.d("LoadQuestionsUseCase — source=%s, shuffle=%b", if (useRemote) "REMOTE" else "LOCAL", shuffle)

        val repo = if (useRemote) remoteRepo else localRepo
        val result = repo.loadQuestions()

        result.onSuccess { questions ->
            val filtered = questions.filter { q ->
                q.options.size == 4 &&
                    q.correctOptionIndex in q.options.indices &&
                    q.question.isNotBlank()
            }
            val final = if (shuffle) filtered.shuffled() else filtered
            val elapsed = System.currentTimeMillis() - startTime
            Timber.d("Questions: raw=%d, filtered=%d, shuffled=%b in %dms", questions.size, filtered.size, shuffle, elapsed)
            return Result.success(final)
        }

        result.onFailure {
            Timber.e(it, "LoadQuestionsUseCase FAILED after %dms", System.currentTimeMillis() - startTime)
        }

        return result
    }
}
