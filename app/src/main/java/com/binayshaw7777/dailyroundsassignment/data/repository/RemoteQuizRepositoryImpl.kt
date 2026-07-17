package com.binayshaw7777.dailyroundsassignment.data.repository

import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.remote.QuizApiService
import com.binayshaw7777.dailyroundsassignment.data.remote.dto.toDomain

class RemoteQuizRepositoryImpl(private val apiService: QuizApiService) : QuizRepository {
    override suspend fun loadQuestions(): Result<List<Question>> = runCatching {
        apiService.fetchQuestions().map { it.toDomain() }
    }
}
