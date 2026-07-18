package com.binayshaw7777.dailyroundsassignment.data.repository

import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

/**
 * Local implementation of [QuizRepository] that reads questions from the
 * bundled `assets/questions_mock.json` file.
 *
 * Used as the default data source; the remote alternative is
 * [RemoteQuizRepositoryImpl]. The active source is chosen at runtime by
 * [com.binayshaw7777.dailyroundsassignment.domain.usecase.LoadQuestionsUseCase]
 * based on the user's "use remote API" preference.
 *
 * @param context Application context used to access assets.
 */
class LocalQuizRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) : QuizRepository {

    /**
     * Reads and deserializes questions from the local JSON asset.
     *
     * Runs on [Dispatchers.IO]. Cancellation exceptions are re-thrown to
     * preserve structured-concurrency contracts.
     *
     * @return [Result.success] with the parsed [Question] list, or
     *   [Result.failure] with the underlying exception.
     */
    override suspend fun loadQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        Timber.d("loadQuestions() — reading from LOCAL assets")
        val startTime = System.currentTimeMillis()

        runCatching {
            Timber.d("Opening assets/questions_mock.json...")
            val json = context.assets.open("questions_mock.json").bufferedReader().use { it.readText() }
            Timber.d("Asset read: %d chars", json.length)

            Timber.d("Deserializing JSON...")
            val questions = Json.decodeFromString<List<Question>>(json)
            Timber.d("Deserialized %d questions", questions.size)
            questions
        }.onSuccess {
            Timber.d("loadQuestions() SUCCESS: %d questions in %dms", it.size, System.currentTimeMillis() - startTime)
        }.onFailure {
            if (it is CancellationException) throw it
            Timber.e(it, "loadQuestions() FAILED after %dms", System.currentTimeMillis() - startTime)
        }
    }
}
