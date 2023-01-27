package com.dicoding.andrintermediate2.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dicoding.andrintermediate2.api.ApiService
import com.dicoding.andrintermediate2.database.StoryDatabase
import com.dicoding.andrintermediate2.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class HomeRepository (private val storyDatabase: StoryDatabase, private val apiService: ApiService) {

    fun getStories(token: String): LiveData<PagingData<ListStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).liveData
    }

    fun postLogin(email: String, pass: String): LiveData<ResultData<LoginResponse>> = liveData {
        emit(ResultData.Loading)
        try {
            val responseApi = apiService.postLogin(email, pass)
            emit(ResultData.Success(responseApi))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(ResultData.Error(e.message.toString()))
        }
    }

    fun postRegister(
        name: String,
        email: String,
        pass: String
    ): LiveData<ResultData<RegisterResponse>> = liveData {
        emit(ResultData.Loading)
        try {
            val response = apiService.postRegister(name, email, pass)
            emit(ResultData.Success(response))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(ResultData.Error(e.message.toString()))
        }
    }

    fun getStoryWithMaps(location: Int, token: String): LiveData<ResultData<AllStoryResponse>> =
        liveData {
            emit(ResultData.Loading)
            try {
                val responseApi = apiService.getStoriesWithLocation(location, token)
                emit(ResultData.Success(responseApi))
            } catch (e: Exception) {
                Log.d("Register", e.message.toString())
                emit(ResultData.Error(e.message.toString()))
            }
        }

    fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double,
        token: String
    ): LiveData<ResultData<NewStoryResponse>> = liveData {
        emit(ResultData.Loading)
        try {
            val response = apiService.uploadImage(file, description, lat, lon, token)
            emit(ResultData.Success(response))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(ResultData.Error(e.message.toString()))
        }
    }
}