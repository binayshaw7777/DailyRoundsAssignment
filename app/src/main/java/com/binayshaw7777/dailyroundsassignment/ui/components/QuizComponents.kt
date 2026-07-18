package com.binayshaw7777.dailyroundsassignment.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DMSans
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.theme.DarkBackground
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrectBg
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrectBgDk
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrong
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrongBg
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrongBgDk
import sv.lib.squircleshape.SquircleShape

private val optionLabels = listOf("A", "B", "C", "D")

/**
 * Animated quiz option button with selection feedback and correct/wrong state styling.
 *
 * Displays a labeled pill (A/B/C/D) alongside the option text. Colors, borders,
 * and the label background all animate to reflect the current state:
 * - **Default** (unanswered): neutral theme colors.
 * - **Selected + Correct**: green highlight.
 * - **Selected + Wrong**: red highlight.
 * - **Not selected + Correct** (after answer): green highlight to reveal the right answer.
 * - **Unselected + Wrong / unanswered**: text fades to 40% alpha.
 *
 * The button uses [SquircleShape] for a modern squircle aesthetic and is disabled
 * after the user has answered.
 *
 * @param text The option's display text.
 * @param optionIndex Zero-based index of this option (determines the A/B/C/D label).
 * @param isAnswered Whether the user has already answered this question.
 * @param selectedOptionIndex The index of the option the user selected, or `null` if none.
 * @param correctOptionIndex The index of the correct answer.
 * @param onOptionSelected Callback invoked with the [optionIndex] when the user taps.
 * @param modifier [Modifier] applied to the button root.
 *
 * ```kotlin
 * // Default (unanswered) state
 * OptionButton(
 *     text = "Paris",
 *     optionIndex = 1,
 *     isAnswered = false,
 *     selectedOptionIndex = null,
 *     correctOptionIndex = 1,
 *     onOptionSelected = { index -> /* handle selection */ },
 * )
 *
 * // After answering correctly
 * OptionButton(
 *     text = "Paris",
 *     optionIndex = 1,
 *     isAnswered = true,
 *     selectedOptionIndex = 1,
 *     correctOptionIndex = 1,
 *     onOptionSelected = {},
 * )
 * ```
 */
@Composable
fun OptionButton(
    text: String,
    optionIndex: Int,
    isAnswered: Boolean,
    selectedOptionIndex: Int?,
    correctOptionIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val label = optionLabels.getOrElse(optionIndex) { "${optionIndex + 1}" }
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    val isSelected = optionIndex == selectedOptionIndex
    val isCorrect = optionIndex == correctOptionIndex

    val defaultBg = MaterialTheme.colorScheme.background
    val defaultText = MaterialTheme.colorScheme.onBackground
    val defaultBorder = MaterialTheme.colorScheme.outline

    val correctBg = if (isDark) OptionCorrectBgDk else OptionCorrectBg
    val wrongBg = if (isDark) OptionWrongBgDk else OptionWrongBg

    val targetBg = when {
        !isAnswered -> defaultBg
        isSelected && isCorrect -> correctBg
        isSelected -> wrongBg
        isCorrect -> correctBg
        else -> defaultBg
    }
    val targetBorder = when {
        !isAnswered -> defaultBorder
        isSelected && isCorrect -> OptionCorrect
        isSelected -> OptionWrong
        isCorrect -> OptionCorrect
        else -> defaultBorder
    }
    val targetText = when {
        !isAnswered -> defaultText
        isSelected && isCorrect -> OptionCorrect
        isSelected -> OptionWrong
        isCorrect -> OptionCorrect
        else -> defaultText.copy(alpha = 0.4f)
    }
    val targetLabelBg = when {
        !isAnswered -> MaterialTheme.colorScheme.surfaceVariant
        isSelected && isCorrect -> OptionCorrect.copy(alpha = 0.2f)
        isSelected -> OptionWrong.copy(alpha = 0.2f)
        isCorrect -> OptionCorrect.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val bg by animateColorAsState(targetBg, tween(350), label = "bg$optionIndex")
    val border by animateColorAsState(targetBorder, tween(350), label = "bd$optionIndex")
    val textColor by animateColorAsState(targetText, tween(350), label = "tx$optionIndex")
    val lbg by animateColorAsState(targetLabelBg, tween(350), label = "lb$optionIndex")

    Button(
        onClick = { if (!isAnswered) onOptionSelected(optionIndex) },
        enabled = !isAnswered,
        shape = SquircleShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            disabledContainerColor = bg,
            contentColor = textColor,
            disabledContentColor = textColor,
        ),
        border = BorderStroke(1.dp, border),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(lbg, SquircleShape(6.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            val isCode = text.contains("_")
            Text(
                text = text,
                fontSize = if (isCode) 13.sp else 15.sp,
                fontFamily = if (isCode) FontFamily.Monospace else DMSans,
                fontWeight = FontWeight.Medium,
                color = textColor,
                textAlign = TextAlign.Start,
            )
        }
    }
}

/**
 * A segmented ring that visually represents the user's current quiz streak.
 *
 * The ring is divided into [maxStreak] arc segments. As [currentStreak] increases,
 * segments fill with an animated color that transitions through warm tones:
 * - 1 streak: gold
 * - 2 streaks: orange
 * - 3 streaks: orange-red (glow enabled)
 * - 4+ streaks: red (glow enabled)
 *
 * The ring physically grows with each streak via a spring animation, and a
 * fire emoji in the center pulses to reinforce the streak concept. At 3+ streaks
 * a blurred glow halo appears behind the emoji.
 *
 * @param currentStreak The user's current consecutive-correct-answer count.
 * @param maxStreak Maximum number of segments in the ring. Defaults to `4`.
 * @param baseSize Base diameter of the ring before streak-based growth. Defaults to `96.dp`.
 * @param sizePerStreak Additional diameter added per streak point. Defaults to `5.dp`.
 * @param modifier [Modifier] applied to the outer container.
 *
 * ```kotlin
 * @Preview(showBackground = true)
 * @Composable
 * fun StreakRingPreview() {
 *     DailyRoundsAssignmentTheme {
 *         StreakProgressRing(
 *             currentStreak = 3,
 *             maxStreak = 4,
 *             baseSize = 56.dp,
 *             sizePerStreak = 2.dp,
 *         )
 *     }
 * }
 * ```
 */
@Composable
fun StreakProgressRing(
    currentStreak: Int,
    maxStreak: Int = 4,
    baseSize: Dp = 96.dp,
    sizePerStreak: Dp = 5.dp,
    modifier: Modifier = Modifier,
) {

    val isGlowing = currentStreak >= 3

    val progressSpec: AnimationSpec<Float> =
        if (currentStreak == 0) snap() else tween(500, easing = FastOutSlowInEasing)
    val animatedProgress by animateFloatAsState(
        targetValue = currentStreak.toFloat() / maxStreak,
        animationSpec = progressSpec,
        label = "ringProgress",
    )

    val sizeSpec: AnimationSpec<Dp> =
        if (currentStreak == 0) snap() else spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        )
    val animatedSize by animateDpAsState(
        targetValue = baseSize + sizePerStreak * currentStreak,
        animationSpec = sizeSpec,
        label = "ringSize",
    )

    val arcColor by animateColorAsState(
        targetValue = when (currentStreak) {
            1 -> Color(0xFFFFD700)
            2 -> Color(0xFFFF8C00)
            3 -> Color(0xFFFF4500)
            else -> if (currentStreak >= 4) Color(0xFFFF1744) else Color(0xFFFFD700)
        },
        animationSpec = tween(400),
        label = "arcColor",
    )

    Box(
        modifier = modifier.size(animatedSize),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.minDimension * 0.10f
            val gap = 12f
            val segmentAngle = 360f / maxStreak
            val segmentSweep = segmentAngle - gap

            repeat(maxStreak) { i ->
                val segStart = -90f + i * segmentAngle + gap / 2f
                val segProgress = (animatedProgress * maxStreak - i).coerceIn(0f, 1f)

                drawArc(
                    color = Color.Gray.copy(alpha = 0.15f),
                    startAngle = segStart,
                    sweepAngle = segmentSweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                if (segProgress > 0f) {
                    drawArc(
                        color = arcColor,
                        startAngle = segStart,
                        sweepAngle = segmentSweep * segProgress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.graphicsLayer { clip = false },
        ) {
            if (isGlowing) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .blur(12.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .background(Color(0xFFFF4500).copy(alpha = 0.2f), CircleShape),
                )
            }
            Text(
                text = "🔥",
                fontSize = (animatedSize.value * 0.30f).sp,
                enableAutoSize = false,
            )
        }
    }
}

/**
 * A simple row of four flame emojis that light up sequentially with the streak.
 *
 * Active flames are displayed at full size and full opacity, while inactive flames
 * are smaller and faded to 30% gray. This is a lighter-weight alternative to
 * [StreakProgressRing] suitable for compact layouts.
 *
 * @param currentStreak Number of consecutive correct answers (0–4).
 * @param modifier [Modifier] applied to the root [Row].
 *
 * ```kotlin
 * @Preview(showBackground = true)
 * @Composable
 * fun StreakFlamesPreview() {
 *     DailyRoundsAssignmentTheme {
 *         StreakFlames(currentStreak = 2)
 *     }
 * }
 * ```
 */
@Composable
fun StreakFlames(
    currentStreak: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(4) { index ->
            val isActive = currentStreak > index
            Text(
                text = "🔥",
                fontSize = if (isActive) 26.sp else 20.sp,
                color = if (isActive) Color.Unspecified else Color.Gray.copy(alpha = 0.3f),
                enableAutoSize = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionButtonPreview() {
    DailyRoundsAssignmentTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OptionButton("Default Option", 0, false, null, 1, {})
            OptionButton("Correct Option", 0, true, 0, 0, {})
            OptionButton("Wrong Option", 1, true, 1, 0, {})
            OptionButton("Correct (not selected)", 0, true, 1, 0, {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StreakProgressRingPreview() {
    DailyRoundsAssignmentTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StreakProgressRing(currentStreak = 0, baseSize = 80.dp, sizePerStreak = 4.dp)
            StreakProgressRing(currentStreak = 1, baseSize = 80.dp, sizePerStreak = 4.dp)
            StreakProgressRing(currentStreak = 3, baseSize = 80.dp, sizePerStreak = 4.dp)
            StreakProgressRing(currentStreak = 4, baseSize = 80.dp, sizePerStreak = 4.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StreakFlamesPreview() {
    DailyRoundsAssignmentTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StreakFlames(currentStreak = 0)
            StreakFlames(currentStreak = 1)
            StreakFlames(currentStreak = 2)
            StreakFlames(currentStreak = 4)
        }
    }
}
