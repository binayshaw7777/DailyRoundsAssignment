package com.binayshaw7777.dailyroundsassignment

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application subclass that bootstraps Hilt dependency injection and Timber logging.
 *
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation and provide
 * the application-level [dagger.hilt.components.SingletonComponent].
 *
 * Timber's [Timber.DebugTree] is planted only in debug builds to avoid logging
 * in release.
 */
@HiltAndroidApp
class DailyRoundsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
