package com.binayshaw7777.dailyroundsassignment.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.binayshaw7777.dailyroundsassignment.ui.components.OptionButton
import com.binayshaw7777.dailyroundsassignment.ui.components.StreakFlames
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizUiState
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizViewModel
import com.binayshaw7777.dailyroundsassignment.ui.theme.Background
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionDefault
import com.binayshaw7777.dailyroundsassignment.ui.theme.ProgressTrack
import com.binayshaw7777.dailyroundsassignment.ui.theme.TextPrimary
import com.binayshaw7777.dailyroundsassignment.ui.theme.TextSecondary

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    QuizContent(
        state = state,
        onOptionSelected = viewModel::selectOption,
        onSkip = viewModel::skipQuestion,
        modifier = modifier
    )
}

@Composable
fun QuizContent(
    state: QuizUiState,
    onOptionSelected: (Int) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    var swipeDeltaX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding()
            .pointerInput(state.isAnswered) {
                detectHorizontalDragGestures(
                    onDragStart = { swipeDeltaX = 0f },
                    onDragEnd = {
                        if (swipeDeltaX < -200f && !state.isAnswered) onSkip()
                        swipeDeltaX = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        swipeDeltaX += dragAmount
                    }
                )
            }
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    color = TextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.questions.isEmpty() -> {
                Text(
                    text = "No questions available.",
                    color = TextSecondary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    // Top bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Quiz",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Streak flames row
                    StreakFlames(
                        currentStreak = state.currentStreak,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    )

                    // Streak badge
                    AnimatedVisibility(
                        visible = state.streakActive,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "🔥 ${state.currentStreak} questions streak achieved!!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 4.dp)
                        )
                    }

                    // Progress row
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Question ${state.currentIndex + 1} of ${state.totalQuestions}",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LinearProgressIndicator(
                            progress = { state.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp),
                            color = TextPrimary,
                            trackColor = ProgressTrack
                        )
                    }

                    // Animated question + options
                    AnimatedContent(
                        targetState = state.currentIndex,
                        transitionSpec = {
                            (slideInHorizontally { it } + fadeIn()) togetherWith
                                    (slideOutHorizontally { -it } + fadeOut())
                        },
                        label = "questionTransition"
                    ) { index ->
                        val question = state.questions.getOrNull(index)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Question text
                            if (question != null) {
                                Text(
                                    text = question.question,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    lineHeight = 32.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 24.dp)
                                )

                                // Options
                                question.options.forEachIndexed { optionIndex, optionText ->
                                    OptionButton(
                                        text = optionText,
                                        optionIndex = optionIndex,
                                        state = state,
                                        onOptionSelected = onOptionSelected,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Skip button
                    Button(
                        onClick = onSkip,
                        enabled = !state.isAnswered,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OptionDefault,
                            disabledContainerColor = OptionDefault.copy(alpha = 0.5f),
                            contentColor = TextPrimary,
                            disabledContentColor = TextSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "Skip",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
