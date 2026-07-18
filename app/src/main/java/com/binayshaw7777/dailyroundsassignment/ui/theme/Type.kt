package com.binayshaw7777.dailyroundsassignment.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.R

/**
 * Custom [FontFamily] built from the DM Sans font resources.
 *
 * Includes Normal, Medium, Bold, SemiBold, and Light weights,
 * plus italic variants for Normal, Medium, and Bold.
 *
 * Usage:
 * ```kotlin
 * Text("Hello", style = TextStyle(fontFamily = DMSans, fontWeight = FontWeight.Bold))
 * ```
 */
val DMSans = FontFamily(
    Font(resId = R.font.dm_sans, weight = FontWeight.Normal),
    Font(resId = R.font.dm_sans, weight = FontWeight.Medium),
    Font(resId = R.font.dm_sans, weight = FontWeight.Bold),
    Font(resId = R.font.dm_sans, weight = FontWeight.SemiBold),
    Font(resId = R.font.dm_sans, weight = FontWeight.Light),
    Font(resId = R.font.dm_sans_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(resId = R.font.dm_sans_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(resId = R.font.dm_sans_italic, weight = FontWeight.Bold, style = FontStyle.Italic)
)

/**
 * Material 3 [Typography] scale using [DMSans] throughout.
 *
 * Overrides:
 * - [displaySmall] – 28.sp bold (screen headings)
 * - [titleLarge] – 18.sp medium (section titles)
 * - [titleMedium] – 22.sp bold (card headings)
 * - [bodyLarge] – 16.sp medium (primary body text)
 * - [bodyMedium] – 14.sp normal (secondary body text)
 *
 * Usage:
 * ```kotlin
 * Text("Heading", style = MaterialTheme.typography.displaySmall)
 * Text("Body", style = MaterialTheme.typography.bodyLarge)
 * ```
 */
val Typography = Typography(
    displaySmall = TextStyle(
        fontFamily = DMSans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DMSans,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = DMSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = DMSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DMSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
)
