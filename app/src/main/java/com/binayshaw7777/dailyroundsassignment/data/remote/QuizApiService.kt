package com.binayshaw7777.dailyroundsassignment.data.remote

import android.util.Log
import com.binayshaw7777.dailyroundsassignment.data.remote.dto.QuestionDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "QuizApiService"
private const val QUESTIONS_URL =
    "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw"

@Singleton
class QuizApiService @Inject constructor() {

    private val client = HttpClient(Android) {
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Log.d(TAG, message)
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

    suspend fun fetchQuestions(): List<QuestionDto> {
        Log.d(TAG, "fetchQuestions() called")
        Log.d(TAG, "Request URL: $QUESTIONS_URL")
        Log.d(TAG, "HTTP Method: GET")

        val startTime = System.currentTimeMillis()

        return try {
            Log.d(TAG, "Sending HTTP request...")
            val response = client.get(QUESTIONS_URL) {
                header("Accept", "application/json")
                header("User-Agent", "DailyRoundsAssignment/1.0")
            }

            val elapsed = System.currentTimeMillis() - startTime
            val responseText = response.bodyAsText()

            Log.d(TAG, "HTTP Response received")
            Log.d(TAG, "Response Time: ${elapsed}ms")
            Log.d(TAG, "Response Size: ${responseText.length} chars")
            Log.d(TAG, "Response body (first 500 chars): ${responseText.take(500)}")

            Log.d(TAG, "Deserializing JSON response...")
            val questions = json.decodeFromString<List<QuestionDto>>(responseText)
            Log.d(TAG, "Deserialization successful: ${questions.size} questions parsed")
            questions.forEachIndexed { index, q ->
                Log.d(TAG, "Question[$index]: id=${q.id}, options=${q.options.size}, correctIndex=${q.correctOptionIndex}")
            }

            Log.d(TAG, "fetchQuestions() completed successfully in ${elapsed}ms")
            questions
        } catch (e: Exception) {
            val elapsed = System.currentTimeMillis() - startTime
            Log.e(TAG, "fetchQuestions() failed after ${elapsed}ms: ${e.message}", e)
            throw e
        }
    }
}
