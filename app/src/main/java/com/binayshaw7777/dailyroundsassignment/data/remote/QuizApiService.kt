package com.binayshaw7777.dailyroundsassignment.data.remote

import com.binayshaw7777.dailyroundsassignment.data.remote.dto.QuestionDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val QUESTIONS_URL =
    "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw"

/**
 * Network service responsible for fetching quiz questions from a remote gist endpoint.
 *
 * Uses Ktor's [HttpClient] with the Android engine and includes request/response
 * logging (routed through Timber) for debug builds. Responses are deserialized
 * with a lenient [Json] configuration that ignores unknown keys.
 *
 * This class is provided as a [Singleton] by Hilt and is consumed by
 * [com.binayshaw7777.dailyroundsassignment.data.repository.RemoteQuizRepositoryImpl].
 *
 * @see com.binayshaw7777.dailyroundsassignment.data.remote.dto.QuestionDto for the
 *   expected JSON shape.
 */
@Singleton
class QuizApiService @Inject constructor() {

    private val client = HttpClient(Android) {
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Timber.d(message)
                }
            }
            level = LogLevel.ALL
            filter { request ->
                request.url.host.contains("gist.githubusercontent.com")
            }
            sanitizeHeader { header -> header == "Authorization" }
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Fetches the full list of quiz questions from the remote gist.
     *
     * Executes on [Dispatchers.IO]. Includes detailed timing and response-size
     * logging for performance diagnostics.
     *
     * @return Deserialized list of [QuestionDto].
     * @throws Exception if the network call or deserialization fails.
     */
    suspend fun fetchQuestions(): List<QuestionDto> = withContext(Dispatchers.IO) {
        Timber.d("fetchQuestions() called — url=%s", QUESTIONS_URL)
        val startTime = System.currentTimeMillis()

        try {
            val response = client.get(QUESTIONS_URL) {
                header("Accept", "application/json")
                header("User-Agent", "DailyRoundsAssignment/1.0")
            }

            val elapsed = System.currentTimeMillis() - startTime
            val responseText = response.bodyAsText()

            Timber.d("HTTP response received in %dms, size=%d chars", elapsed, responseText.length)
            Timber.d("Response body (first 500 chars): %s", responseText.take(500))

            Timber.d("Deserializing JSON response...")
            val questions = json.decodeFromString<List<QuestionDto>>(responseText)
            Timber.d("Deserialized %d questions", questions.size)
            questions.forEachIndexed { index, q ->
                Timber.d("Question[%d]: id=%s, options=%d, correctIndex=%d", index, q.id, q.options.size, q.correctOptionIndex)
            }

            questions
        } catch (e: Exception) {
            val elapsed = System.currentTimeMillis() - startTime
            Timber.e(e, "fetchQuestions() failed after %dms", elapsed)
            throw e
        }
    }
}
