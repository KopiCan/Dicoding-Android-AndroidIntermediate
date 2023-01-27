package com.dicoding.andrintermediate2.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.andrintermediate2.DataDummy
import com.dicoding.andrintermediate2.data.HomeRepository
import com.dicoding.andrintermediate2.data.ResultData
import com.dicoding.andrintermediate2.getOrAwaitValue
import com.dicoding.andrintermediate2.response.AllStoryResponse
import com.dicoding.andrintermediate2.ui.maps.MapsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var homeRepository: HomeRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dataDummyMaps = DataDummy.generateDummyStoryWithMaps()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(homeRepository)
    }

    @Test
    fun `when Get Story With Maps Should Not Null and Return Success`() {
        val expectedStory = MutableLiveData<ResultData<AllStoryResponse>>()
        expectedStory.value = ResultData.Success(dataDummyMaps)
        Mockito.`when`(homeRepository.getStoryWithMaps(LOCATION, TOKEN)).thenReturn(expectedStory)

        val actualStory = mapsViewModel.getStoryWithMaps(LOCATION, TOKEN).getOrAwaitValue()

        Mockito.verify(homeRepository).getStoryWithMaps(LOCATION, TOKEN)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is ResultData.Success)
    }

    companion object {
        private const val LOCATION = 1
        private const val TOKEN = "Bearer TOKEN"
    }
}