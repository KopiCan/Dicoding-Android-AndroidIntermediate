package com.dicoding.andrintermediate2.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.andrintermediate2.DataDummy
import com.dicoding.andrintermediate2.data.HomeRepository
import com.dicoding.andrintermediate2.data.ResultData
import com.dicoding.andrintermediate2.getOrAwaitValue
import com.dicoding.andrintermediate2.response.NewStoryResponse
import com.dicoding.andrintermediate2.ui.newstory.NewStoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class NewStoryViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var homeRepository: HomeRepository
    private lateinit var newStoryViewModel: NewStoryViewModel
    private val dummyAddStory = DataDummy.generateDummyAddNewStory()

    @Before
    fun setUp() {
        newStoryViewModel = NewStoryViewModel(homeRepository)
    }

    @Test
    fun `When Test Upload Should Not Be Null and Will Return Success`() {
        val description = "Ini adalah deksripsi gambar".toRequestBody("text/plain".toMediaType())
        val file = Mockito.mock(File::class.java)
        val requestImg = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImg
        )

        val expectedStory = MutableLiveData<ResultData<NewStoryResponse>>()
        expectedStory.value = ResultData.Success(dummyAddStory)
        Mockito.`when`(homeRepository.uploadImage(imgMultipart, description, LAT, LON, TOKEN)).thenReturn(expectedStory)

        val resultStory = newStoryViewModel.uploadImage(imgMultipart, description, LAT, LON, TOKEN).getOrAwaitValue()

        Mockito.verify(homeRepository).uploadImage(imgMultipart, description, LAT, LON, TOKEN)
        Assert.assertNotNull(resultStory)
        Assert.assertTrue(resultStory is ResultData.Success)
    }

    companion object {
        private const val LAT = 1.23
        private const val LON = 1.23
        private const val TOKEN = "Bearer TOKEN"
    }
}