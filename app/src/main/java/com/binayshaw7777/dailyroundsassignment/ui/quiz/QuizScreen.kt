package com.binayshaw7777.dailyroundsassignment.ui.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import sv.lib.squircleshape.SquircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
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
import com.binayshaw7777.dailyroundsassignment.ui.components.StreakProgressRing

import androidx.compose.ui.tooling.preview.Preview
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.theme.DarkBackground
import com.binayshaw7777.dailyroundsassignment.data.model.Question

/**
 * Main quiz screen that renders questions, options, progress, and streak feedback.
 *
 * Handles three UI phases via [QuizUiState]:
 * - [QuizUiState.Loading] — centered [CircularProgressIndicator].
 * - [QuizUiState.Error] — centered error message.
 * - [QuizUiState.Content] — full quiz UI with animated question transitions,
 *   option buttons, streak ring, progress bar, and skip button.
 *
 * Supports **swipe-to-skip**: a left swipe of >200px triggers [QuizUiEvent.Skip]
 * when the current question hasn't been answered yet.
 *
 * @param uiState Current [QuizUiState] driving the UI.
 * @param onEvent Callback for user interactions (select option, skip, consume haptic, etc.).
 * @param modifier [Modifier] applied to the root Box.
 */
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
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 88.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            QuizTopBar(currentStreak = state.currentStreak)

            QuizProgressSection(
                currentIndex = state.currentIndex,
                totalQuestions = state.totalQuestions,
                progress = state.progress,
            )

            AnimatedContent(
                targetState = state.currentQuestion,
                transitionSpec = {
                    (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it } + fadeOut())
                },
                label = "questionTransition",
            ) { question ->
                QuizQuestionSection(
                    question = question,
                    isAnswered = state.isAnswered,
                    selectedOptionIndex = state.selectedOptionIndex,
                    onEvent = onEvent,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        QuizSkipButton(
            enabled = !state.isAnswered,
            onClick = { onEvent(QuizUiEvent.Skip) },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun QuizQuestionSection(
    question: Question,
    isAnswered: Boolean,
    selectedOptionIndex: Int?,
    onEvent: (QuizUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        QuestionCard(questionText = question.question)

        Spacer(modifier = Modifier.height(24.dp))

        question.options.forEachIndexed { optionIndex, optionText ->
            OptionButton(
                text = optionText,
                optionIndex = optionIndex,
                isAnswered = isAnswered,
                selectedOptionIndex = selectedOptionIndex,
                correctOptionIndex = question.correctOptionIndex,
                onOptionSelected = { onEvent(QuizUiEvent.SelectOption(it)) },
                modifier = Modifier.padding(vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun QuizSkipButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = SquircleShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(56.dp),
    ) {
        Text(
            text = "Skip Question",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun QuizTopBar(
    currentStreak: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Quiz",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        StreakProgressRing(
            currentStreak = currentStreak,
            baseSize = 56.dp,
            sizePerStreak = 2.dp,
        )
    }
}

@Composable
private fun QuizProgressSection(
    currentIndex: Int,
    totalQuestions: Int,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        Text(
            text = "Question ${currentIndex + 1} of $totalQuestions",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            drawStopIndicator = {},
        )
    }
}

@Composable
private fun QuestionCard(
    questionText: String,
    modifier: Modifier = Modifier,
) {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground
    val questionGrad = if (isDark) {
        Brush.linearGradient(
            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0))
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = questionGrad, shape = SquircleShape(14.dp)),
        shape = SquircleShape(14.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
    ) {
        Text(
            text = questionText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 28.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
        )
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
