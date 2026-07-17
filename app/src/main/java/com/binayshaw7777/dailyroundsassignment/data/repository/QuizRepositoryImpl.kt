package com.binayshaw7777.dailyroundsassignment.data.repository

import android.util.Log
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val TAG = "LocalQuizRepo"

class LocalQuizRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) : QuizRepository {

    override suspend fun loadQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        Log.d(TAG, "loadQuestions() called - reading from LOCAL assets")
        val startTime = System.currentTimeMillis()

        runCatching {
            Log.d(TAG, "Opening assets/questions_mock.json...")
            val json = context.assets.open("questions_mock.json").bufferedReader().use { it.readText() }
            Log.d(TAG, "Asset read complete: ${json.length} chars")

            Log.d(TAG, "Deserializing JSON...")
            val questions = Json.decodeFromString<List<Question>>(json)
            Log.d(TAG, "Deserialization successful: ${questions.size} questions parsed")
            questions
        }.onSuccess {
            val elapsed = System.currentTimeMillis() - startTime
            Log.d(TAG, "loadQuestions() SUCCESS: ${it.size} questions loaded in ${elapsed}ms")
        }.onFailure {
            if (it is CancellationException) throw it
            val elapsed = System.currentTimeMillis() - startTime
            Log.e(TAG, "loadQuestions() FAILED after ${elapsed}ms: ${it.message}", it)
        }
    }
}
