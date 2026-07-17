package com.binayshaw7777.dailyroundsassignment.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

class AppPreferences(private val context: Context) {
    private object Keys {
        val USE_REMOTE_API = booleanPreferencesKey("use_remote_api")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    val useRemoteApi: Flow<Boolean> = context.dataStore.data.map { it[Keys.USE_REMOTE_API] ?: false }
    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map { it[Keys.IS_DARK_THEME] ?: true }
    val hasCompletedOnboarding: Flow<Boolean> = context.dataStore.data.map { it[Keys.HAS_COMPLETED_ONBOARDING] ?: false }
    val userName: Flow<String> = context.dataStore.data.map { it[Keys.USER_NAME] ?: "" }

    suspend fun setUseRemoteApi(enabled: Boolean) {
        context.dataStore.edit { it[Keys.USE_REMOTE_API] = enabled }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[Keys.IS_DARK_THEME] = enabled }
    }

    suspend fun setHasCompletedOnboarding(completed: Boolean) {
        context.dataStore.edit { it[Keys.HAS_COMPLETED_ONBOARDING] = completed }
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { it[Keys.USER_NAME] = name }
    }
}
