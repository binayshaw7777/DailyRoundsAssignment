package com.binayshaw7777.dailyroundsassignment.ui.userdetails

/**
 * Immutable UI state for the user-details (name entry) screen.
 *
 * @property name Current text-field value.
 * @property isSubmitting `true` while preferences are being persisted.
 * @property navigateToHome One-shot flag to trigger navigation to the home screen.
 */
data class UserDetailsUiState(
    val name: String = "",
    val isSubmitting: Boolean = false,
    val navigateToHome: Boolean = false,
) {
    /** `true` when the trimmed name is at least 2 characters. */
    val isNameValid: Boolean get() = name.trim().length >= 2
}
