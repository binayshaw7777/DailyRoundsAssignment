package com.binayshaw7777.dailyroundsassignment.ui.quiz

sealed interface QuizUiEvent {
    data class SelectOption(val index: Int) : QuizUiEvent
    data object Skip : QuizUiEvent
}
