package com.example.commentsold.di

import android.content.SharedPreferences
import com.example.commentsold.utils.Constants
import javax.inject.Inject

class SessionManager @Inject constructor(private val preferences: SharedPreferences) {

    fun getAuthToken() = preferences.getString(Constants.AUTH_TOKEN_KEY, "")

    fun saveAuthToken(token: String) {
        val editor = preferences.edit()
        editor.putString(Constants.AUTH_TOKEN_KEY, token)
        editor.apply()
    }
}