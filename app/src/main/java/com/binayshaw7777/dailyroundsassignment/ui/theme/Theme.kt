package com.binayshaw7777.dailyroundsassignment.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TextPrimary,
    onPrimary = Background,
    secondary = TextSecondary,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = OptionDefault,
    onSurfaceVariant = TextPrimary,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5C6BC0),
    onPrimary = Color.White,
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1A24),
    onSurface = Color(0xFF1A1A24),
    secondary = Color(0xFF757575),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF1A1A24),
)

@Composable
fun DailyRoundsAssignmentTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
