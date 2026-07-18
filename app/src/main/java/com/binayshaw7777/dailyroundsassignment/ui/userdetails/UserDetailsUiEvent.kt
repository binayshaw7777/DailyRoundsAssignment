package com.binayshaw7777.dailyroundsassignment.ui.userdetails

/**
 * User-initiated events for the user-details screen, consumed by [UserDetailsViewModel].
 */
sealed interface UserDetailsUiEvent {
    /**
     * The user edited the name field.
     *
     * @property name The current text-field value.
     */
    data class NameChanged(val name: String) : UserDetailsUiEvent

    /** The user tapped "Continue" or pressed the keyboard Done action. */
    data object Submit : UserDetailsUiEvent

    /** One-shot event consumed after navigation to the home screen has occurred. */
    data object OnNavigated : UserDetailsUiEvent
}
