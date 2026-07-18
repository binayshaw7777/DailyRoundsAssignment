package com.binayshaw7777.dailyroundsassignment.ui.quizstart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import kotlinx.coroutines.launch
import sv.lib.squircleshape.SquircleShape

/**
 * Pre-quiz landing screen that displays lifetime stats and a "Start Quiz" / "Play Again" button.
 *
 * Shows a summary card with **Played**, **Wins**, and **Best Streak** when the user has
 * completed at least one quiz. For first-time users, a friendly "No games yet" message
 * is shown instead.
 *
 * Content fades in and the button springs into view via [Animatable] animations on
 * first composition.
 *
 * @param totalGames Lifetime number of completed quizzes.
 * @param totalWins Lifetime number of winning quizzes.
 * @param bestStreak The user's all-time highest streak.
 * @param onStartQuiz Callback invoked when the user taps the primary action button.
 * @param modifier [Modifier] applied to the root Column.
 */
@Composable
fun QuizStartScreen(
    totalGames: Int,
    totalWins: Int,
    bestStreak: Int,
    onStartQuiz: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentAlpha = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        launch { contentAlpha.animateTo(1f, animationSpec = tween(400)) }
        launch {
            buttonScale.animateTo(
                1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow,
                ),
            )
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().alpha(contentAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QuizStartHeader()

            Spacer(modifier = Modifier.height(32.dp))

            QuizStartStats(
                totalGames = totalGames,
                totalWins = totalWins,
                bestStreak = bestStreak,
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        Button(
            onClick = onStartQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .scale(buttonScale.value),
            shape = SquircleShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text(
                text = if (totalGames > 0) "Play Again" else "Start Quiz",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun QuizStartHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "🧠", fontSize = 56.sp, enableAutoSize = false)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Daily Quiz",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Test your knowledge and build your streak.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp,
        )
    }
}

@Composable
private fun QuizStartStats(
    totalGames: Int,
    totalWins: Int,
    bestStreak: Int,
    modifier: Modifier = Modifier,
) {
    if (totalGames > 0) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = SquircleShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatItem(label = "Played", value = totalGames.toString())
                VerticalDivider()
                StatItem(label = "Wins", value = totalWins.toString())
                VerticalDivider()
                StatItem(label = "Best Streak", value = "$bestStreak 🔥")
            }
        }
    } else {
        Text(
            text = "No games yet. Start your first quiz!",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = modifier,
        )
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(MaterialTheme.colorScheme.outline),
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizStartScreenPreview() {
    DailyRoundsAssignmentTheme {
        QuizStartScreen(totalGames = 12, totalWins = 8, bestStreak = 5, onStartQuiz = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizStartScreenFirstPreview() {
    DailyRoundsAssignmentTheme {
        QuizStartScreen(totalGames = 0, totalWins = 0, bestStreak = 0, onStartQuiz = {})
    }
}
