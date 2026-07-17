package com.binayshaw7777.dailyroundsassignment.ui.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.usecase.LoadQuestionsUseCase
import com.binayshaw7777.dailyroundsassignment.domain.usecase.SaveQuizResultUseCase
import com.binayshaw7777.dailyroundsassignment.util.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "QuizViewModel"

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val loadQuestionsUseCase: LoadQuestionsUseCase,
    private val saveQuizResultUseCase: SaveQuizResultUseCase,
) : ViewModel() {

    private data class SessionState(
        val questions: List<Question> = emptyList(),
        val currentIndex: Int = 0,
        val selectedOptionIndex: Int? = null,
        val isAnswered: Boolean = false,
        val currentStreak: Int = 0,
        val longestStreak: Int = 0,
        val correctCount: Int = 0,
        val skippedCount: Int = 0,
        val isLoading: Boolean = true,
        val error: String? = null,
    )

    private val _session = MutableStateFlow(SessionState())

    val quizUiState: StateFlow<QuizUiState> = _session.map { s ->
        when {
            s.isLoading -> QuizUiState.Loading
            s.error != null -> QuizUiState.Error(s.error)
            s.questions.isEmpty() -> QuizUiState.Error("No questions available")
            else -> QuizUiState.Content(
                currentIndex = s.currentIndex,
                totalQuestions = s.questions.size,
                currentQuestion = s.questions[s.currentIndex],
                selectedOptionIndex = s.selectedOptionIndex,
                isAnswered = s.isAnswered,
                currentStreak = s.currentStreak,
                longestStreak = s.longestStreak,
                correctCount = s.correctCount,
                skippedCount = s.skippedCount,
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, QuizUiState.Loading)

    private val _effects = Channel<QuizEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        Log.d(TAG, "QuizViewModel initialized")
        loadQuestions()
    }

    fun onQuizEvent(event: QuizUiEvent) {
        when (event) {
            is QuizUiEvent.SelectOption -> selectOption(event.index)
            QuizUiEvent.Skip -> skipQuestion()
        }
    }

    private fun loadQuestions() {
        Log.d(TAG, "loadQuestions() initiated from ViewModel")
        viewModelScope.launch {
            loadQuestionsUseCase().fold(
                onSuccess = { questions ->
                    Log.d(TAG, "Questions loaded successfully: ${questions.size} questions")
                    _session.update { it.copy(questions = questions, isLoading = false) }
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to load questions: ${error.message}", error)
                    _session.update {
                        it.copy(isLoading = false, error = error.message ?: "Failed to load questions")
                    }
                }
            )
        }
    }

    private fun selectOption(index: Int) {
        if (_session.value.isAnswered) return
        val session = _session.value
        val question = session.questions.getOrNull(session.currentIndex) ?: return
        val isCorrect = index == question.correctOptionIndex

        viewModelScope.launch {
            if (isCorrect) {
                SoundManager.playSuccess()
                _effects.send(QuizEffect.HapticSuccess)
            } else {
                SoundManager.playFailure()
                _effects.send(QuizEffect.HapticFailure)
            }
        }

        val newStreak = if (isCorrect) session.currentStreak + 1 else 0
        _session.update {
            it.copy(
                selectedOptionIndex = index,
                isAnswered = true,
                currentStreak = newStreak,
                longestStreak = maxOf(it.longestStreak, newStreak),
                correctCount = if (isCorrect) it.correctCount + 1 else it.correctCount,
            )
        }
        viewModelScope.launch {
            delay(2000)
            advanceQuestion()
        }
    }

    private fun skipQuestion() {
        if (_session.value.isAnswered) return
        _session.update { it.copy(skippedCount = it.skippedCount + 1, isAnswered = true) }
        viewModelScope.launch {
            _effects.send(QuizEffect.HapticSkip)
            advanceQuestion()
        }
    }

    private suspend fun advanceQuestion() {
        val s = _session.value
        if (s.currentIndex + 1 >= s.questions.size) {
            val result = QuizResult(
                correctCount = s.correctCount,
                totalQuestions = s.questions.size,
                longestStreak = s.longestStreak,
                skippedCount = s.skippedCount,
                timestamp = System.currentTimeMillis(),
                isWin = s.correctCount >= s.questions.size / 2,
            )
            saveQuizResultUseCase(result)
            _effects.send(QuizEffect.NavigateToResults)
        } else {
            _session.update {
                it.copy(currentIndex = it.currentIndex + 1, selectedOptionIndex = null, isAnswered = false)
            }
        }
    }

    fun startQuiz() {
        Log.d(TAG, "startQuiz() called")
        val questions = _session.value.questions
        if (questions.isEmpty() || _session.value.error != null) {
            Log.d(TAG, "No cached questions or previous error - reloading from source")
            _session.update { SessionState() }
            loadQuestions()
        } else {
            Log.d(TAG, "Using cached ${questions.size} questions")
            _session.update { SessionState(questions = questions, isLoading = false) }
        }
    }
}
