package com.binayshaw7777.dailyroundsassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.binayshaw7777.dailyroundsassignment.ui.home.HomeScreen
import com.binayshaw7777.dailyroundsassignment.ui.leaderboard.LeaderboardScreen
import com.binayshaw7777.dailyroundsassignment.ui.leaderboard.LeaderboardViewModel
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingEffect
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingScreen
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingViewModel
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizEffect
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizScreen
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizViewModel
import com.binayshaw7777.dailyroundsassignment.ui.quizstart.QuizStartScreen
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsScreen
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsUiEvent
import com.binayshaw7777.dailyroundsassignment.ui.settings.SettingsScreen
import com.binayshaw7777.dailyroundsassignment.ui.settings.SettingsViewModel
import com.binayshaw7777.dailyroundsassignment.ui.splash.SplashEffect
import com.binayshaw7777.dailyroundsassignment.ui.splash.SplashScreen
import com.binayshaw7777.dailyroundsassignment.ui.splash.SplashViewModel
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.userdetails.UserDetailsEffect
import com.binayshaw7777.dailyroundsassignment.ui.userdetails.UserDetailsScreen
import com.binayshaw7777.dailyroundsassignment.ui.userdetails.UserDetailsViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = viewModel()
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val quizViewModel: QuizViewModel = viewModel()
    val leaderboardViewModel: LeaderboardViewModel = viewModel()

    DailyRoundsAssignmentTheme(darkTheme = settingsState.isDarkTheme) {
        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") {
                SplashRoute(navController = navController)
            }
            composable("onboarding") {
                OnboardingRoute(navController = navController)
            }
            composable("userdetails") {
                UserDetailsRoute(navController = navController)
            }
            composable("home") {
                HomeRoute(
                    navController = navController,
                    quizViewModel = quizViewModel,
                    leaderboardViewModel = leaderboardViewModel,
                    settingsViewModel = settingsViewModel,
                )
            }
            composable("quiz") {
                QuizRoute(
                    viewModel = quizViewModel,
                    onNavigateToResults = { navController.navigate("results") },
                )
            }
            composable("results") {
                ResultsRoute(
                    viewModel = quizViewModel,
                    navController = navController,
                )
            }
        }
    }
}

@Composable
private fun SplashRoute(navController: NavController) {
    val splashViewModel: SplashViewModel = viewModel()

    LaunchedEffect(splashViewModel) {
        splashViewModel.effects.collect { effect ->
            when (effect) {
                SplashEffect.NavigateToOnboarding -> navController.navigate("onboarding") {
                    popUpTo("splash") { inclusive = true }
                }
                SplashEffect.NavigateToHome -> navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    SplashScreen()
}

@Composable
private fun OnboardingRoute(navController: NavController) {
    val onboardingViewModel: OnboardingViewModel = viewModel()
    val uiState by onboardingViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(onboardingViewModel) {
        onboardingViewModel.effects.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToUserDetails -> navController.navigate("userdetails") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        }
    }

    OnboardingScreen(
        uiState = uiState,
        onEvent = onboardingViewModel::onEvent,
    )
}

@Composable
private fun UserDetailsRoute(navController: NavController) {
    val userDetailsViewModel: UserDetailsViewModel = viewModel()
    val uiState by userDetailsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userDetailsViewModel) {
        userDetailsViewModel.effects.collect { effect ->
            when (effect) {
                UserDetailsEffect.NavigateToHome -> navController.navigate("home") {
                    popUpTo("userdetails") { inclusive = true }
                }
            }
        }
    }

    UserDetailsScreen(
        uiState = uiState,
        onEvent = userDetailsViewModel::onEvent,
    )
}

@Composable
private fun HomeRoute(
    navController: NavController,
    quizViewModel: QuizViewModel,
    leaderboardViewModel: LeaderboardViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val leaderboardState by leaderboardViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        userName = settingsState.userName,
        quizContent = {
            QuizStartScreen(
                totalGames = leaderboardState.results.size,
                totalWins = leaderboardState.totalWins,
                bestStreak = leaderboardState.results.maxOfOrNull { it.longestStreak } ?: 0,
                onStartQuiz = {
                    quizViewModel.startQuiz()
                    navController.navigate("quiz")
                },
            )
        },
        leaderboardContent = {
            LeaderboardRoute(viewModel = leaderboardViewModel)
        },
        settingsContent = {
            SettingsRoute(viewModel = settingsViewModel)
        },
    )
}

@Composable
private fun QuizRoute(
    viewModel: QuizViewModel,
    onNavigateToResults: () -> Unit,
) {
    val uiState by viewModel.quizUiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                QuizEffect.NavigateToResults -> onNavigateToResults()
            }
        }
    }

    QuizScreen(
        uiState = uiState,
        onEvent = viewModel::onQuizEvent,
    )
}

@Composable
private fun ResultsRoute(
    viewModel: QuizViewModel,
    navController: NavController,
) {
    val uiState by viewModel.resultsUiState.collectAsStateWithLifecycle()

    ResultsScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                ResultsUiEvent.RestartQuiz -> {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            }
        },
    )
}

@Composable
private fun LeaderboardRoute(viewModel: LeaderboardViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LeaderboardScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun SettingsRoute(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}
