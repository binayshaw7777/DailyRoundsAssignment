package com.binayshaw7777.dailyroundsassignment.util

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

/**
 * Triggers a **success** haptic pattern.
 *
 * On Android 11+ (R) uses [HapticFeedbackConstants.CONFIRM]; on older devices
 * falls back to [HapticFeedbackConstants.VIRTUAL_KEY].
 *
 * Usage:
 * ```kotlin
 * LocalView.current.hapticSuccess()
 * ```
 */
fun View.hapticSuccess() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}

/**
 * Triggers a **failure** haptic pattern.
 *
 * On Android 11+ (R) uses [HapticFeedbackConstants.REJECT]; on older devices
 * falls back to [HapticFeedbackConstants.LONG_PRESS].
 *
 * Usage:
 * ```kotlin
 * LocalView.current.hapticFailure()
 * ```
 */
fun View.hapticFailure() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.REJECT)
    } else {
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}

/**
 * Triggers a neutral **click** haptic via [HapticFeedbackConstants.KEYBOARD_TAP].
 *
 * Used for skip/neutral actions that don't carry success or failure semantics.
 *
 * Usage:
 * ```kotlin
 * LocalView.current.hapticClick()
 * ```
 */
fun View.hapticClick() {
    performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
}
