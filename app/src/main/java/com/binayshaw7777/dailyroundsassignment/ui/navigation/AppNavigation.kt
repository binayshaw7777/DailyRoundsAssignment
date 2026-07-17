package com.binayshaw7777.dailyroundsassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizEffect
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizViewModel
import com.binayshaw7777.dailyroundsassignment.ui.screens.QuizScreen
import com.binayshaw7777.dailyroundsassignment.ui.screens.ResultsScreen
import com.binayshaw7777.dailyroundsassignment.ui.screens.SplashScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Quiz : Screen("quiz")
    object Results : Screen("results")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val quizViewModel: QuizViewModel = viewModel()

    LaunchedEffect(Unit) {
        quizViewModel.effects.collectLatest { effect ->
            when (effect) {
                is QuizEffect.NavigateToResults -> {
                    navController.navigate(Screen.Results.route) {
                        popUpTo(Screen.Quiz.route) { inclusive = false }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToQuiz = {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Quiz.route) {
            QuizScreen(viewModel = quizViewModel)
        }

        composable(Screen.Results.route) {
            ResultsScreen(
                viewModel = quizViewModel,
                onRestartQuiz = {
                    quizViewModel.restartQuiz()
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Results.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
