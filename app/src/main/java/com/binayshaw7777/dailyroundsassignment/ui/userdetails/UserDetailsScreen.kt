package com.binayshaw7777.dailyroundsassignment.ui.userdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
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
import com.binayshaw7777.dailyroundsassignment.ui.theme.OptionCorrect

@Composable
fun UserDetailsScreen(
    uiState: UserDetailsUiState,
    onEvent: (UserDetailsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "👋", fontSize = 72.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "What's your name?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We'll use this to personalize your experience.",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = uiState.name,
            onValueChange = { onEvent(UserDetailsUiEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your name", color = MaterialTheme.colorScheme.secondary) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OptionCorrect,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = OptionCorrect,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(UserDetailsUiEvent.Submit)
                },
            ),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onEvent(UserDetailsUiEvent.Submit)
            },
            enabled = uiState.isNameValid && !uiState.isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OptionCorrect,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Text(
                text = if (uiState.isSubmitting) "Saving..." else "Let's Go!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailsScreenPreview() {
    DailyRoundsAssignmentTheme {
        UserDetailsScreen(
            uiState = UserDetailsUiState(name = "Binay"),
            onEvent = {},
        )
    }
}
