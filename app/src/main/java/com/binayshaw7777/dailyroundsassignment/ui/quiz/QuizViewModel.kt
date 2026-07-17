package com.binayshaw7777.dailyroundsassignment.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.binayshaw7777.dailyroundsassignment.data.local.db.QuizDatabase
import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.data.remote.QuizApiService
import com.binayshaw7777.dailyroundsassignment.data.repository.LocalQuizRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.data.repository.QuizResultRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.data.repository.RemoteQuizRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.domain.usecase.LoadQuestionsUseCase
import com.binayshaw7777.dailyroundsassignment.domain.usecase.SaveQuizResultUseCase
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsUiEvent
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsUiState
import com.binayshaw7777.dailyroundsassignment.util.SoundManager
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

sealed interface QuizEffect {
    data object NavigateToResults : QuizEffect
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = AppPreferences(application)
    private val localRepo = LocalQuizRepositoryImpl(application)
    private val remoteRepo = RemoteQuizRepositoryImpl(QuizApiService())
    private val db = QuizDatabase.getInstance(application)
    private val resultRepo = QuizResultRepositoryImpl(db.quizResultDao())
    private val loadQuestionsUseCase = LoadQuestionsUseCase(localRepo, remoteRepo, prefs)
    private val saveQuizResultUseCase = SaveQuizResultUseCase(resultRepo)

    // Internal session state — not exposed directly
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

    // Quiz screen state
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

    // Results screen state
    val resultsUiState: StateFlow<ResultsUiState> = _session.map { s ->
        ResultsUiState(
            correctCount = s.correctCount,
            totalQuestions = s.questions.size,
            longestStreak = s.longestStreak,
            skippedCount = s.skippedCount,
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ResultsUiState())

    private val _effects = Channel<QuizEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadQuestions()
    }

    fun onQuizEvent(event: QuizUiEvent) {
        when (event) {
            is QuizUiEvent.SelectOption -> selectOption(event.index)
            QuizUiEvent.Skip -> skipQuestion()
        }
    }

    fun onResultsEvent(event: ResultsUiEvent) {
        when (event) {
            ResultsUiEvent.RestartQuiz -> restartQuiz()
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            loadQuestionsUseCase().fold(
                onSuccess = { questions ->
                    _session.update { it.copy(questions = questions, isLoading = false) }
                },
                onFailure = { error ->
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

        if (isCorrect) SoundManager.playSuccess() else SoundManager.playFailure()

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
        viewModelScope.launch { advanceQuestion() }
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
        val questions = _session.value.questions
        if (questions.isEmpty() || _session.value.error != null) {
            _session.update { SessionState() }
            loadQuestions()
        } else {
            _session.update { SessionState(questions = questions, isLoading = false) }
        }
    }

    private fun restartQuiz() {
        startQuiz()
    }
}
