package com.binayshaw7777.dailyroundsassignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizUiState
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizViewModel
import com.binayshaw7777.dailyroundsassignment.ui.theme.Background
import com.binayshaw7777.dailyroundsassignment.ui.theme.RestartButton
import com.binayshaw7777.dailyroundsassignment.ui.theme.RestartButtonText
import com.binayshaw7777.dailyroundsassignment.ui.theme.Surface
import com.binayshaw7777.dailyroundsassignment.ui.theme.TextPrimary
import com.binayshaw7777.dailyroundsassignment.ui.theme.TextSecondary

@Composable
fun ResultsScreen(
    viewModel: QuizViewModel,
    onRestartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResultsContent(
        state = state,
        onRestartQuiz = onRestartQuiz,
        modifier = modifier
    )
}

@Composable
fun ResultsContent(
    state: QuizUiState,
    onRestartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding()
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            IconButton(
                onClick = onRestartQuiz,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Restart quiz",
                    tint = TextPrimary
                )
            }
            Text(
                text = "Quiz Results",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Congratulations text
            Text(
                text = "Congratulations!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "You've completed the quiz.\nHere's your performance summary:",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Stat cards row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = "Correct Answers",
                    value = "${state.correctCount}/${state.totalQuestions}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Highest Streak",
                    value = "${state.longestStreak}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Skipped count card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = "Skipped",
                    value = "${state.skippedCount}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Accuracy",
                    value = if (state.totalQuestions > 0) {
                        "${(state.correctCount * 100 / state.totalQuestions)}%"
                    } else "0%",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Restart button
        Button(
            onClick = onRestartQuiz,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = RestartButton,
                contentColor = RestartButtonText
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Restart Quiz",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = RestartButtonText
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
