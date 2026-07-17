package com.binayshaw7777.dailyroundsassignment.data.repository

import android.util.Log
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.remote.QuizApiService
import com.binayshaw7777.dailyroundsassignment.data.remote.dto.toDomain
import javax.inject.Inject

private const val TAG = "RemoteQuizRepo"

class RemoteQuizRepositoryImpl @Inject constructor(private val apiService: QuizApiService) : QuizRepository {

    override suspend fun loadQuestions(): Result<List<Question>> {
        Log.d(TAG, "loadQuestions() called - fetching from REMOTE data source")
        val startTime = System.currentTimeMillis()

        return runCatching {
            Log.d(TAG, "Delegating to QuizApiService.fetchQuestions()...")
            val dtos = apiService.fetchQuestions()
            Log.d(TAG, "Received ${dtos.size} DTOs from API")

            Log.d(TAG, "Mapping DTOs to domain models...")
            val domainModels = dtos.map { it.toDomain() }
            Log.d(TAG, "Mapping complete: ${domainModels.size} domain models created")

            domainModels
        }.onSuccess {
            val elapsed = System.currentTimeMillis() - startTime
            Log.d(TAG, "loadQuestions() SUCCESS: ${it.size} questions loaded in ${elapsed}ms")
        }.onFailure {
            val elapsed = System.currentTimeMillis() - startTime
            Log.e(TAG, "loadQuestions() FAILED after ${elapsed}ms: ${it.message}", it)
        }
    }
}
