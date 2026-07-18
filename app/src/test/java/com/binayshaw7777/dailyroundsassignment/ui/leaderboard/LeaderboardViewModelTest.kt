package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizResultRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LeaderboardViewModelTest {

    private val repository: QuizResultRepository = mockk(relaxed = true)
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
    fun `uiState combines results and max streak correctly`() = runTest {
        val dummyResults = listOf(
            QuizResult(id = 1L, correctCount = 5, totalQuestions = 10, longestStreak = 3, skippedCount = 1)
        )
        every { repository.getAll() } returns flowOf(dummyResults)
        every { repository.getMaxStreak() } returns flowOf(3)

        val viewModel = LeaderboardViewModel(repository)
        val collectJob = launch { viewModel.uiState.collect {} }
        runCurrent()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(dummyResults, state.results)
        assertEquals(3, state.bestStreak)

        collectJob.cancel()
    }

    @Test
    fun `onEvent ClearHistory calls repository clearAll`() = runTest {
        val viewModel = LeaderboardViewModel(repository)
        viewModel.onEvent(LeaderboardUiEvent.ClearHistory)
        runCurrent()

        coVerify(exactly = 1) { repository.clearAll() }
    }

    @Test
    fun `onEvent DeleteResult calls repository deleteById`() = runTest {
        val viewModel = LeaderboardViewModel(repository)
        viewModel.onEvent(LeaderboardUiEvent.DeleteResult(42L))
        runCurrent()

        coVerify(exactly = 1) { repository.deleteById(42L) }
    }
}
