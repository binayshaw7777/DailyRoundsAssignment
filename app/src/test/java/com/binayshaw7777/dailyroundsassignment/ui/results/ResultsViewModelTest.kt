package com.binayshaw7777.dailyroundsassignment.ui.results

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResultsViewModelTest {

    private val repository: QuizResultRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState returns latest result stats when repository has results`() = runTest {
        val dummyResult = QuizResult(
            id = 1L,
            correctCount = 7,
            totalQuestions = 10,
            longestStreak = 5,
            skippedCount = 2,
            timestamp = 123456789L
        )
        every { repository.getAll() } returns flowOf(listOf(dummyResult))

        val viewModel = ResultsViewModel(repository)
        val collectJob = launch { viewModel.uiState.collect {} }
        runCurrent()

        val state = viewModel.uiState.value
        assertEquals(7, state.correctCount)
        assertEquals(10, state.totalQuestions)
        assertEquals(5, state.longestStreak)
        assertEquals(2, state.skippedCount)
        assertEquals(70, state.accuracy)

        collectJob.cancel()
    }

    @Test
    fun `uiState returns default values when repository has no results`() = runTest {
        every { repository.getAll() } returns flowOf(emptyList())

        val viewModel = ResultsViewModel(repository)
        val collectJob = launch { viewModel.uiState.collect {} }
        runCurrent()

        val state = viewModel.uiState.value
        assertEquals(0, state.correctCount)
        assertEquals(0, state.totalQuestions)
        assertEquals(0, state.longestStreak)
        assertEquals(0, state.skippedCount)
        assertEquals(0, state.accuracy)

        collectJob.cancel()
    }
}
