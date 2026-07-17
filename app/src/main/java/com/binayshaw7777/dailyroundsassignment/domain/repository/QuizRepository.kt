package com.binayshaw7777.dailyroundsassignment.domain.repository

import com.binayshaw7777.dailyroundsassignment.data.model.Question

interface QuizRepository {
    suspend fun loadQuestions(): Result<List<Question>>
}
