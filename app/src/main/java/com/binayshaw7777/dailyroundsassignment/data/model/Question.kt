package com.binayshaw7777.dailyroundsassignment.data.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a single quiz question with multiple-choice options.
 *
 * This is the core data entity used throughout the app — from JSON deserialization
 * (local asset or remote API) through to the quiz UI layer.
 *
 * @property id Unique identifier for the question.
 * @property question The question text displayed to the user.
 * @property options List of answer choices (typically 4). The list order determines
 *   the display index (0 = "A", 1 = "B", etc.).
 * @property correctOptionIndex Zero-based index into [options] indicating the correct answer.
 *
 * @see com.binayshaw7777.dailyroundsassignment.data.remote.dto.QuestionDto for the
 *   network-layer equivalent.
 *
 * ```
 * val question = Question(
 *     id = 1,
 *     question = "What is the capital of France?",
 *     options = listOf("London", "Paris", "Berlin", "Rome"),
 *     correctOptionIndex = 1
 * )
 * ```
 *
 */
@Serializable
data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int
)
