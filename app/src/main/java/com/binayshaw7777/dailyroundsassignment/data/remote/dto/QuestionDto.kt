package com.binayshaw7777.dailyroundsassignment.data.remote.dto

import com.binayshaw7777.dailyroundsassignment.data.model.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
)

fun QuestionDto.toDomain() = Question(
    id = id,
    question = question,
    options = options,
    correctOptionIndex = correctOptionIndex,
)
