package com.dicoding.andrintermediate2.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.andrintermediate2.CoroutineRule
import com.dicoding.andrintermediate2.DataDummy
import com.dicoding.andrintermediate2.adapter.AdapterStory
import com.dicoding.andrintermediate2.data.HomeRepository
import com.dicoding.andrintermediate2.getOrAwaitValue
import com.dicoding.andrintermediate2.response.ListStory
import com.dicoding.andrintermediate2.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = CoroutineRule()

    @Mock
    private lateinit var homeRepository: HomeRepository
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun `When Test Get Story Should Not Be Null`() = mainCoroutineRules.runBlockingTest {
        val dataDummyStory = DataDummy.generateDummyStoryList()
        val data: PagingData<ListStory> = PagedTestDataSources.snapshot(dataDummyStory)
        val story = MutableLiveData<PagingData<ListStory>>()
        story.value = data
        Mockito.`when`(homeRepository.getStories(TOKEN)).thenReturn(story)
        val resultStory: PagingData<ListStory> = homeViewModel.getStory(TOKEN).getOrAwaitValue()

        val dataDiffer = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        dataDiffer.submitData(resultStory)

        advanceUntilIdle()

        Mockito.verify(homeRepository).getStories(TOKEN)
        Assert.assertNotNull(dataDiffer.snapshot())
        Assert.assertEquals(dataDummyStory.size, dataDiffer.snapshot().size)
    }

    companion object {
        private const val TOKEN = "Bearer TOKEN"
    }
}

class PagedTestDataSources private constructor(private val items: List<ListStory>) :
    PagingSource<Int, LiveData<List<ListStory>>>() {
    companion object {
        fun snapshot(items: List<ListStory>): PagingData<ListStory> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStory>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStory>>> {
        return LoadResult.Page(emptyList(), 0 , 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
