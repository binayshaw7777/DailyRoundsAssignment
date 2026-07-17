package com.binayshaw7777.dailyroundsassignment.ui.results

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
import sv.lib.squircleshape.SquircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

import androidx.compose.ui.tooling.preview.Preview
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme

@Composable
fun ResultsScreen(
    uiState: ResultsUiState,
    onEvent: (ResultsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding(),
        ) {
            // Top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                IconButton(
                    onClick = { onEvent(ResultsUiEvent.RestartQuiz) },
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Restart quiz",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                Text(
                    text = "Quiz Results",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Congratulations!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "You've completed the quiz.\nHere's your performance summary:",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                )

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    StatCard(
                        label = "Correct Answers",
                        value = "${uiState.correctCount}/${uiState.totalQuestions}",
                        modifier = Modifier.weight(1f),
                    )
                    StatCard(
                        label = "Highest Streak",
                        value = "${uiState.longestStreak}",
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    StatCard(
                        label = "Skipped",
                        value = "${uiState.skippedCount}",
                        modifier = Modifier.weight(1f),
                    )
                    StatCard(
                        label = "Accuracy",
                        value = "${uiState.accuracy}%",
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Button(
                onClick = { onEvent(ResultsUiEvent.RestartQuiz) },
                shape = SquircleShape(percent = 50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = "Restart Quiz",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Konfetti celebration overlay
        val parties = remember {
            val colors = listOf(
                0xFFFCDD45.toInt(), 0xFFFF6B6B.toInt(), 0xFF45B7D1.toInt(),
                0xFF96CEB4.toInt(), 0xFFFF9F43.toInt(), 0xFFA29BFE.toInt(),
            )
            listOf(
                Party(
                    colors = colors,
                    shapes = listOf(Shape.Circle, Shape.Square),
                    size = listOf(Size.SMALL, Size.MEDIUM),
                    position = Position.Relative(0.0, 0.3),
                    angle = 0,
                    spread = 40,
                    emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(40),
                ),
                Party(
                    colors = colors,
                    shapes = listOf(Shape.Circle, Shape.Square),
                    size = listOf(Size.SMALL, Size.MEDIUM),
                    position = Position.Relative(1.0, 0.3),
                    angle = 180,
                    spread = 40,
                    emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(40),
                ),
            )
        }
        KonfettiView(
            parties = parties,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenPreview() {
    DailyRoundsAssignmentTheme {
        ResultsScreen(
            uiState = ResultsUiState(
                correctCount = 8,
                totalQuestions = 10,
                longestStreak = 6,
                skippedCount = 1
            ),
            onEvent = {}
        )
    }
}
