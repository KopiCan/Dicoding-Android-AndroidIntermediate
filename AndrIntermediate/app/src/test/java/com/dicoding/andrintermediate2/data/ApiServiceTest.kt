package com.dicoding.andrintermediate2.data

import com.dicoding.andrintermediate2.DataDummy
import com.dicoding.andrintermediate2.api.ApiService
import com.dicoding.andrintermediate2.response.AllStoryResponse
import com.dicoding.andrintermediate2.response.LoginResponse
import com.dicoding.andrintermediate2.response.NewStoryResponse
import com.dicoding.andrintermediate2.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiServiceTest: ApiService {
    private val dummyStoryResponse = DataDummy.generateDummyStoryWithMaps()
    private val dummyRegisterResponse = DataDummy.generateDummyRegister()
    private val dummyLoginResponse = DataDummy.generateDummyLogin()
    private val dummyAddNewStoryResponse = DataDummy.generateDummyAddNewStory()

    override suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        return dummyRegisterResponse
    }

    override suspend fun postLogin(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun getStories(page: Int, size: Int, auth: String): AllStoryResponse {
        return dummyStoryResponse
    }

    override suspend fun getStoriesWithLocation(loc: Int, auth: String): AllStoryResponse {
        return dummyStoryResponse
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?,
        auth: String
    ): NewStoryResponse {
        return dummyAddNewStoryResponse
    }
}