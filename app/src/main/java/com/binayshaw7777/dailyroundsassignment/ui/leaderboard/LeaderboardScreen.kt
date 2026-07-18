package com.binayshaw7777.dailyroundsassignment.ui.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.data.model.QuizResult
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.theme.DarkBackground
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrectBg
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrectBgDk
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrong
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrongBg
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrongBgDk
import com.binayshaw7777.dailyroundsassignment.ui.theme.StreakActive
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
        ClearHistoryDialog(
            onDismiss = { showClearDialog = false },
            onConfirm = {
                onEvent(LeaderboardUiEvent.ClearHistory)
                showClearDialog = false
            }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // 1. Stats row
        item {
            LeaderboardStatsCards(
                uiState = uiState,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // 2. Section Header
        if (uiState.results.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent History",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedButton(
                        onClick = { showClearDialog = true },
                        shape = SquircleShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = OptionWrong
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            modifier = Modifier.size(14.dp),
                            tint = OptionWrong
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Clear All",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = OptionWrong
                        )
                    }
                }
            }
        }

        // 3. Results list or Empty State
        if (uiState.results.isEmpty()) {
            item {
                LeaderboardEmptyState(
                    modifier = Modifier.fillParentMaxHeight(0.7f)
                )
            }
        } else {
            items(uiState.results) { result ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ResultItem(
                        result = result,
                        onDelete = { onEvent(LeaderboardUiEvent.DeleteResult(result.id)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ClearHistoryDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = SquircleShape(12.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Clear History") },
        text = { Text("Clear all history? This cannot be undone.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Clear", color = OptionWrong) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
private fun LeaderboardStatsCards(
    uiState: LeaderboardUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val totalGames = uiState.results.size
        val winRate = if (totalGames > 0) (uiState.totalWins * 100) / totalGames else 0

        SummaryCard(
            label = "Wins",
            value = uiState.totalWins.toString(),
            accent = OptionCorrect,
            subtext = "$winRate% Win Rate",
            icon = {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = OptionCorrect, shape = CircleShape)
                )
            },
            modifier = Modifier.weight(1f)
        )

        SummaryCard(
            label = "Losses",
            value = uiState.totalLosses.toString(),
            accent = OptionWrong,
            subtext = "Out of $totalGames games",
            icon = {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = OptionWrong, shape = CircleShape)
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    accent: Color,
    subtext: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = SquircleShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                icon()
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = accent
            )
            if (subtext.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtext,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ResultItem(
    result: QuizResult,
    onDelete: () -> Unit,
) {
    val accentColor = if (result.isWin) OptionCorrect else OptionWrong
    val dateFormatter = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    val formattedDate = remember(result.timestamp) { dateFormatter.format(Date(result.timestamp)) }
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    val badgeBg = if (result.isWin) {
        if (isDark) OptionCorrectBgDk else OptionCorrectBg
    } else {
        if (isDark) OptionWrongBgDk else OptionWrongBg
    }
    val badgeBorder = if (result.isWin) OptionCorrect.copy(alpha = 0.3f) else OptionWrong.copy(alpha = 0.3f)
    val badgeText = if (result.isWin) "WIN" else "LOSS"

    Surface(
        shape = SquircleShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Status Badge
            Surface(
                shape = SquircleShape(8.dp),
                color = badgeBg,
                border = BorderStroke(1.dp, badgeBorder),
                modifier = Modifier.width(54.dp)
            ) {
                Text(
                    text = badgeText,
                    color = accentColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // 2. Main details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${result.correctCount}/${result.totalQuestions} Correct",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (result.longestStreak >= 3) {
                        StreakBadge(streak = result.longestStreak)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$formattedDate • ${result.accuracy}% accuracy",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 3. Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun StreakBadge(streak: Int) {
    Surface(
        shape = SquircleShape(6.dp),
        color = StreakActive.copy(alpha = 0.15f),
        border = BorderStroke(0.5.dp, StreakActive.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "🔥",
                fontSize = 10.sp
            )
            Text(
                text = "$streak Streak",
                color = StreakActive,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LeaderboardEmptyState(
    modifier: Modifier = Modifier,
) {
    val cardBg = MaterialTheme.colorScheme.surface
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = SquircleShape(12.dp),
            color = cardBg,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Empty",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No history yet",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Complete a quiz to see your results here.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
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
