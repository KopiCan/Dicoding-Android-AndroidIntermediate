package com.dicoding.andrintermediate2.injection

import android.content.Context
import com.dicoding.andrintermediate2.api.ApiConfig
import com.dicoding.andrintermediate2.data.HomeRepository
import com.dicoding.andrintermediate2.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context) : HomeRepository {
        val storyDatabase = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return HomeRepository(storyDatabase, apiService)
    }
}