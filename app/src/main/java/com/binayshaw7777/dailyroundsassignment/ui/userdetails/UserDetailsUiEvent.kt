package com.binayshaw7777.dailyroundsassignment.ui.userdetails

sealed interface UserDetailsUiEvent {
    data class NameChanged(val name: String) : UserDetailsUiEvent
    data object Submit : UserDetailsUiEvent
}
