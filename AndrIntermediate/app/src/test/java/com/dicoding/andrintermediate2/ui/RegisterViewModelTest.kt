package com.dicoding.andrintermediate2.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.andrintermediate2.DataDummy
import com.dicoding.andrintermediate2.data.HomeRepository
import com.dicoding.andrintermediate2.data.ResultData
import com.dicoding.andrintermediate2.getOrAwaitValue
import com.dicoding.andrintermediate2.response.RegisterResponse
import com.dicoding.andrintermediate2.ui.register.RegisterViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var homeRepository: HomeRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val datadummySignUp = DataDummy.generateDummyRegister()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(homeRepository)
    }

    @Test
    fun `When Test Sign Up Should Not Be Null and Will Return Success`() {
        val expectedUser = MutableLiveData<ResultData<RegisterResponse>>()
        expectedUser.value = ResultData.Success(datadummySignUp)
        Mockito.`when`(homeRepository.postRegister(NAME, EMAIL, PASSWORD)).thenReturn(expectedUser)

        val resultUser = registerViewModel.postRegister(NAME, EMAIL, PASSWORD).getOrAwaitValue()

        Mockito.verify(homeRepository).postRegister(NAME, EMAIL, PASSWORD)
        Assert.assertNotNull(resultUser)
        Assert.assertTrue(resultUser is ResultData.Success)
    }

    companion object {
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}