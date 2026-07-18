package com.binayshaw7777.dailyroundsassignment.util

/**
 * Computes an integer accuracy percentage (0–100) from a correct/total pair.
 *
 * @param correctCount Number of correct answers. Must be >= 0.
 * @param totalQuestions Total number of questions attempted. Must be >= 0.
 * @return Accuracy as a whole-number percentage. Returns `0` when [totalQuestions] is zero
 *   to avoid division by zero.
 *
 * ```kotlin
 * val pct = calculateAccuracy(correctCount = 7, totalQuestions = 10)
 * // pct == 70
 * ```
 */
fun calculateAccuracy(correctCount: Int, totalQuestions: Int): Int {
    return if (totalQuestions > 0) correctCount * 100 / totalQuestions else 0
}
