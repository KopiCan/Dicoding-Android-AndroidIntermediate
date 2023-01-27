package com.dicoding.andrintermediate2.ui.maps

import androidx.lifecycle.ViewModel
import com.dicoding.andrintermediate2.data.HomeRepository

class MapsViewModel (private val homeRepository: HomeRepository) : ViewModel() {
    fun getStoryWithMaps(location: Int, token: String) =
        homeRepository.getStoryWithMaps(location, token)
}