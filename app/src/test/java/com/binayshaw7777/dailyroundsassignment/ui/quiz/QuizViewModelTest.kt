package com.binayshaw7777.dailyroundsassignment.ui.quiz

import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import com.binayshaw7777.dailyroundsassignment.domain.usecase.LoadQuestionsUseCase
import com.binayshaw7777.dailyroundsassignment.util.SoundManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val loadQuestionsUseCase: LoadQuestionsUseCase = mockk()
    private val repository: QuizResultRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    private val dummyQuestions = listOf(
        Question(1, "Question 1", listOf("A", "B", "C", "D"), 0),
        Question(2, "Question 2", listOf("A", "B", "C", "D"), 1),
        Question(3, "Question 3", listOf("A", "B", "C", "D"), 2)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(SoundManager)
        coEvery { SoundManager.playSuccess() } returns Unit
        coEvery { SoundManager.playFailure() } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `init loads questions and sets state to content`() = runTest {
        coEvery { loadQuestionsUseCase() } returns Result.success(dummyQuestions)

        val viewModel = QuizViewModel(loadQuestionsUseCase, repository)
        
        // Let init block execute its coroutine
        testDispatcher.scheduler.runCurrent()

        val state = viewModel.quizUiState.value
        assertTrue(state is QuizUiState.Content)
        val content = state as QuizUiState.Content
        assertEquals(0, content.currentIndex)
        assertEquals(3, content.totalQuestions)
        assertEquals(dummyQuestions[0], content.currentQuestion)
    }

    @Test
    fun `init loads questions failure sets state to error`() = runTest {
        coEvery { loadQuestionsUseCase() } returns Result.failure(Exception("Load error"))

        val viewModel = QuizViewModel(loadQuestionsUseCase, repository)
        testDispatcher.scheduler.runCurrent()

        val state = viewModel.quizUiState.value
        assertTrue(state is QuizUiState.Error)
        assertEquals("Load error", (state as QuizUiState.Error).message)
    }

    @Test
    fun `selectOption correct updates correctCount and streaks`() = runTest {
        coEvery { loadQuestionsUseCase() } returns Result.success(dummyQuestions)

        val viewModel = QuizViewModel(loadQuestionsUseCase, repository)
        runCurrent()

        // Select correct option for question 1 (index 0)
        viewModel.onQuizEvent(QuizUiEvent.SelectOption(0))
        runCurrent()

        val state = viewModel.quizUiState.value as QuizUiState.Content
        assertEquals(1, state.correctCount)
        assertEquals(1, state.currentStreak)
        assertEquals(1, state.longestStreak)
        assertEquals(0, state.selectedOptionIndex)
        assertTrue(state.isAnswered)
        assertEquals(QuizHaptic.Success, state.pendingHaptic?.type)

        // Advance 2 seconds to advance question
        advanceTimeBy(2001)
        runCurrent()

        val advancedState = viewModel.quizUiState.value as QuizUiState.Content
        assertEquals(1, advancedState.currentIndex)
        assertNull(advancedState.selectedOptionIndex)
        assertTrue(!advancedState.isAnswered)
    }

    @Test
    fun `selectOption incorrect resets streak`() = runTest {
        coEvery { loadQuestionsUseCase() } returns Result.success(dummyQuestions)

        val viewModel = QuizViewModel(loadQuestionsUseCase, repository)
        runCurrent()

        // Make first answer correct to build a streak
        viewModel.onQuizEvent(QuizUiEvent.SelectOption(0))
        runCurrent()
        advanceTimeBy(2001)
        runCurrent()

        // Make second answer incorrect (correct is 1, select 2)
        viewModel.onQuizEvent(QuizUiEvent.SelectOption(2))
        runCurrent()

        val state = viewModel.quizUiState.value as QuizUiState.Content
        assertEquals(1, state.correctCount) // no change
        assertEquals(0, state.currentStreak) // reset
        assertEquals(1, state.longestStreak) // preserved
        assertEquals(QuizHaptic.Failure, state.pendingHaptic?.type)
    }

    @Test
    fun `skipQuestion increments skippedCount and advances`() = runTest {
        coEvery { loadQuestionsUseCase() } returns Result.success(dummyQuestions)

        val viewModel = QuizViewModel(loadQuestionsUseCase, repository)
        runCurrent()

        viewModel.onQuizEvent(QuizUiEvent.Skip)
        runCurrent()

        val state = viewModel.quizUiState.value as QuizUiState.Content
        assertEquals(1, state.skippedCount)
        assertEquals(1, state.currentIndex)
        assertFalse(state.isAnswered)
        assertEquals(QuizHaptic.Skip, state.pendingHaptic?.type)
    }

    @Test
    fun `completing quiz saves results to repository`() = runTest {
        coEvery { loadQuestionsUseCase() } returns Result.success(listOf(dummyQuestions[0]))

        val viewModel = QuizViewModel(loadQuestionsUseCase, repository)
        runCurrent()

        // Answer the single question correctly
        viewModel.onQuizEvent(QuizUiEvent.SelectOption(0))
        runCurrent()

        // Wait for advancement which should complete the quiz
        advanceTimeBy(2001)
        runCurrent()

        val state = viewModel.quizUiState.value as QuizUiState.Content
        assertTrue(state.navigateToResults)

        coVerify(exactly = 1) {
            repository.save(
                match {
                    it.correctCount == 1 &&
                    it.totalQuestions == 1 &&
                    it.longestStreak == 1 &&
                    it.skippedCount == 0
                }
            )
        }
    }
}
