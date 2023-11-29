package com.dicoding.picodiploma.StoryApp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.StoryApp.data.api.ListStoryItem
import com.dicoding.picodiploma.StoryApp.databinding.CardItemBinding

class Adapter :
    PagingDataAdapter<ListStoryItem, Adapter.ViewHolder>(DIFF_CALLBACK) {

    private var onItemClick: ((ListStoryItem) -> Unit)? = null

    inner class ViewHolder(private val binding: CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.apply {
                tvName.text = data.name
                tvDesc.text = data.description
            }
            Glide.with(itemView.context).load(data.photoUrl).into(binding.ivStory)
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(data)
                }
            }
        }
    }

    fun setOnClickListener(listener: (ListStoryItem) -> Unit){
        onItemClick = listener
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
