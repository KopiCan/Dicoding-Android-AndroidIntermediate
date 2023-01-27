package com.dicoding.andrintermediate2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.andrintermediate2.adapter.AdapterStory
import com.dicoding.andrintermediate2.data.ProfilePreferences
import com.dicoding.andrintermediate2.data.UserModel
import com.dicoding.andrintermediate2.databinding.FragmentHomeBinding
import com.dicoding.andrintermediate2.response.ListStory
import com.dicoding.andrintermediate2.ui.detailstory.DetailStoryActivity
import com.dicoding.andrintermediate2.ui.utilitys.ViewModelFactory

class HomeFramgent: Fragment() {
    private var token : String = ""
    private lateinit var binding : FragmentHomeBinding
    private lateinit var userModel: UserModel
    private lateinit var profilePreferences: ProfilePreferences
    private lateinit var adapterStory: AdapterStory
    private lateinit var rvStory: RecyclerView
    private lateinit var viewModelFactory: ViewModelFactory
    private val storyViewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory.getInstance(requireActivity())

        profilePreferences = ProfilePreferences(requireActivity())
        userModel = profilePreferences.getUser()

        rvStory = binding.rvHome

        token = userModel.token.toString()

        getStory()


    }

    private fun getStory() {
        val authToken = "Bearer $token"

        adapterStory = AdapterStory()
        rvStory.adapter = adapterStory

        storyViewModel.getStory(authToken).observe(viewLifecycleOwner) {
            adapterStory.submitData(lifecycle, it)
        }

        rvStory.layoutManager = LinearLayoutManager(context)
        adapterStory.setOnItemClickCallback(object : AdapterStory.OnItemClickCallback {
            override fun onItemClicked(story: ListStory) {
                showSelectedStory(story)
            }
        })
    }

    private fun showSelectedStory(story: ListStory) {
        Intent(activity, DetailStoryActivity::class.java).also {
            it.putExtra(DetailStoryActivity.EXTRA_NAME, story.name)
            it.putExtra(DetailStoryActivity.EXTRA_DESC, story.description)
            it.putExtra(DetailStoryActivity.EXTRA_IMAGE, story.photoUrl)
            startActivity(it)
        }
    }
}