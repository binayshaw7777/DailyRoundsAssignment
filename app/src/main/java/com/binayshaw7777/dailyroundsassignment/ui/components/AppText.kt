package com.binayshaw7777.dailyroundsassignment.ui.components

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * App-wide text composable that wraps Material 3's `Text` with automatic font
 * size scaling.
 *
 * When [enableAutoSize] is `true` (the default), the text will step down in
 * 0.5sp increments from the resolved font size to 60% of that size, preventing
 * overflow on smaller screens without truncation.
 *
 * This composable is used project-wide instead of `androidx.compose.material3.Text`
 * to ensure consistent auto-sizing behavior.
 *
 * @param text The string content to display.
 * @param modifier [Modifier] applied to the underlying text layout.
 * @param color Text color. Defaults to [Color.Unspecified].
 * @param fontSize Requested font size. Also used as the max for auto-size when
 *   [autoSize] is not provided. Defaults to [TextUnit.Unspecified].
 * @param fontStyle Optional [FontStyle] (normal, italic, oblique).
 * @param fontWeight Optional [FontWeight] (e.g., [FontWeight.Bold]).
 * @param fontFamily Optional [FontFamily]. Defaults to the theme typography.
 * @param letterSpacing Additional letter spacing in [TextUnit].
 * @param textDecoration Text decoration (underline, strikethrough).
 * @param textAlign Horizontal alignment of the text.
 * @param lineHeight Line height in [TextUnit].
 * @param overflow How to handle text that exceeds [maxLines].
 * @param softWrap Whether the text should wrap at soft line breaks.
 * @param maxLines Maximum number of lines before truncation.
 * @param minLines Minimum number of lines to occupy.
 * @param onTextLayout Callback invoked with the final [TextLayoutResult].
 * @param style Base [TextStyle] from which unspecified parameters are inherited.
 * @param enableAutoSize When `true`, auto-size scaling is applied. Set to `false`
 *   to disable and use the raw [fontSize].
 * @param autoSize Explicit [TextAutoSize] configuration. When `null` and
 *   [enableAutoSize] is `true`, a default step-based config is derived from [fontSize].
 *
 * ```kotlin
 * Text(
 *     text = "Hello, World!",
 *     fontSize = 24.sp,
 *     fontWeight = FontWeight.Bold,
 *     color = MaterialTheme.colorScheme.onBackground,
 * )
 * ```
 */

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    enableAutoSize: Boolean = true,
    autoSize: TextAutoSize? = null,
) {
    val resolvedAutoSize = if (enableAutoSize) {
        autoSize ?: run {
            val resolvedMax = when {
                fontSize.isSp -> fontSize
                style.fontSize.isSp -> style.fontSize
                else -> 16.sp
            }
            val maxVal = resolvedMax.value
            val minVal = (maxVal * 0.6f).coerceAtLeast(8f).coerceAtMost(maxVal)
            TextAutoSize.StepBased(
                minFontSize = minVal.sp,
                maxFontSize = resolvedMax,
                stepSize = 0.5.sp
            )
        }
    } else {
        null
    }

    androidx.compose.material3.Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style,
        autoSize = resolvedAutoSize,
    )
}

@Composable
fun Text(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    enableAutoSize: Boolean = true,
    autoSize: TextAutoSize? = null,
) {
    val resolvedAutoSize = if (enableAutoSize) {
        autoSize ?: run {
            val resolvedMax = when {
                fontSize.isSp -> fontSize
                style.fontSize.isSp -> style.fontSize
                else -> 16.sp
            }
            val maxVal = resolvedMax.value
            val minVal = (maxVal * 0.6f).coerceAtLeast(8f).coerceAtMost(maxVal)
            TextAutoSize.StepBased(
                minFontSize = minVal.sp,
                maxFontSize = resolvedMax,
                stepSize = 0.5.sp
            )
        }
    } else {
        null
    }

    androidx.compose.material3.Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style,
        autoSize = resolvedAutoSize,
    )
}
