package com.dicoding.andrintermediate2.ui.login

import androidx.lifecycle.ViewModel
import com.dicoding.andrintermediate2.data.HomeRepository

class LoginViewModel(private val homeRepository: HomeRepository): ViewModel() {
    fun postLogin(email: String, pass: String) = homeRepository.postLogin(email, pass)
}