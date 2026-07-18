package com.binayshaw7777.dailyroundsassignment.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import sv.lib.squircleshape.SquircleShape

/**
 * Settings screen organized into sections with toggle switches and info rows.
 *
 * Sections:
 * - **Data Source** — toggle between local assets and remote API.
 * - **Quiz** — toggle question shuffling.
 * - **Appearance** — dark / light theme toggle.
 * - **About** — version and build info.
 *
 * @param uiState Current [SettingsUiState] with preference values.
 * @param onEvent Callback for user interactions (toggle settings).
 * @param modifier [Modifier] applied to the root Column.
 */
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        SettingsSection(title = "Data Source") {
            SwitchRow(
                label = "Use Remote API",
                description = "Fetch questions from the cloud",
                checked = uiState.useRemoteApi,
                onChange = { onEvent(SettingsUiEvent.SetUseRemoteApi(it)) },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        SettingsSection(title = "Quiz") {
            SwitchRow(
                label = "Shuffle Questions",
                description = "Randomize question order each session",
                checked = uiState.shuffleQuestions,
                onChange = { onEvent(SettingsUiEvent.SetShuffleQuestions(it)) },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        SettingsSection(title = "Appearance") {
            SwitchRow(
                label = "Dark Theme",
                description = "Switch between light and dark",
                checked = uiState.isDarkTheme,
                onChange = { onEvent(SettingsUiEvent.SetDarkTheme(it)) },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        SettingsSection(title = "About") {
            InfoRow(label = "Version", value = uiState.appVersion)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(label = "Built for", value = "DailyRounds")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 2.dp, bottom = 6.dp),
        )
        Surface(
            shape = SquircleShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun SwitchRow(label: String, description: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            Text(text = description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
            ),
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        Text(text = value, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    DailyRoundsAssignmentTheme {
        SettingsScreen(
            uiState = SettingsUiState(useRemoteApi = true, isDarkTheme = true, appVersion = "1.0.0"),
            onEvent = {},
        )
    }
}
