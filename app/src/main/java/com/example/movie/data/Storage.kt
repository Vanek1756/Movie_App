package com.example.movie.data

import android.content.Context
import com.example.movie.util.*
import javax.inject.Inject

class Storage @Inject constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )

    fun addPreferencesSession(value: String) = with(sharedPreferences.edit()) {
        putString(KEY_SESSION, value).apply()
    }

    fun addPreferencesUserName(value: String) = with(sharedPreferences.edit()) {
        putString(KEY_USER_NAME, value).apply()
    }

    fun addPreferencesName(value: String) = with(sharedPreferences.edit()) {
        putString(KEY_NAME, value).apply()
    }

    fun addPreferencesId(value: String) = with(sharedPreferences.edit()) {
        putString(KEY_USER_ID, value).apply()
    }

    fun addPreferencesImageProfile(tmdb: String?) = with(sharedPreferences.edit()) {
        putString(KEY_USER_IMAGE_PROFILE, tmdb).apply()
    }

    fun readPreferencesSession() = sharedPreferences.getString(KEY_SESSION, "")
    fun readPreferencesUserName() = sharedPreferences.getString(KEY_USER_NAME, "")
    fun readPreferencesName() = sharedPreferences.getString(KEY_NAME, "")
    fun readPreferencesId() = sharedPreferences.getString(KEY_USER_ID, "")
    fun readPreferencesImageProfile() = sharedPreferences.getString(KEY_USER_IMAGE_PROFILE, "")

    fun clearStorage() = sharedPreferences.edit().clear().apply()
}
