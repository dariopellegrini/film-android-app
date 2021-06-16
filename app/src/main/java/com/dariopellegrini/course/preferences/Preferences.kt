package com.dariopellegrini.course.preferences

import android.content.Context
import android.content.SharedPreferences
import com.dariopellegrini.course.BuildConfig

class Preferences(context: Context) {
    private val PREFS_FILENAME = "${BuildConfig.APPLICATION_ID}.preferences"
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var visited: Boolean
        get() = preferences.getBoolean(this::visited.name, false)
        set(value) = preferences.edit().putBoolean(this::visited.name, value).apply()
}