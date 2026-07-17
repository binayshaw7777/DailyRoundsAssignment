package com.binayshaw7777.dailyroundsassignment.data.repository

import android.content.Context
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository(private val context: Context) {
    private val gson = Gson()

    suspend fun loadQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        runCatching {
            val json = context.assets.open("questions_mock.json")
                .bufferedReader()
                .use { it.readText() }
            gson.fromJson(json, Array<Question>::class.java).toList()
        }
    }
}
