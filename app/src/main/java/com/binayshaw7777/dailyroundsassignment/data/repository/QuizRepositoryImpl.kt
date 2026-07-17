package com.binayshaw7777.dailyroundsassignment.data.repository

import android.content.Context
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class LocalQuizRepositoryImpl(private val context: Context) : QuizRepository {
    private val gson = Gson()
    override suspend fun loadQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        runCatching {
            val json = context.assets.open("questions_mock.json").bufferedReader().use { it.readText() }
            gson.fromJson(json, Array<Question>::class.java).toList()
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }
}
