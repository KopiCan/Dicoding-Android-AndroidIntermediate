package com.dicoding.andrintermediate2.data

import android.content.Context

class ProfilePreferences (context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserModel){
        val editor = preferences.edit()
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.token = preferences.getString(TOKEN, "")
        return model
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = ""
    }
}