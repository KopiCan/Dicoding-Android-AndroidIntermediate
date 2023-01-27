package com.dicoding.andrintermediate2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.andrintermediate2.databinding.RvHomeBinding
import com.dicoding.andrintermediate2.response.ListStory

class AdapterStory :
    PagingDataAdapter<ListStory, AdapterStory.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(private val binding: RvHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStory) {
            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .into(binding.imgStory)

            binding.rvProfileName.text = data.name
            binding.rvDescription.text = data.description
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(story:ListStory)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}