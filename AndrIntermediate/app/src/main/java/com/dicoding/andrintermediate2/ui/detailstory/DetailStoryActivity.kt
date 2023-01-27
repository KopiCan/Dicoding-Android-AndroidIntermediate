package com.dicoding.andrintermediate2.ui.detailstory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.andrintermediate2.R
import com.dicoding.andrintermediate2.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESC)
        val imgStory = intent.getStringExtra(EXTRA_IMAGE)

        Glide.with(this)
            .load(imgStory)
            .into(binding.imgDetail)

        binding.apply {
            tvUsername.text = username
            tvDescription.text = description
        }

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.detail_story)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_IMAGE = "extra_image"
    }
}