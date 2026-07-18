package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrong
import sv.lib.squircleshape.SquircleShape
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Leaderboard screen displaying quiz history as a scrollable list with summary cards.
 *
 * Shows a **Wins** / **Losses** summary row at the top with a delete button that
 * triggers a confirmation dialog. Each [QuizResult] is rendered as a card with
 * a colored left border (green for wins, red for losses), date, accuracy, score,
 * and streak.
 *
 * @param uiState Current [LeaderboardUiState] with the full result list.
 * @param onEvent Callback for user interactions (clear history).
 * @param modifier [Modifier] applied to the root Column.
 */
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
            text = { Text("Clear all history? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(LeaderboardUiEvent.ClearHistory)
                    showClearDialog = false
                }) { Text("Clear", color = OptionWrong) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
            },
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Summary row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SummaryCard(label = "Wins", value = uiState.totalWins.toString(), accent = OptionCorrect, modifier = Modifier.weight(1f))
            SummaryCard(label = "Losses", value = uiState.totalLosses.toString(), accent = OptionWrong, modifier = Modifier.weight(1f))
            IconButton(onClick = { showClearDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        if (uiState.results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No quiz history yet.\nComplete a quiz to see results here.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(uiState.results) { result -> ResultItem(result = result) }
                item { Spacer(modifier = Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, accent: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = SquircleShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = accent)
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ResultItem(result: QuizResult) {
    val accentColor = if (result.isWin) OptionCorrect else OptionWrong
    val dateFormatter = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    val formattedDate = remember(result.timestamp) { dateFormatter.format(Date(result.timestamp)) }

    Surface(
        shape = SquircleShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(72.dp)
                    .background(color = accentColor, shape = SquircleShape(0.dp)),
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(text = formattedDate, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(text = "${result.accuracy}% accuracy", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "${result.correctCount}/${result.totalQuestions}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = accentColor)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(text = "🔥 ${result.longestStreak}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                    QuizResult(id = 2, correctCount = 4, totalQuestions = 10, longestStreak = 2, skippedCount = 2, timestamp = 1717913600000L, isWin = false),
                ),
                isLoading = false,
            ),
            onEvent = {},
        )
    }
}
