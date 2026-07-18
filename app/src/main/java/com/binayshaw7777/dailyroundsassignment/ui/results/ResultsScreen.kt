package com.binayshaw7777.dailyroundsassignment.ui.results

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import sv.lib.squircleshape.SquircleShape
import java.util.concurrent.TimeUnit

/**
 * Post-quiz results screen that displays a summary card grid and confetti animation.
 *
 * Shows a celebration emoji and message based on accuracy (>= 60% = "Well done!"),
 * four stat cards (Correct, Streak, Skipped, Accuracy), and a "Play Again" button.
 * A [KonfettiView] overlay fires confetti from both sides of the screen.
 *
 * @param uiState Current [ResultsUiState] with the quiz results data.
 * @param onEvent Callback for user interactions (restart quiz).
 * @param modifier [Modifier] applied to the root Box.
 */
@Composable
fun ResultsScreen(
    uiState: ResultsUiState,
    onEvent: (ResultsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            ResultsHeader(onRestartQuiz = { onEvent(ResultsUiEvent.RestartQuiz) })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                ResultsCelebration(accuracy = uiState.accuracy)

                Spacer(modifier = Modifier.height(32.dp))

                ResultsStatsGrid(uiState = uiState)
            }

            Button(
                onClick = { onEvent(ResultsUiEvent.RestartQuiz) },
                shape = SquircleShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 20.dp),
            ) {
                Text("Play Again", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        KonfettiView(parties = remember { konfettiParties() }, modifier = Modifier.fillMaxSize())
    }
}

private fun konfettiParties(): List<Party> {
    val colors = listOf(0xFFFCDD45.toInt(), 0xFFFF6B6B.toInt(), 0xFF45B7D1.toInt(), 0xFF96CEB4.toInt())
    return listOf(
        Party(colors = colors, shapes = listOf(Shape.Circle, Shape.Square), size = listOf(Size.SMALL, Size.MEDIUM), position = Position.Relative(0.0, 0.3), angle = 0, spread = 40, emitter = Emitter(2, TimeUnit.SECONDS).perSecond(40)),
        Party(colors = colors, shapes = listOf(Shape.Circle, Shape.Square), size = listOf(Size.SMALL, Size.MEDIUM), position = Position.Relative(1.0, 0.3), angle = 180, spread = 40, emitter = Emitter(2, TimeUnit.SECONDS).perSecond(40)),
    )
}

@Composable
private fun ResultsHeader(
    onRestartQuiz: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth().height(56.dp)) {
        IconButton(
            onClick = onRestartQuiz,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onBackground)
        }
        Text(
            text = "Results",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun ResultsCelebration(
    accuracy: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (accuracy >= 60) "🎉" else "💪", fontSize = 56.sp, enableAutoSize = false)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = if (accuracy >= 60) "Well done!" else "Keep going!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Here's your summary",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ResultsStatsGrid(
    uiState: ResultsUiState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatCard(label = "Correct", value = "${uiState.correctCount}/${uiState.totalQuestions}", modifier = Modifier.weight(1f))
            StatCard(label = "Streak", value = "${uiState.longestStreak} 🔥", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatCard(label = "Skipped", value = "${uiState.skippedCount}", modifier = Modifier.weight(1f))
            StatCard(label = "Accuracy", value = "${uiState.accuracy}%", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = SquircleShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            uiState = ResultsUiState(correctCount = 8, totalQuestions = 10, longestStreak = 6, skippedCount = 1),
            onEvent = {},
        )
    }
}
