package com.binayshaw7777.dailyroundsassignment.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

/**
 * Type-safe wrapper around Jetpack DataStore Preferences for app-wide settings.
 *
 * Exposes reactive [Flow] properties for each preference key and suspend-based
 * setters that atomically update values.
 *
 * ### Stored keys
 * | Key | Type | Default | Purpose |
 * |-----|------|---------|---------|
 * | `use_remote_api` | Boolean | `false` | Toggle local vs. remote question source |
 * | `is_dark_theme` | Boolean | `true` | Dark/light theme toggle |
 * | `has_completed_onboarding` | Boolean | `false` | First-launch onboarding gate |
 * | `user_name` | String | `""` | Display name shown in the home screen greeting |
 * | `shuffle_questions` | Boolean | `false` | Whether to randomize question order |
 *
 * @see com.binayshaw7777.dailyroundsassignment.ui.settings.SettingsViewModel for the
 *   UI consumer.
 */
@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    private object Keys {
        val USE_REMOTE_API = booleanPreferencesKey("use_remote_api")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val USER_NAME = stringPreferencesKey("user_name")
        val SHUFFLE_QUESTIONS = booleanPreferencesKey("shuffle_questions")
    }

    /** Reactive stream for the "use remote API" toggle. Default: `false`. */
    val useRemoteApi: Flow<Boolean> = context.dataStore.data.map {
        val value = it[Keys.USE_REMOTE_API] ?: false
        Timber.d("useRemoteApi read: %b", value)
        value
    }

    /** Reactive stream for the dark-theme toggle. Default: `true` (dark). */
    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map {
        val value = it[Keys.IS_DARK_THEME] ?: true
        Timber.d("isDarkTheme read: %b", value)
        value
    }

    /** Reactive stream indicating whether the onboarding flow has been completed. */
    val hasCompletedOnboarding: Flow<Boolean> = context.dataStore.data.map {
        val value = it[Keys.HAS_COMPLETED_ONBOARDING] ?: false
        Timber.d("hasCompletedOnboarding read: %b", value)
        value
    }

    /** Reactive stream for the user's display name. Empty string if unset. */
    val userName: Flow<String> = context.dataStore.data.map {
        val value = it[Keys.USER_NAME] ?: ""
        Timber.d("userName read: %s", value)
        value
    }

    /** Reactive stream for the question-shuffle toggle. Default: `false`. */
    val shuffleQuestions: Flow<Boolean> = context.dataStore.data.map {
        val value = it[Keys.SHUFFLE_QUESTIONS] ?: false
        Timber.d("shuffleQuestions read: %b", value)
        value
    }

    /**
     * Persists the remote-API toggle.
     *
     * @param enabled `true` to fetch questions from the remote gist endpoint.
     */
    suspend fun setUseRemoteApi(enabled: Boolean) {
        Timber.d("setUseRemoteApi(%b)", enabled)
        context.dataStore.edit { it[Keys.USE_REMOTE_API] = enabled }
    }

    /**
     * Persists the dark-theme toggle.
     *
     * @param enabled `true` for dark theme, `false` for light.
     */
    suspend fun setDarkTheme(enabled: Boolean) {
        Timber.d("setDarkTheme(%b)", enabled)
        context.dataStore.edit { it[Keys.IS_DARK_THEME] = enabled }
    }

    /**
     * Marks the onboarding flow as completed (or not).
     *
     * @param completed `true` to skip the onboarding screen on subsequent launches.
     */
    suspend fun setHasCompletedOnboarding(completed: Boolean) {
        Timber.d("setHasCompletedOnboarding(%b)", completed)
        context.dataStore.edit { it[Keys.HAS_COMPLETED_ONBOARDING] = completed }
    }

    /**
     * Saves the user's display name.
     *
     * @param name The name to persist. Leading/trailing whitespace is preserved here;
     *   trimming is the caller's responsibility.
     */
    suspend fun setUserName(name: String) {
        Timber.d("setUserName(%s)", name)
        context.dataStore.edit { it[Keys.USER_NAME] = name }
    }

    /**
     * Persists the question-shuffle toggle.
     *
     * @param enabled `true` to randomize question order each quiz session.
     */
    suspend fun setShuffleQuestions(enabled: Boolean) {
        Timber.d("setShuffleQuestions(%b)", enabled)
        context.dataStore.edit { it[Keys.SHUFFLE_QUESTIONS] = enabled }
    }
}
