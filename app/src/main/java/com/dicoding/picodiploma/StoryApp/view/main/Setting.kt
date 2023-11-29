package com.dicoding.picodiploma.StoryApp.view.main

import android.content.Context

class Setting(context: Context) {

    companion object{
        private const val REFS_NAME = "user_preference"
        private const val TOKEN = "token"
    }

    private val preferences = context.getSharedPreferences(REFS_NAME, Context.MODE_PRIVATE)

    fun putUser(token: String){
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun gainUser(): String?{
        val gainToken = preferences.getString(TOKEN, "")
        return gainToken
    }
}