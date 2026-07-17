package com.binayshaw7777.dailyroundsassignment.data.local.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AppPreferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    private object Keys {
        val USE_REMOTE_API = booleanPreferencesKey("use_remote_api")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    val useRemoteApi: Flow<Boolean> = context.dataStore.data.map {
        val value = it[Keys.USE_REMOTE_API] ?: false
        Log.d(TAG, "useRemoteApi read: $value")
        value
    }
    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map {
        val value = it[Keys.IS_DARK_THEME] ?: true
        Log.d(TAG, "isDarkTheme read: $value")
        value
    }
    val hasCompletedOnboarding: Flow<Boolean> = context.dataStore.data.map {
        val value = it[Keys.HAS_COMPLETED_ONBOARDING] ?: false
        Log.d(TAG, "hasCompletedOnboarding read: $value")
        value
    }
    val userName: Flow<String> = context.dataStore.data.map {
        val value = it[Keys.USER_NAME] ?: ""
        Log.d(TAG, "userName read: $value")
        value
    }

    suspend fun setUseRemoteApi(enabled: Boolean) {
        Log.d(TAG, "setUseRemoteApi($enabled)")
        context.dataStore.edit { it[Keys.USE_REMOTE_API] = enabled }
        Log.d(TAG, "setUseRemoteApi($enabled) persisted")
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        Log.d(TAG, "setDarkTheme($enabled)")
        context.dataStore.edit { it[Keys.IS_DARK_THEME] = enabled }
        Log.d(TAG, "setDarkTheme($enabled) persisted")
    }

    suspend fun setHasCompletedOnboarding(completed: Boolean) {
        Log.d(TAG, "setHasCompletedOnboarding($completed)")
        context.dataStore.edit { it[Keys.HAS_COMPLETED_ONBOARDING] = completed }
        Log.d(TAG, "setHasCompletedOnboarding($completed) persisted")
    }

    suspend fun setUserName(name: String) {
        Log.d(TAG, "setUserName($name)")
        context.dataStore.edit { it[Keys.USER_NAME] = name }
        Log.d(TAG, "setUserName($name) persisted")
    }
}
