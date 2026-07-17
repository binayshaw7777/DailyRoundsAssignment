package com.binayshaw7777.dailyroundsassignment.ui.quizstart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
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
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect
import com.binayshaw7777.dailyroundsassignment.ui.theme.TextPrimary

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
        contentAlpha.animateTo(1f, animationSpec = tween(500))
        buttonScale.animateTo(1f, animationSpec = tween(300))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(contentAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "🧠", fontSize = 72.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Daily Quiz",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Test your knowledge, build your streak,\nand dominate the leaderboard.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (totalGames > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                        .padding(vertical = 20.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    StatItem(label = "Played", value = totalGames.toString())
                    VerticalDivider()
                    StatItem(label = "Wins", value = totalWins.toString())
                    VerticalDivider()
                    StatItem(label = "Best Streak", value = "$bestStreak 🔥")
                }

                Spacer(modifier = Modifier.height(40.dp))
            } else {
                Text(
                    text = "No games played yet.\nStart your first quiz now!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        Button(
            onClick = onStartQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .scale(buttonScale.value),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OptionCorrect),
        ) {
            Text(
                text = if (totalGames > 0) "Play Again" else "Start Quiz",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
        }
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizStartScreenPreview() {
    DailyRoundsAssignmentTheme {
        QuizStartScreen(
            totalGames = 12,
            totalWins = 8,
            bestStreak = 5,
            onStartQuiz = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizStartScreenFirstTimePreview() {
    DailyRoundsAssignmentTheme {
        QuizStartScreen(
            totalGames = 0,
            totalWins = 0,
            bestStreak = 0,
            onStartQuiz = {},
        )
    }
}
