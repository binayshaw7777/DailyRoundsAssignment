package com.binayshaw7777.dailyroundsassignment.ui.quiz

sealed interface QuizEffect {
    data object NavigateToResults : QuizEffect
    data object HapticSuccess : QuizEffect
    data object HapticFailure : QuizEffect
    data object HapticSkip : QuizEffect
}
