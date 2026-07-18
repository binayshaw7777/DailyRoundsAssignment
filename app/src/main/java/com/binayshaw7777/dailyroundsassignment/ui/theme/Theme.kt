package com.binayshaw7777.dailyroundsassignment.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkMuted,
    onPrimaryContainer = DarkOnSurface,
    secondary = DarkMuted,
    onSecondary = DarkOnSurface,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkMuted,
    onSurfaceVariant = DarkMutedFg,
    outline = DarkBorder,
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightMuted,
    onPrimaryContainer = LightOnSurface,
    secondary = LightMuted,
    onSecondary = LightOnSurface,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightMuted,
    onSurfaceVariant = LightMutedFg,
    outline = LightBorder,
)

/**
 * App-wide Material 3 theme composable.
 *
 * Applies either [DarkColorScheme] or [LightColorScheme] based on the
 * [darkTheme] parameter and wraps [content] with [MaterialTheme].
 *
 * @param darkTheme When `true` (default), uses the dark color palette.
 * @param content Root composable tree to receive the theme.
 *
 * Usage:
 * ```kotlin
 * @Composable
 * fun App() {
 *     DailyRoundsAssignmentTheme(darkTheme = true) {
 *         Scaffold { padding ->
 *             Box(Modifier.padding(padding)) {
 *                 Text("Hello", color = MaterialTheme.colorScheme.primary)
 *             }
 *         }
 *     }
 * }
 * ```
 */
@Composable
fun DailyRoundsAssignmentTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
