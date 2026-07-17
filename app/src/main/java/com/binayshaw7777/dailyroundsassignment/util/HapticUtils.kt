package com.binayshaw7777.dailyroundsassignment.util

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

fun View.hapticSuccess() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}

fun View.hapticFailure() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.REJECT)
    } else {
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}

fun View.hapticClick() {
    performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
}
