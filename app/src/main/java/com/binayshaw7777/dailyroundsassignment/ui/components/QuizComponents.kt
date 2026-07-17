package com.binayshaw7777.dailyroundsassignment.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.quiz.QuizUiState
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionDefault
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionWrong
import com.binayshaw7777.dailyroundsassignment.ui.theme.StreakActive
import com.binayshaw7777.dailyroundsassignment.ui.theme.StreakInactive
import com.binayshaw7777.dailyroundsassignment.ui.theme.TextPrimary

@Composable
fun OptionButton(
    text: String,
    optionIndex: Int,
    state: QuizUiState,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentQuestion = state.currentQuestion

    val targetColor = when {
        !state.isAnswered -> OptionDefault
        optionIndex == state.selectedOptionIndex && optionIndex == currentQuestion?.correctOptionIndex -> OptionCorrect
        optionIndex == state.selectedOptionIndex -> OptionWrong
        optionIndex == currentQuestion?.correctOptionIndex -> OptionCorrect
        else -> OptionDefault
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 400),
        label = "optionColor$optionIndex"
    )

    Button(
        onClick = { if (!state.isAnswered) onOptionSelected(optionIndex) },
        enabled = !state.isAnswered,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            contentColor = TextPrimary,
            disabledContentColor = TextPrimary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )
    }
}

@Composable
fun StreakFlames(
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        repeat(4) { index ->
            val isActive = currentStreak > index
            val tintColor by animateColorAsState(
                targetValue = if (isActive) StreakActive else StreakInactive,
                animationSpec = tween(durationMillis = 300),
                label = "flame$index"
            )
            // Use emoji rendered in a Text; wrap in a Box so alpha can be applied per flame
            Text(
                text = "🔥",
                fontSize = 28.sp,
                color = if (isActive) TextPrimary else TextPrimary.copy(alpha = 0.3f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
