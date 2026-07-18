package com.binayshaw7777.dailyroundsassignment.ui.userdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.binayshaw7777.dailyroundsassignment.ui.components.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.dailyroundsassignment.ui.theme.DailyRoundsAssignmentTheme
import sv.lib.squircleshape.SquircleShape

/**
 * Name-entry screen shown after onboarding to personalize the experience.
 *
 * Displays a greeting emoji, prompt text, an [OutlinedTextField] with squircle
 * shape, and a "Continue" button. The button is disabled until the name is at
 * least 2 characters (trimmed). Handles IME Done action to submit.
 *
 * @param uiState Current [UserDetailsUiState] with name text and submission flags.
 * @param onEvent Callback for user interactions (name change, submit, navigation consumed).
 * @param modifier [Modifier] applied to the root Box.
 */
@Composable
fun UserDetailsScreen(
    uiState: UserDetailsUiState,
    onEvent: (UserDetailsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            UserDetailsHeader()

            Spacer(modifier = Modifier.height(36.dp))

            NameTextField(
                value = uiState.name,
                onValueChange = { onEvent(UserDetailsUiEvent.NameChanged(it)) },
                onDone = {
                    focusManager.clearFocus()
                    onEvent(UserDetailsUiEvent.Submit)
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            ContinueButton(
                enabled = uiState.isNameValid && !uiState.isSubmitting,
                isSubmitting = uiState.isSubmitting,
                onClick = {
                    focusManager.clearFocus()
                    onEvent(UserDetailsUiEvent.Submit)
                },
            )
        }
    }
}

@Composable
private fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text("Enter your name", color = MaterialTheme.colorScheme.onSurfaceVariant, enableAutoSize = false)
        },
        singleLine = true,
        shape = SquircleShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
    )
}

@Composable
private fun ContinueButton(
    enabled: Boolean,
    isSubmitting: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = SquircleShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    ) {
        Text(
            text = if (isSubmitting) "Saving..." else "Continue",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun UserDetailsHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "👋", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "What's your name?",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We'll personalize your experience.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailsScreenPreview() {
    DailyRoundsAssignmentTheme {
        UserDetailsScreen(uiState = UserDetailsUiState(name = "Binay"), onEvent = {})
    }
}
