package com.binayshaw7777.dailyroundsassignment.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import sv.lib.squircleshape.SquircleShape

/**
 * Available bottom navigation tabs on the Home screen.
 */
enum class HomeTab { QUIZ, LEADERBOARD, SETTINGS }

private data class TabItem(
    val tab: HomeTab,
    val label: String,
    val icon: ImageVector,
)

private val tabs = listOf(
    TabItem(HomeTab.QUIZ, "Quiz", Icons.Default.Edit),
    TabItem(HomeTab.LEADERBOARD, "Scores", Icons.Default.Star),
    TabItem(HomeTab.SETTINGS, "Settings", Icons.Default.Settings),
)

/**
 * Main home screen featuring a custom bottom navigation bar with three tabs:
 * **Quiz**, **Leaderboard**, and **Settings**.
 *
 * The top bar title dynamically updates based on the selected tab, showing a
 * personalized greeting ("Hey, {name}!") when on the Quiz tab.
 *
 * Content for each tab is provided via slot parameters, allowing the parent
 * ([AppNavigation]) to supply the actual screen content while this composable
 * handles tab selection, animated transitions, and the sliding pill indicator.
 *
 * @param userName The user's display name for the personalized greeting. If blank,
 *   the title falls back to "Quiz".
 * @param quizContent Composable content for the Quiz tab.
 * @param leaderboardContent Composable content for the Leaderboard tab.
 * @param settingsContent Composable content for the Settings tab.
 * @param modifier [Modifier] applied to the root Scaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    quizContent: @Composable () -> Unit,
    leaderboardContent: @Composable () -> Unit,
    settingsContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(HomeTab.QUIZ) }
    val selectedIndex = tabs.indexOfFirst { it.tab == selectedTab }

    val topBarTitle = when (selectedTab) {
        HomeTab.QUIZ -> if (userName.isNotBlank()) "Hey, $userName!" else "Quiz"
        HomeTab.LEADERBOARD -> "Leaderboard"
        HomeTab.SETTINGS -> "Settings"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = topBarTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = SquircleShape(18.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                        ) {
                            val itemWidth = maxWidth / tabs.size
                            val pillInset = 4.dp

                            // Sliding pill with horizontal breathing room
                            val pillOffset by animateDpAsState(
                                targetValue = itemWidth * selectedIndex + pillInset,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium,
                                ),
                                label = "pillOffset",
                            )
                            Box(
                                modifier = Modifier
                                    .offset(x = pillOffset)
                                    .width(itemWidth - pillInset * 2)
                                    .height(48.dp)
                                    .clip(SquircleShape(14.dp))
                                    .background(MaterialTheme.colorScheme.primary),
                            )

                            // Tab items
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                tabs.forEachIndexed { index, tabItem ->
                                    val selected = selectedTab == tabItem.tab

                                    val iconColor by animateColorAsState(
                                        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary
                                                      else MaterialTheme.colorScheme.onSurfaceVariant,
                                        animationSpec = tween(180),
                                        label = "ic_${tabItem.tab}",
                                    )
                                    val textColor by animateColorAsState(
                                        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary
                                                      else MaterialTheme.colorScheme.onSurfaceVariant,
                                        animationSpec = tween(180),
                                        label = "tx_${tabItem.tab}",
                                    )
                                    val scale by animateFloatAsState(
                                        targetValue = if (selected) 1.12f else 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium,
                                        ),
                                        label = "sc_${tabItem.tab}",
                                    )

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clip(SquircleShape(14.dp))
                                            .clickable { selectedTab = tabItem.tab },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(2.dp),
                                        ) {
                                            Icon(
                                                imageVector = tabItem.icon,
                                                contentDescription = tabItem.label,
                                                tint = iconColor,
                                                modifier = Modifier
                                                    .scale(scale)
                                                    .size(20.dp),
                                            )
                                            Text(
                                                text = tabItem.label,
                                                fontSize = 10.sp,
                                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                                color = textColor,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
        ) { innerPadding ->
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    val cur = tabs.indexOfFirst { it.tab == initialState }
                    val tgt = tabs.indexOfFirst { it.tab == targetState }
                    val right = tgt > cur
                    (slideInHorizontally(tween(280)) { if (right) it else -it } + fadeIn(tween(280))) togetherWith
                            (slideOutHorizontally(tween(240)) { if (right) -it else it } + fadeOut(tween(200)))
                },
                label = "tabContent",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding(),
                    ),
            ) { tab ->
                when (tab) {
                    HomeTab.QUIZ -> quizContent()
                    HomeTab.LEADERBOARD -> leaderboardContent()
                    HomeTab.SETTINGS -> settingsContent()
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    DailyRoundsAssignmentTheme {
        HomeScreen(
            userName = "Binay",
            quizContent = { Box(Modifier.fillMaxSize()) { Text("Quiz") } },
            leaderboardContent = { Box(Modifier.fillMaxSize()) { Text("Leaderboard") } },
            settingsContent = { Box(Modifier.fillMaxSize()) { Text("Settings") } },
        )
    }
}
