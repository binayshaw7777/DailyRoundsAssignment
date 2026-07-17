package com.binayshaw7777.dailyroundsassignment.ui.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.components.OptionButton
import com.binayshaw7777.dailyroundsassignment.ui.components.StreakFlames

import androidx.compose.ui.tooling.preview.Preview
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.data.model.Question

@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onEvent: (QuizUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var swipeDeltaX by remember { mutableFloatStateOf(0f) }
    val isAnswered = (uiState as? QuizUiState.Content)?.isAnswered ?: true

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .pointerInput(isAnswered) {
                detectHorizontalDragGestures(
                    onDragStart = { swipeDeltaX = 0f },
                    onDragEnd = {
                        if (swipeDeltaX < -200f && !isAnswered) onEvent(QuizUiEvent.Skip)
                        swipeDeltaX = 0f
                    },
                    onHorizontalDrag = { _, dragAmount -> swipeDeltaX += dragAmount }
                )
            }
    ) {
        when (uiState) {
            is QuizUiState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is QuizUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
            }

            is QuizUiState.Content -> QuizContent(
                state = uiState,
                onEvent = onEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun QuizContent(
    state: QuizUiState.Content,
    onEvent: (QuizUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Quiz",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        // Streak flames
        StreakFlames(
            currentStreak = state.currentStreak,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
        )

        // Streak badge
        AnimatedVisibility(
            visible = state.streakActive,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Text(
                text = "🔥 ${state.currentStreak} questions streak achieved!!",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp),
            )
        }

        // Progress
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
        ) {
            Text(
                text = "Question ${state.currentIndex + 1} of ${state.totalQuestions}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        // Animated question + options
        AnimatedContent(
            targetState = state.currentIndex,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it } + fadeOut())
            },
            label = "questionTransition",
        ) { index ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = state.currentQuestion.question,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 32.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                )

                state.currentQuestion.options.forEachIndexed { optionIndex, optionText ->
                    OptionButton(
                        text = optionText,
                        optionIndex = optionIndex,
                        isAnswered = state.isAnswered,
                        selectedOptionIndex = state.selectedOptionIndex,
                        correctOptionIndex = state.currentQuestion.correctOptionIndex,
                        onOptionSelected = { onEvent(QuizUiEvent.SelectOption(it)) },
                        modifier = Modifier.padding(vertical = 6.dp),
                    )
                }

                // Suppress unused index warning — needed by AnimatedContent
                @Suppress("UNUSED_EXPRESSION") index
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Skip button
        OutlinedButton(
            onClick = { onEvent(QuizUiEvent.Skip) },
            enabled = !state.isAnswered,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (!state.isAnswered) MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp),
        ) {
            Text(
                text = "Skip Question",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenLoadingPreview() {
    DailyRoundsAssignmentTheme {
        QuizScreen(
            uiState = QuizUiState.Loading,
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenContentPreview() {
    val dummyQuestion = Question(
        id = 1,
        question = "What is the capital of France?",
        options = listOf("London", "Paris", "Berlin", "Rome"),
        correctOptionIndex = 1
    )
    val dummyState = QuizUiState.Content(
        currentIndex = 2,
        totalQuestions = 10,
        currentQuestion = dummyQuestion,
        selectedOptionIndex = null,
        isAnswered = false,
        currentStreak = 4,
        longestStreak = 4,
        correctCount = 2,
        skippedCount = 0
    )
    DailyRoundsAssignmentTheme {
        QuizScreen(
            uiState = dummyState,
            onEvent = {}
        )
    }
}
