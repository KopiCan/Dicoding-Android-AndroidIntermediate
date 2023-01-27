package com.dicoding.andrintermediate2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.andrintermediate2.data.HomeRepository
import com.dicoding.andrintermediate2.response.ListStory

class HomeViewModel (private val homeRepository: HomeRepository): ViewModel() {
    fun getStory(token: String) : LiveData<PagingData<ListStory>> =
        homeRepository.getStories(token).cachedIn(viewModelScope)
}