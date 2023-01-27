package com.dicoding.andrintermediate2.ui.newstory

import androidx.lifecycle.ViewModel
import com.dicoding.andrintermediate2.data.HomeRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NewStoryViewModel (private val homeRepository: HomeRepository): ViewModel() {
    fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double,
        token: String
    ) = homeRepository.uploadImage(file, description, lat, lon, token)
}