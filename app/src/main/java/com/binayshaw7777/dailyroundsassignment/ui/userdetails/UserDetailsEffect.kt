package com.binayshaw7777.dailyroundsassignment.ui.userdetails

sealed interface UserDetailsEffect {
    data object NavigateToHome : UserDetailsEffect
}
