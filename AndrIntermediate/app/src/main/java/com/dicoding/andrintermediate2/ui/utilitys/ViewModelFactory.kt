package com.dicoding.andrintermediate2.ui.utilitys

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.andrintermediate2.data.HomeRepository
import com.dicoding.andrintermediate2.injection.Injection
import com.dicoding.andrintermediate2.ui.home.HomeViewModel
import com.dicoding.andrintermediate2.ui.login.LoginViewModel
import com.dicoding.andrintermediate2.ui.maps.MapsViewModel
import com.dicoding.andrintermediate2.ui.newstory.NewStoryActivity
import com.dicoding.andrintermediate2.ui.newstory.NewStoryViewModel
import com.dicoding.andrintermediate2.ui.register.RegisterViewModel

class ViewModelFactory private constructor(private val homeRepository: HomeRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(homeRepository) as T }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(homeRepository) as T }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(homeRepository) as T }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                return MapsViewModel(homeRepository) as T }

            modelClass.isAssignableFrom(NewStoryViewModel::class.java) -> {
                return NewStoryViewModel(homeRepository) as T }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}