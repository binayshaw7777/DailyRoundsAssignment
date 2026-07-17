package com.binayshaw7777.dailyroundsassignment.ui.userdetails

data class UserDetailsUiState(
    val name: String = "",
    val isSubmitting: Boolean = false,
) {
    val isNameValid: Boolean get() = name.trim().length >= 2
}
