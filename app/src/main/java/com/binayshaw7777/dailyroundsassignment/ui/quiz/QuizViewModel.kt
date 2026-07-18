package com.binayshaw7777.dailyroundsassignment.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import com.binayshaw7777.dailyroundsassignment.domain.usecase.LoadQuestionsUseCase
import com.binayshaw7777.dailyroundsassignment.util.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the active quiz screen.
 *
 * Manages the entire quiz lifecycle: loading questions, tracking the current
 * question index, selected option, streak counts, and persisting the final
 * result. Exposes a single [quizUiState] [StateFlow] that the Compose UI
 * collects.
 *
 * Question loading is triggered automatically on [init] and again in
 * [startQuiz] if the cache is empty or a previous error occurred.
 *
 * @property loadQuestionsUseCase Fetches and validates questions from the
 *   configured data source.
 * @property repository Persists the final quiz result to the database.
 */
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val loadQuestionsUseCase: LoadQuestionsUseCase,
    private val repository: QuizResultRepository,
) : ViewModel() {

    /**
     * Internal mutable snapshot of the entire quiz session.
     *
     * Mapped to the public [QuizUiState] sealed interface so the UI only sees
     * the relevant subset for the current phase (loading, error, or content).
     */
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
        val pendingHaptic: HapticEvent? = null,
        val navigateToResults: Boolean = false,
    )

    private val _session = MutableStateFlow(SessionState())
    private var hapticNonce = 0

    /** Public state stream for the quiz UI. Maps [SessionState] to [QuizUiState]. */
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
                pendingHaptic = s.pendingHaptic,
                navigateToResults = s.navigateToResults,
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, QuizUiState.Loading)

    init {
        Timber.d("QuizViewModel initialized")
        loadQuestions()
    }

    fun onQuizEvent(event: QuizUiEvent) {
        when (event) {
            is QuizUiEvent.SelectOption -> selectOption(event.index)
            QuizUiEvent.Skip -> skipQuestion()
            QuizUiEvent.OnNavigated -> _session.update { it.copy(navigateToResults = false) }
            QuizUiEvent.OnHapticConsumed -> _session.update { it.copy(pendingHaptic = null) }
        }
    }

    private fun loadQuestions() {
        Timber.d("loadQuestions() initiated")
        viewModelScope.launch {
            loadQuestionsUseCase().fold(
                onSuccess = { questions ->
                    Timber.d("Questions loaded: %d", questions.size)
                    _session.update { it.copy(questions = questions, isLoading = false) }
                },
                onFailure = { error ->
                    Timber.e(error, "Failed to load questions")
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

        Timber.d("selectOption(index=%d) — correct=%b, q=%d/%d", index, isCorrect, session.currentIndex + 1, session.questions.size)

        viewModelScope.launch {
            if (isCorrect) SoundManager.playSuccess() else SoundManager.playFailure()
        }

        val hapticType = if (isCorrect) QuizHaptic.Success else QuizHaptic.Failure
        val newStreak = if (isCorrect) session.currentStreak + 1 else 0
        _session.update {
            it.copy(
                selectedOptionIndex = index,
                isAnswered = true,
                currentStreak = newStreak,
                longestStreak = maxOf(it.longestStreak, newStreak),
                correctCount = if (isCorrect) it.correctCount + 1 else it.correctCount,
                pendingHaptic = HapticEvent(++hapticNonce, hapticType),
            )
        }
        viewModelScope.launch {
            delay(2000)
            advanceQuestion()
        }
    }

    private fun skipQuestion() {
        if (_session.value.isAnswered) return
        Timber.d("skipQuestion() — q=%d/%d", _session.value.currentIndex + 1, _session.value.questions.size)
        _session.update {
            it.copy(
                skippedCount = it.skippedCount + 1,
                isAnswered = true,
                pendingHaptic = HapticEvent(++hapticNonce, QuizHaptic.Skip),
            )
        }
        viewModelScope.launch { advanceQuestion() }
    }

    private suspend fun advanceQuestion() {
        val s = _session.value
        if (s.currentIndex + 1 >= s.questions.size) {
            Timber.d("Quiz complete — correct=%d/%d, streak=%d, skipped=%d", s.correctCount, s.questions.size, s.longestStreak, s.skippedCount)
            repository.save(
                QuizResult(
                    correctCount = s.correctCount,
                    totalQuestions = s.questions.size,
                    longestStreak = s.longestStreak,
                    skippedCount = s.skippedCount,
                )
            )
            _session.update { it.copy(navigateToResults = true) }
        } else {
            _session.update {
                it.copy(currentIndex = it.currentIndex + 1, selectedOptionIndex = null, isAnswered = false)
            }
            Timber.d("Advanced to question %d/%d", _session.value.currentIndex + 1, s.questions.size)
        }
    }

    fun startQuiz() {
        Timber.d("startQuiz() called")
        _session.update {
            it.copy(
                isLoading = true,
                currentIndex = 0,
                selectedOptionIndex = null,
                isAnswered = false,
                currentStreak = 0,
                correctCount = 0,
                skippedCount = 0,
                navigateToResults = false,
                pendingHaptic = null,
            )
        }
        viewModelScope.launch {
            delay(1200)
            loadQuestions()
        }
    }
}
