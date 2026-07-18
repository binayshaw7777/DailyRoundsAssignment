package com.binayshaw7777.dailyroundsassignment.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import kotlinx.coroutines.launch

/**
 * Animated splash screen displayed for ~2 seconds on cold start.
 *
 * Shows the app logo emoji, "QuizMaster" title, and tagline with a combined
 * fade-in and spring-scale animation.
 *
 * Navigation is handled by the parent composable via [SplashViewModel.uiState];
 * this screen is purely visual.
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.85f) }

    LaunchedEffect(Unit) {
        launch { alpha.animateTo(1f, animationSpec = tween(400)) }
        launch {
            scale.animateTo(
                1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow,
                ),
            )
        }
    }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(
            modifier = Modifier.fillMaxSize().systemBarsPadding(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.alpha(alpha.value).scale(scale.value),
            ) {
                Text(text = "🔥", fontSize = 56.sp, enableAutoSize = false)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "QuizMaster",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Test. Learn. Level up.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    DailyRoundsAssignmentTheme { SplashScreen() }
}
