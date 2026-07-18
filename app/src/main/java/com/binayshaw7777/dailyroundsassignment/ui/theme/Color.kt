package com.binayshaw7777.dailyroundsassignment.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Light theme color palette based on shadcn zinc.
 *
 * Usage:
 * ```kotlin
 * Box(modifier = Modifier.background(LightBackground))
 * Text("Hello", color = LightOnBackground)
 * ```
 */
// shadcn zinc — light
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFAFAFA)
val LightBorder = Color(0xFFE4E4E7)
val LightPrimary = Color(0xFF18181B)
val LightOnPrimary = Color(0xFFFAFAFA)
val LightMuted = Color(0xFFF4F4F5)
val LightMutedFg = Color(0xFF71717A)
val LightOnBackground = Color(0xFF09090B)
val LightOnSurface = Color(0xFF09090B)

/**
 * Dark theme color palette based on shadcn zinc.
 *
 * Usage:
 * ```kotlin
 * Box(modifier = Modifier.background(DarkBackground))
 * Text("Hello", color = DarkOnBackground)
 * ```
 */
// shadcn zinc — dark
val DarkBackground = Color(0xFF09090B)
val DarkSurface = Color(0xFF18181B)
val DarkBorder = Color(0xFF27272A)
val DarkPrimary = Color(0xFFFAFAFA)
val DarkOnPrimary = Color(0xFF18181B)
val DarkMuted = Color(0xFF27272A)
val DarkMutedFg = Color(0xFFA1A1AA)
val DarkOnBackground = Color(0xFFFAFAFA)
val DarkOnSurface = Color(0xFFFAFAFA)

/**
 * Semantic colors for quiz answer states.
 *
 * Usage:
 * ```kotlin
 * // Correct answer highlight
 * Box(modifier = Modifier.background(OptionCorrectBg))
 * Text("Correct!", color = OptionCorrect)
 *
 * // Wrong answer highlight
 * Box(modifier = Modifier.background(OptionWrongBg))
 * Text("Wrong!", color = OptionWrong)
 * ```
 */
// Semantic — quiz states
val OptionCorrect = Color(0xFF22C55E)
val OptionCorrectBg = Color(0xFFF0FDF4)
val OptionCorrectBgDk = Color(0xFF052E16)
val OptionWrong = Color(0xFFEF4444)
val OptionWrongBg = Color(0xFFFEF2F2)
val OptionWrongBgDk = Color(0xFF450A0A)

/**
 * Legacy aliases kept for backward compatibility with scattered usages.
 *
 * ```kotlin
 * Text("Title", color = TextPrimary)
 * Icon(tint = if (active) StreakActive else StreakInactive)
 * ```
 */
// Legacy aliases kept for scattered usages
val TextPrimary = Color(0xFFFAFAFA)
val StreakActive = Color(0xFFF97316)
val StreakInactive = Color(0xFF3F3F46)
