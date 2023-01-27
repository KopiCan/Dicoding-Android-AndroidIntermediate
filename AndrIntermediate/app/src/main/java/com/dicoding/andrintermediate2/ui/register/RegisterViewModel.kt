package com.dicoding.andrintermediate2.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.andrintermediate2.data.HomeRepository

class RegisterViewModel (private val homeRepository: HomeRepository): ViewModel() {
    fun postRegister(name: String, email: String, pass: String) =
        homeRepository.postRegister(name, email, pass)
}