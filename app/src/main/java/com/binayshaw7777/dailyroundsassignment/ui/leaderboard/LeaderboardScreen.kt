package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrong
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme

@Composable
fun LeaderboardScreen(
    uiState: LeaderboardUiState,
    onEvent: (LeaderboardUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear History") },
            text = { Text("Clear all history? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(LeaderboardUiEvent.ClearHistory)
                    showClearDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("No")
                }
            },
        )
    }

    Column(
        modifier = modifier
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
            Text(
                text = "Leaderboard",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
            )
            IconButton(
                onClick = { showClearDialog = true },
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear history",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        // Summary row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SummaryCard(
                label = "Wins",
                emoji = "🏆",
                value = uiState.totalWins.toString(),
                modifier = Modifier.weight(1f),
            )
            SummaryCard(
                label = "Losses",
                emoji = "❌",
                value = uiState.totalLosses.toString(),
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "🏆\n\nNo quiz history yet.\nComplete a quiz to see results here.",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(uiState.results) { result ->
                    ResultItem(result = result)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    label: String,
    emoji: String,
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
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = label,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun ResultItem(result: QuizResult) {
    val accentColor = if (result.isWin) OptionCorrect else OptionWrong
    val dateFormatter = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    val formattedDate = remember(result.timestamp) {
        dateFormatter.format(Date(result.timestamp))
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Colored left bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(80.dp)
                    .background(
                        color = accentColor,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Left: date + accuracy
                Column {
                    Text(
                        text = formattedDate,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${result.accuracy}% accuracy",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                // Right: score + streak
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${result.correctCount}/${result.totalQuestions}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🔥 ${result.longestStreak}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    DailyRoundsAssignmentTheme {
        LeaderboardScreen(
            uiState = LeaderboardUiState(
                results = listOf(
                    QuizResult(id = 1, correctCount = 8, totalQuestions = 10, longestStreak = 5, skippedCount = 1, timestamp = 1718000000000L, isWin = true),
                    QuizResult(id = 2, correctCount = 4, totalQuestions = 10, longestStreak = 2, skippedCount = 2, timestamp = 1717913600000L, isWin = false)
                ),
                isLoading = false
            ),
            onEvent = {}
        )
    }
}
