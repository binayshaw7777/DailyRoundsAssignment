package com.binayshaw7777.dailyroundsassignment.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect
import com.binayshaw7777.dailyroundsassignment.ui.theme.TextPrimary

private data class OnboardingPage(
    val emoji: String,
    val title: String,
    val description: String,
)

private val pages = listOf(
    OnboardingPage(
        emoji = "🧠",
        title = "Test Your Knowledge",
        description = "Challenge yourself with carefully curated MCQ questions across a variety of topics.",
    ),
    OnboardingPage(
        emoji = "🔥",
        title = "Build Your Streak",
        description = "Answer 3 or more questions correctly in a row to ignite your streak and climb the rankings.",
    ),
    OnboardingPage(
        emoji = "🏆",
        title = "Track Your Progress",
        description = "Every quiz result is saved. Review your history, accuracy, and longest streaks anytime.",
    ),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { uiState.totalPages })

    LaunchedEffect(uiState.currentPage) {
        pagerState.animateScrollToPage(uiState.currentPage)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            if (!uiState.isLastPage) {
                TextButton(onClick = { onEvent(OnboardingUiEvent.Skip) }) {
                    Text(text = "Skip", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            userScrollEnabled = false,
        ) { page ->
            val p = pages[page]
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = p.emoji, fontSize = 80.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = p.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = p.description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(uiState.totalPages) { index ->
                val isSelected = index == uiState.currentPage
                val color by animateColorAsState(
                    targetValue = if (isSelected) OptionCorrect else MaterialTheme.colorScheme.surfaceVariant,
                    label = "dot_color_$index",
                )
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(color),
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onEvent(OnboardingUiEvent.Next) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OptionCorrect),
        ) {
            Text(
                text = if (uiState.isLastPage) "Get Started" else "Next",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    DailyRoundsAssignmentTheme {
        OnboardingScreen(
            uiState = OnboardingUiState(),
            onEvent = {},
        )
    }
}
