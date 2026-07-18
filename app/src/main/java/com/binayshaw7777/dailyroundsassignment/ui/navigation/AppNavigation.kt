package com.binayshaw7777.dailyroundsassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.binayshaw7777.dailyroundsassignment.ui.home.HomeScreen
import com.binayshaw7777.dailyroundsassignment.ui.leaderboard.LeaderboardScreen
import com.binayshaw7777.dailyroundsassignment.ui.leaderboard.LeaderboardViewModel
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingDestination
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingScreen
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingUiEvent
import com.binayshaw7777.dailyroundsassignment.ui.onboarding.OnboardingViewModel
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizHaptic
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizScreen
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizUiEvent
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizUiState
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizViewModel
import com.binayshaw7777.dailyroundsassignment.ui.quizstart.QuizStartScreen
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsScreen
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsUiEvent
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsViewModel
import com.binayshaw7777.dailyroundsassignment.ui.settings.SettingsScreen
import com.binayshaw7777.dailyroundsassignment.ui.settings.SettingsViewModel
import com.binayshaw7777.dailyroundsassignment.ui.splash.SplashDestination
import com.binayshaw7777.dailyroundsassignment.ui.splash.SplashScreen
import com.binayshaw7777.dailyroundsassignment.ui.splash.SplashUiEvent
import com.binayshaw7777.dailyroundsassignment.ui.splash.SplashViewModel
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.userdetails.UserDetailsScreen
import com.binayshaw7777.dailyroundsassignment.ui.userdetails.UserDetailsUiEvent
import com.binayshaw7777.dailyroundsassignment.ui.userdetails.UserDetailsViewModel
import com.binayshaw7777.dailyroundsassignment.util.hapticClick
import com.binayshaw7777.dailyroundsassignment.util.hapticFailure
import com.binayshaw7777.dailyroundsassignment.util.hapticSuccess

/**
 * Root composable that wires up the entire Navigation graph.
 *
 * This is the single entry point set in [com.binayshaw7777.dailyroundsassignment.MainActivity].
 * It owns the [NavHost], the shared [SettingsViewModel] (for theme), and the shared
 * [QuizViewModel] (to avoid re-loading questions on re-entry).
 *
 * ### Navigation flow
 * ```
 * Splash ──(onboarding not done)──▶ Onboarding ──▶ UserDetails ──▶ Home
 * Splash ──(onboarding done)───────▶ Home
 * Home ──(start quiz)──▶ Quiz ──▶ Results ──▶ Home
 * ```
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val quizViewModel: QuizViewModel = hiltViewModel()

    DailyRoundsAssignmentTheme(darkTheme = settingsState.isDarkTheme) {
        NavHost(navController = navController, startDestination = Screen.Splash.route) {
            composable(Screen.Splash.route) {
                SplashRoute(
                    onNavigateToOnboarding = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(Screen.Onboarding.route) {
                OnboardingRoute(
                    onNavigateToUserDetails = {
                        navController.navigate(Screen.UserDetails.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(Screen.UserDetails.route) {
                UserDetailsRoute(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.UserDetails.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(Screen.Home.route) {
                val leaderboardViewModel: LeaderboardViewModel = hiltViewModel()
                HomeRoute(
                    onNavigateToQuiz = {
                        quizViewModel.startQuiz()
                        navController.navigate(Screen.Quiz.route)
                    },
                    leaderboardViewModel = leaderboardViewModel,
                    settingsViewModel = settingsViewModel,
                )
            }
            composable(Screen.Quiz.route) {
                QuizRoute(
                    viewModel = quizViewModel,
                    onNavigateToResults = { navController.navigate(Screen.Results.route) },
                )
            }
            composable(Screen.Results.route) {
                ResultsRoute(
                    onRestartQuiz = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun SplashRoute(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val splashViewModel: SplashViewModel = hiltViewModel()
    val uiState by splashViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.destination) {
        val dest = uiState.destination ?: return@LaunchedEffect
        when (dest) {
            SplashDestination.Onboarding -> {
                onNavigateToOnboarding()
                splashViewModel.onEvent(SplashUiEvent.OnNavigated)
            }
            SplashDestination.Home -> {
                onNavigateToHome()
                splashViewModel.onEvent(SplashUiEvent.OnNavigated)
            }
        }
    }

    SplashScreen()
}

@Composable
private fun OnboardingRoute(onNavigateToUserDetails: () -> Unit) {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val uiState by onboardingViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.destination) {
        val dest = uiState.destination ?: return@LaunchedEffect
        when (dest) {
            OnboardingDestination.UserDetails -> {
                onNavigateToUserDetails()
                onboardingViewModel.onEvent(OnboardingUiEvent.OnNavigated)
            }
        }
    }

    OnboardingScreen(
        uiState = uiState,
        onEvent = onboardingViewModel::onEvent,
    )
}

@Composable
private fun UserDetailsRoute(onNavigateToHome: () -> Unit) {
    val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()
    val uiState by userDetailsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            onNavigateToHome()
            userDetailsViewModel.onEvent(UserDetailsUiEvent.OnNavigated)
        }
    }

    UserDetailsScreen(
        uiState = uiState,
        onEvent = userDetailsViewModel::onEvent,
    )
}

@Composable
private fun HomeRoute(
    onNavigateToQuiz: () -> Unit,
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
                onStartQuiz = onNavigateToQuiz,
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
    val view = LocalView.current

    LaunchedEffect(uiState) {
        val state = uiState as? QuizUiState.Content ?: return@LaunchedEffect
        if (state.navigateToResults) {
            onNavigateToResults()
            viewModel.onQuizEvent(QuizUiEvent.OnNavigated)
        }
    }

    LaunchedEffect(uiState) {
        val state = uiState as? QuizUiState.Content ?: return@LaunchedEffect
        val hapticEvent = state.pendingHaptic ?: return@LaunchedEffect
        when (hapticEvent.type) {
            QuizHaptic.Success -> view.hapticSuccess()
            QuizHaptic.Failure -> view.hapticFailure()
            QuizHaptic.Skip -> view.hapticClick()
        }
        viewModel.onQuizEvent(QuizUiEvent.OnHapticConsumed)
    }

    QuizScreen(
        uiState = uiState,
        onEvent = viewModel::onQuizEvent,
    )
}

@Composable
private fun ResultsRoute(
    onRestartQuiz: () -> Unit,
    viewModel: ResultsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ResultsScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                ResultsUiEvent.RestartQuiz -> onRestartQuiz()
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
