package com.binayshaw7777.dailyroundsassignment.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.repository.QuizRepository
import com.binayshaw7777.dailyroundsassignment.util.SoundManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface QuizEffect {
    data object NavigateToResults : QuizEffect
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(application)

    private val _state = MutableStateFlow(QuizUiState())
    val state: StateFlow<QuizUiState> = _state.asStateFlow()

    private val _effects = Channel<QuizEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val result = repository.loadQuestions()
            result.fold(
                onSuccess = { questions ->
                    _state.update { it.copy(questions = questions, isLoading = false) }
                },
                onFailure = {
                    _state.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    fun selectOption(index: Int) {
        if (_state.value.isAnswered) return

        val currentState = _state.value
        val currentQuestion = currentState.currentQuestion ?: return
        val isCorrect = index == currentQuestion.correctOptionIndex

        val newStreak = if (isCorrect) currentState.currentStreak + 1 else 0
        val newLongestStreak = maxOf(currentState.longestStreak, newStreak)
        val newCorrectCount = if (isCorrect) currentState.correctCount + 1 else currentState.correctCount

        if (isCorrect) SoundManager.playSuccess() else SoundManager.playFailure()

        _state.update {
            it.copy(
                selectedOptionIndex = index,
                isAnswered = true,
                currentStreak = newStreak,
                longestStreak = newLongestStreak,
                correctCount = newCorrectCount
            )
        }

        viewModelScope.launch {
            delay(2000)
            advanceQuestion()
        }
    }

    fun skipQuestion() {
        if (_state.value.isAnswered) return

        _state.update { it.copy(skippedCount = it.skippedCount + 1, isAnswered = true) }
        viewModelScope.launch {
            advanceQuestion()
        }
    }

    private suspend fun advanceQuestion() {
        val currentState = _state.value
        if (currentState.currentIndex + 1 >= currentState.totalQuestions) {
            _effects.send(QuizEffect.NavigateToResults)
        } else {
            _state.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedOptionIndex = null,
                    isAnswered = false
                )
            }
        }
    }

    fun restartQuiz() {
        val questions = _state.value.questions
        _state.update {
            QuizUiState(
                questions = questions,
                isLoading = false
            )
        }
    }
}
