package com.binayshaw7777.dailyroundsassignment.data.remote

import com.binayshaw7777.dailyroundsassignment.data.remote.dto.QuestionDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val QUESTIONS_URL =
    "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw"

class QuizApiService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; isLenient = true })
        }
        install(Logging) { level = LogLevel.BODY }
    }

    suspend fun fetchQuestions(): List<QuestionDto> =
        client.get(QUESTIONS_URL).body()
}
