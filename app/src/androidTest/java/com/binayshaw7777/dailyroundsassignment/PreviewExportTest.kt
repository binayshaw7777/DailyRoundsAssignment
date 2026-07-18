package com.binayshaw7777.dailyroundsassignment

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.ui.components.OptionButton
import com.binayshaw7777.dailyroundsassignment.ui.components.StreakFlames
import com.binayshaw7777.dailyroundsassignment.ui.home.HomeScreen
import com.binayshaw7777.dailyroundsassignment.ui.leaderboard.LeaderboardScreen
import com.binayshaw7777.dailyroundsassignment.ui.leaderboard.LeaderboardUiState
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingScreen
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingUiState
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizScreen
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizUiState
import com.binayshaw7777.dailyroundsassignment.ui.quizstart.QuizStartScreen
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsScreen
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsUiState
import com.binayshaw7777.dailyroundsassignment.ui.settings.SettingsScreen
import com.binayshaw7777.dailyroundsassignment.ui.settings.SettingsUiState
import com.binayshaw7777.dailyroundsassignment.ui.splash.SplashScreen
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.userdetails.UserDetailsScreen
import com.binayshaw7777.dailyroundsassignment.ui.userdetails.UserDetailsUiState
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.io.FileOutputStream

class PreviewExportTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private fun saveScreenshot(bitmap: Bitmap, name: String) {
        val instr = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation()
        // Shell creates world-writable dir; drain stdout to wait for completion
        val pfd = instr.uiAutomation.executeShellCommand("mkdir -p /data/local/tmp/ComposePreviews")
        java.io.FileInputStream(pfd.fileDescriptor).use { it.readBytes() }
        pfd.close()

        // /data/local/tmp/ComposePreviews is 777 — app can write directly
        val file = File("/data/local/tmp/ComposePreviews", "$name.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        println("Saved: ${file.absolutePath}")
    }

    private fun captureAndSave(name: String) {
        composeTestRule.waitForIdle()
        val imageBitmap = composeTestRule.onRoot().captureToImage()
        saveScreenshot(imageBitmap.asAndroidBitmap(), name)
    }

    private val dummyQuestion = Question(
        id = 1,
        question = "What is the capital of France?",
        options = listOf("London", "Paris", "Berlin", "Rome"),
        correctOptionIndex = 1
    )

    @Test
    fun export01_SplashScreen() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme { SplashScreen() }
        }
        captureAndSave("01_SplashScreen")
    }

    @Test
    fun export02_OnboardingScreen() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                OnboardingScreen(uiState = OnboardingUiState(), onEvent = {})
            }
        }
        captureAndSave("02_OnboardingScreen")
    }

    @Test
    fun export03_UserDetailsScreen() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                UserDetailsScreen(uiState = UserDetailsUiState(name = "Binay"), onEvent = {})
            }
        }
        captureAndSave("03_UserDetailsScreen")
    }

    @Test
    fun export04_QuizStartScreen() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                QuizStartScreen(totalGames = 12, totalWins = 8, bestStreak = 5, onStartQuiz = {})
            }
        }
        captureAndSave("04_QuizStartScreen")
    }

    @Test
    fun export05_QuizStartScreen_FirstTime() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                QuizStartScreen(totalGames = 0, totalWins = 0, bestStreak = 0, onStartQuiz = {})
            }
        }
        captureAndSave("05_QuizStartScreen_FirstTime")
    }

    @Test
    fun export06_HomeScreen() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                HomeScreen(userName = "Binay", quizContent = {}, leaderboardContent = {}, settingsContent = {})
            }
        }
        captureAndSave("06_HomeScreen")
    }

    @Test
    fun export07_QuizScreen_Loading() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                QuizScreen(uiState = QuizUiState.Loading, onEvent = {})
            }
        }
        captureAndSave("07_QuizScreen_Loading")
    }

    @Test
    fun export08_QuizScreen_Content() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                QuizScreen(
                    uiState = QuizUiState.Content(
                        currentIndex = 2,
                        totalQuestions = 10,
                        currentQuestion = dummyQuestion,
                        selectedOptionIndex = null,
                        isAnswered = false,
                        currentStreak = 3,
                        longestStreak = 5,
                        correctCount = 7,
                        skippedCount = 1
                    ),
                    onEvent = {}
                )
            }
        }
        captureAndSave("08_QuizScreen_Content")
    }

    @Test
    fun export09_ResultsScreen() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                ResultsScreen(
                    uiState = ResultsUiState(correctCount = 8, totalQuestions = 10, longestStreak = 6, skippedCount = 1),
                    onEvent = {}
                )
            }
        }
        captureAndSave("09_ResultsScreen")
    }

    @Test
    fun export10_LeaderboardScreen() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                LeaderboardScreen(
                    uiState = LeaderboardUiState(
                        results = listOf(
                            QuizResult(id = 1, correctCount = 8, totalQuestions = 10, longestStreak = 5, skippedCount = 1, timestamp = 1718000000000L, isWin = true),
                            QuizResult(id = 2, correctCount = 4, totalQuestions = 10, longestStreak = 2, skippedCount = 2, timestamp = 1717913600000L, isWin = false)
                        ),
                        isLoading = false
                    ),
                    onEvent = {}
                )
            }
        }
        captureAndSave("10_LeaderboardScreen")
    }

    @Test
    fun export11_SettingsScreen() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                SettingsScreen(
                    uiState = SettingsUiState(useRemoteApi = true, isDarkTheme = true, appVersion = "1.0.0-preview"),
                    onEvent = {}
                )
            }
        }
        captureAndSave("11_SettingsScreen")
    }

    @Test
    fun export12_OptionButton_Default() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                OptionButton(text = "Default Option", optionIndex = 0, isAnswered = false, selectedOptionIndex = null, correctOptionIndex = 1, onOptionSelected = {})
            }
        }
        captureAndSave("12_OptionButton_Default")
    }

    @Test
    fun export13_OptionButton_Correct() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                OptionButton(text = "Correct Option", optionIndex = 0, isAnswered = true, selectedOptionIndex = 0, correctOptionIndex = 0, onOptionSelected = {})
            }
        }
        captureAndSave("13_OptionButton_Correct")
    }

    @Test
    fun export14_OptionButton_Wrong() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme {
                OptionButton(text = "Wrong Option", optionIndex = 1, isAnswered = true, selectedOptionIndex = 1, correctOptionIndex = 0, onOptionSelected = {})
            }
        }
        captureAndSave("14_OptionButton_Wrong")
    }

    @Test
    fun export15_StreakFlames() {
        composeTestRule.setContent {
            DailyRoundsAssignmentTheme { StreakFlames(currentStreak = 3) }
        }
        captureAndSave("15_StreakFlames")
    }
}
