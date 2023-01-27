package com.dicoding.andrintermediate2.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.andrintermediate2.CoroutineRule
import com.dicoding.andrintermediate2.DataDummy
import com.dicoding.andrintermediate2.api.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = CoroutineRule()

    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        apiService = ApiServiceTest()
    }

    @Test
    fun getStoriesTest() = mainCoroutineRules.runBlockingTest {
        val expectedStory = DataDummy.generateDummyStoryList()
        val resultStory = apiService.getStories(1, 50, TOKEN)
        assertNotNull(resultStory)
        assertEquals(expectedStory.size, resultStory.listStory.size)
    }

    @Test
    fun postRegisterTest() = mainCoroutineRules.runBlockingTest {
        val expectedUser = DataDummy.generateDummyRegister()
        val resultUser = apiService.postRegister(NAME, EMAIL, PASSWORD)
        assertNotNull(resultUser)
        assertEquals(expectedUser.message, resultUser.message)
    }

    @Test
    fun postLoginTest() = mainCoroutineRules.runBlockingTest {
        val expectedUser = DataDummy.generateDummyLogin()
        val actualUser = apiService.postLogin(EMAIL, PASSWORD)
        assertNotNull(actualUser)
        assertEquals(expectedUser.message, actualUser.message)
    }

    @Test
    fun getStoriesWithLocationTest() = mainCoroutineRules.runBlockingTest {
        val expectedStory = DataDummy.generateDummyStoryWithMaps()
        val resultStory = apiService.getStoriesWithLocation(1, TOKEN)
        assertNotNull(resultStory)
        assertEquals(expectedStory.listStory.size, resultStory.listStory.size)
    }

    @Test
    fun uploadTest() = mainCoroutineRules.runBlockingTest {
        val description = "Img Description".toRequestBody("text/plain".toMediaType())

        val file = mock(File::class.java)
        val requestImg = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImg
        )

        val expectedStory = DataDummy.generateDummyAddNewStory()
        val resultStory = apiService.uploadImage(imgMultipart, description, LAT, LON, TOKEN)
        assertNotNull(resultStory)
        assertEquals(expectedStory.message, resultStory.message)
    }

    companion object {
        private const val TOKEN = "Bearer TOKEN"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val LAT = 1.23
        private const val LON = 1.23
    }
}