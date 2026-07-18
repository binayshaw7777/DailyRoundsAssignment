package com.binayshaw7777.dailyroundsassignment.domain.usecase

import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.repository.LocalQuizRepositoryImpl
import com.binayshaw7777.dailyroundsassignment.data.repository.RemoteQuizRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoadQuestionsUseCaseTest {

    private val localRepo: LocalQuizRepositoryImpl = mockk()
    private val remoteRepo: RemoteQuizRepositoryImpl = mockk()
    private val prefs: AppPreferences = mockk()

    private lateinit var useCase: LoadQuestionsUseCase

    private val dummyQuestions = listOf(
        Question(1, "Question 1", listOf("A", "B", "C", "D"), 0),
        Question(2, "Question 2", listOf("A", "B", "C", "D"), 1),
        // Invalid: less than 4 options
        Question(3, "Question 3", listOf("A", "B"), 0),
        // Invalid: blank question
        Question(4, "   ", listOf("A", "B", "C", "D"), 2),
        // Invalid: correctOptionIndex out of bounds
        Question(5, "Question 5", listOf("A", "B", "C", "D"), 4)
    )

    @Before
    fun setUp() {
        useCase = LoadQuestionsUseCase(localRepo, remoteRepo, prefs)
    }

    @Test
    fun `invoke uses local repo when useRemoteApi is false and filters invalid questions`() = runTest {
        // Given
        every { prefs.useRemoteApi } returns flowOf(false)
        every { prefs.shuffleQuestions } returns flowOf(false)
        coEvery { localRepo.loadQuestions() } returns Result.success(dummyQuestions)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val questions = result.getOrNull()!!
        // Only 2 out of 5 questions are valid
        assertEquals(2, questions.size)
        assertEquals(1, questions[0].id)
        assertEquals(2, questions[1].id)

        coVerify(exactly = 1) { localRepo.loadQuestions() }
        coVerify(exactly = 0) { remoteRepo.loadQuestions() }
    }

    @Test
    fun `invoke uses remote repo when useRemoteApi is true`() = runTest {
        // Given
        every { prefs.useRemoteApi } returns flowOf(true)
        every { prefs.shuffleQuestions } returns flowOf(false)
        coEvery { remoteRepo.loadQuestions() } returns Result.success(dummyQuestions)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val questions = result.getOrNull()!!
        assertEquals(2, questions.size)

        coVerify(exactly = 0) { localRepo.loadQuestions() }
        coVerify(exactly = 1) { remoteRepo.loadQuestions() }
    }

    @Test
    fun `invoke returns failure when repo fails`() = runTest {
        // Given
        every { prefs.useRemoteApi } returns flowOf(false)
        every { prefs.shuffleQuestions } returns flowOf(false)
        val exception = RuntimeException("Database error")
        coEvery { localRepo.loadQuestions() } returns Result.failure(exception)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
