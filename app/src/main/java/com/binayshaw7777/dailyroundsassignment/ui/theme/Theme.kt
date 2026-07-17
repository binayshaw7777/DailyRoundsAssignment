package com.binayshaw7777.dailyroundsassignment.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary = TextPrimary,
    onPrimary = Background,
    secondary = TextSecondary,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = OptionDefault,
    onSurfaceVariant = TextPrimary
)

@Composable
fun DailyRoundsAssignmentTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
