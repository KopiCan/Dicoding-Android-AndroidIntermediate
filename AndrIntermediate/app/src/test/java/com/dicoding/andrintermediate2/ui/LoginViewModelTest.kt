package com.dicoding.andrintermediate2.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.andrintermediate2.DataDummy
import com.dicoding.andrintermediate2.data.HomeRepository
import com.dicoding.andrintermediate2.data.ResultData
import com.dicoding.andrintermediate2.getOrAwaitValue
import com.dicoding.andrintermediate2.response.LoginResponse
import com.dicoding.andrintermediate2.ui.login.LoginViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var homeRepository: HomeRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dataDummyLogin = DataDummy.generateDummyLogin()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(homeRepository)
    }

    @Test
    fun `When Test Login Should Not Be Null and Will Return Success`() {
        val expectedUser = MutableLiveData<ResultData<LoginResponse>>()
        expectedUser.value = ResultData.Success(dataDummyLogin)
        Mockito.`when`(homeRepository.postLogin(EMAIL, PASSWORD)).thenReturn(expectedUser)

        val actualUser = loginViewModel.postLogin(EMAIL, PASSWORD).getOrAwaitValue()

        Mockito.verify(homeRepository).postLogin(EMAIL, PASSWORD)
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is ResultData.Success)
    }

    companion object {
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}