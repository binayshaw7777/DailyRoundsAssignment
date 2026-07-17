package com.binayshaw7777.dailyroundsassignment.ui.settings

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.tooling.preview.Preview
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Data Source section
        SettingsSection(title = "Data Source") {
            SwitchRow(
                label = "Use Remote API",
                checked = uiState.useRemoteApi,
                onChange = { onEvent(SettingsUiEvent.SetUseRemoteApi(it)) },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Appearance section
        SettingsSection(title = "Appearance") {
            SwitchRow(
                label = "Dark Theme",
                checked = uiState.isDarkTheme,
                onChange = { onEvent(SettingsUiEvent.SetDarkTheme(it)) },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // About section
        SettingsSection(title = "About") {
            InfoRow(label = "App Version", value = uiState.appVersion)
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(label = "Built for", value = "DailyRounds Assignment")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = value,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    DailyRoundsAssignmentTheme {
        SettingsScreen(
            uiState = SettingsUiState(
                useRemoteApi = true,
                isDarkTheme = true,
                appVersion = "1.0.0-preview"
            ),
            onEvent = {}
        )
    }
}
