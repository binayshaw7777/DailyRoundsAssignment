package com.binayshaw7777.dailyroundsassignment.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import sv.lib.squircleshape.SquircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrong
import com.binayshaw7777.dailyroundsassignment.ui.theme.TextPrimary
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.theme.DMSans

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
    val defaultBgColor = MaterialTheme.colorScheme.surfaceVariant
    val defaultTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val targetColor = when {
        !isAnswered -> defaultBgColor
        optionIndex == selectedOptionIndex && optionIndex == correctOptionIndex -> OptionCorrect
        optionIndex == selectedOptionIndex -> OptionWrong
        optionIndex == correctOptionIndex -> OptionCorrect
        else -> defaultBgColor
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 400),
        label = "optionColor$optionIndex",
    )

    Button(
        onClick = { if (!isAnswered) onOptionSelected(optionIndex) },
        enabled = !isAnswered,
        shape = SquircleShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            contentColor = if (targetColor == defaultBgColor) defaultTextColor else TextPrimary,
            disabledContentColor = if (targetColor == defaultBgColor) defaultTextColor else TextPrimary,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (!isAnswered) defaultTextColor.copy(alpha = 0.15f) else targetColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
    ) {
        val isCode = text.contains("_")
        Text(
            text = text,
            fontSize = if (isCode) 14.sp else 16.sp,
            fontFamily = if (isCode) FontFamily.Monospace else DMSans,
            fontWeight = FontWeight.Medium,
            color = if (targetColor == defaultBgColor) defaultTextColor else TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StreakFlames(
    currentStreak: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
    repeat(4) { index ->
        val isActive = currentStreak > index
        Text(
                text = "🔥",
                fontSize = 28.sp,
                color = if (isActive) TextPrimary else TextPrimary.copy(alpha = 0.3f),
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionButtonAllStatesPreview() {
    DailyRoundsAssignmentTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Default / Not Answered", fontWeight = FontWeight.Bold, color = TextPrimary)
            OptionButton(
                text = "Default Option",
                optionIndex = 0,
                isAnswered = false,
                selectedOptionIndex = null,
                correctOptionIndex = 1,
                onOptionSelected = {}
            )
            
            Text(text = "Correct Selected", fontWeight = FontWeight.Bold, color = TextPrimary)
            OptionButton(
                text = "Correct Option",
                optionIndex = 0,
                isAnswered = true,
                selectedOptionIndex = 0,
                correctOptionIndex = 0,
                onOptionSelected = {}
            )
            
            Text(text = "Wrong Selected", fontWeight = FontWeight.Bold, color = TextPrimary)
            OptionButton(
                text = "Wrong Option",
                optionIndex = 1,
                isAnswered = true,
                selectedOptionIndex = 1,
                correctOptionIndex = 0,
                onOptionSelected = {}
            )

            Text(text = "Correct (Not Selected)", fontWeight = FontWeight.Bold, color = TextPrimary)
            OptionButton(
                text = "Correct Option",
                optionIndex = 0,
                isAnswered = true,
                selectedOptionIndex = 1,
                correctOptionIndex = 0,
                onOptionSelected = {}
            )
            
            Text(text = "Code Snippet (Default)", fontWeight = FontWeight.Bold, color = TextPrimary)
            OptionButton(
                text = "fun_main_args()",
                optionIndex = 0,
                isAnswered = false,
                selectedOptionIndex = null,
                correctOptionIndex = 1,
                onOptionSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StreakFlamesAllStatesPreview() {
    DailyRoundsAssignmentTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Streak = 0", fontWeight = FontWeight.Bold, color = TextPrimary)
            StreakFlames(currentStreak = 0)
            
            Text(text = "Streak = 2", fontWeight = FontWeight.Bold, color = TextPrimary)
            StreakFlames(currentStreak = 2)
            
            Text(text = "Streak = 4+", fontWeight = FontWeight.Bold, color = TextPrimary)
            StreakFlames(currentStreak = 4)
        }
    }
}
