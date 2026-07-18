package com.binayshaw7777.dailyroundsassignment.ui.settings

import com.binayshaw7777.dailyroundsassignment.data.local.preferences.AppPreferences
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val prefs: AppPreferences = mockk(relaxed = true)
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
    fun `uiState combines AppPreferences flows correctly`() = runTest {
        every { prefs.useRemoteApi } returns flowOf(true)
        every { prefs.isDarkTheme } returns flowOf(false)
        every { prefs.userName } returns flowOf("John Doe")
        every { prefs.shuffleQuestions } returns flowOf(true)

        val viewModel = SettingsViewModel(prefs)
        val collectJob = launch { viewModel.uiState.collect {} }
        runCurrent()

        val state = viewModel.uiState.value
        assertTrue(state.useRemoteApi)
        assertEquals(false, state.isDarkTheme)
        assertEquals("John Doe", state.userName)
        assertTrue(state.shuffleQuestions)

        collectJob.cancel()
    }

    @Test
    fun `onEvent SetUseRemoteApi calls AppPreferences setUseRemoteApi`() = runTest {
        val viewModel = SettingsViewModel(prefs)
        viewModel.onEvent(SettingsUiEvent.SetUseRemoteApi(true))
        runCurrent()

        coVerify(exactly = 1) { prefs.setUseRemoteApi(true) }
    }

    @Test
    fun `onEvent SetDarkTheme calls AppPreferences setDarkTheme`() = runTest {
        val viewModel = SettingsViewModel(prefs)
        viewModel.onEvent(SettingsUiEvent.SetDarkTheme(false))
        runCurrent()

        coVerify(exactly = 1) { prefs.setDarkTheme(false) }
    }

    @Test
    fun `onEvent SetShuffleQuestions calls AppPreferences setShuffleQuestions`() = runTest {
        val viewModel = SettingsViewModel(prefs)
        viewModel.onEvent(SettingsUiEvent.SetShuffleQuestions(true))
        runCurrent()

        coVerify(exactly = 1) { prefs.setShuffleQuestions(true) }
    }
}
