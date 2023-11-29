package com.dicoding.picodiploma.StoryApp.di

import android.content.Context
import com.dicoding.picodiploma.StoryApp.data.UserRepository
import com.dicoding.picodiploma.StoryApp.data.retrofit.ApiConfig
import com.dicoding.picodiploma.StoryApp.view.main.Setting

object Injection {
    fun provideRepository(context: Context): UserRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
        val pref = Setting(context)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}