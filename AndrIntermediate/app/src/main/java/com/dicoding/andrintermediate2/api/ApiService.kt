package com.dicoding.andrintermediate2.api

import com.dicoding.andrintermediate2.response.AllStoryResponse
import com.dicoding.andrintermediate2.response.LoginResponse
import com.dicoding.andrintermediate2.response.NewStoryResponse
import com.dicoding.andrintermediate2.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") auth: String
    ): AllStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") loc: Int,
        @Header("Authorization") auth: String
    ): AllStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?,
        @Header("Authorization") auth: String
    ): NewStoryResponse
}