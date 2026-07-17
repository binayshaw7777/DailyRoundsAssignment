package com.binayshaw7777.dailyroundsassignment.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme

enum class HomeTab { QUIZ, LEADERBOARD, SETTINGS }

private data class TabItem(
    val tab: HomeTab,
    val label: String,
    val icon: ImageVector,
)

private val tabs = listOf(
    TabItem(HomeTab.QUIZ, "Quiz", Icons.Default.Edit),
    TabItem(HomeTab.LEADERBOARD, "Leaderboard", Icons.Default.Star),
    TabItem(HomeTab.SETTINGS, "Settings", Icons.Default.Settings),
)

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

    val topBarTitle = when (selectedTab) {
        HomeTab.QUIZ -> if (userName.isNotBlank()) "Hey, $userName!" else "Quiz"
        HomeTab.LEADERBOARD -> "Leaderboard"
        HomeTab.SETTINGS -> "Settings"
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tabItem ->
                    NavigationBarItem(
                        selected = selectedTab == tabItem.tab,
                        onClick = { selectedTab = tabItem.tab },
                        icon = {
                            Icon(
                                imageVector = tabItem.icon,
                                contentDescription = tabItem.label,
                            )
                        },
                        label = { Text(tabItem.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = innerPadding.calculateBottomPadding()),
        ) {
            when (selectedTab) {
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
            quizContent = { Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { Text("Quiz", color = MaterialTheme.colorScheme.onBackground) } },
            leaderboardContent = { Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { Text("Leaderboard", color = MaterialTheme.colorScheme.onBackground) } },
            settingsContent = { Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { Text("Settings", color = MaterialTheme.colorScheme.onBackground) } },
        )
    }
}
