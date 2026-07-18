package com.binayshaw7777.dailyroundsassignment.data.remote.dto

import com.binayshaw7777.dailyroundsassignment.data.model.Question
import kotlinx.serialization.Serializable

/**
 * Data-transfer object for questions fetched from the remote API.
 *
 * Mirrors the JSON shape returned by the gist endpoint and is mapped to the
 * domain [Question] model via [toDomain].
 *
 * @property id Unique identifier for the question.
 * @property question The question text.
 * @property options List of answer choices.
 * @property correctOptionIndex Zero-based index of the correct answer.
 *
 * ```kotlin
 * // Deserializing from JSON
 * val dto = Json.decodeFromString<QuestionDto>(jsonString)
 * val question = dto.toDomain()
 * ```
 */
@Serializable
data class QuestionDto(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
)

/**
 * Maps this DTO to the domain [Question] model.
 *
 * @return A [Question] with the same field values.
 */
fun QuestionDto.toDomain() = Question(
    id = id,
    question = question,
    options = options,
    correctOptionIndex = correctOptionIndex,
)
