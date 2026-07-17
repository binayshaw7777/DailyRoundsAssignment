package com.binayshaw7777.dailyroundsassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.binayshaw7777.dailyroundsassignment.util.hapticClick
import com.binayshaw7777.dailyroundsassignment.util.hapticFailure
import com.binayshaw7777.dailyroundsassignment.util.hapticSuccess
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
import com.binayshaw7777.dailyroundsassignment.ui.results.ResultsViewModel
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
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val quizViewModel: QuizViewModel = hiltViewModel()
    val leaderboardViewModel: LeaderboardViewModel = hiltViewModel()

    DailyRoundsAssignmentTheme(darkTheme = settingsState.isDarkTheme) {
        NavHost(navController = navController, startDestination = Screen.Splash.route) {
            composable(Screen.Splash.route) {
                SplashRoute(navController = navController)
            }
            composable(Screen.Onboarding.route) {
                OnboardingRoute(navController = navController)
            }
            composable(Screen.UserDetails.route) {
                UserDetailsRoute(navController = navController)
            }
            composable(Screen.Home.route) {
                HomeRoute(
                    navController = navController,
                    quizViewModel = quizViewModel,
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
                    navController = navController,
                )
            }
        }
    }
}

@Composable
private fun SplashRoute(navController: NavController) {
    val splashViewModel: SplashViewModel = hiltViewModel()

    LaunchedEffect(splashViewModel) {
        splashViewModel.effects.collect { effect ->
            when (effect) {
                SplashEffect.NavigateToOnboarding -> navController.navigate(Screen.Onboarding.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
                SplashEffect.NavigateToHome -> navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
    }

    SplashScreen()
}

@Composable
private fun OnboardingRoute(navController: NavController) {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val uiState by onboardingViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(onboardingViewModel) {
        onboardingViewModel.effects.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToUserDetails -> navController.navigate(Screen.UserDetails.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
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
    val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()
    val uiState by userDetailsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userDetailsViewModel) {
        userDetailsViewModel.effects.collect { effect ->
            when (effect) {
                UserDetailsEffect.NavigateToHome -> navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.UserDetails.route) { inclusive = true }
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
                    navController.navigate(Screen.Quiz.route)
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
    val view = LocalView.current

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                QuizEffect.NavigateToResults -> onNavigateToResults()
                QuizEffect.HapticSuccess -> view.hapticSuccess()
                QuizEffect.HapticFailure -> view.hapticFailure()
                QuizEffect.HapticSkip -> view.hapticClick()
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
    navController: NavController,
    viewModel: ResultsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ResultsScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                ResultsUiEvent.RestartQuiz -> {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
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
