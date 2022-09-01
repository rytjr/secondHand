package com.example.mainfragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mainfragment.databinding.ChatBinding

class chattingAdapter : ListAdapter<chatitem2, chattingAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ChatBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(chatItem: chatitem2) {

            binding.myName.text = chatItem.senderId
            binding.chatting.text = chatItem.message

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(currentList[position])


    }

    companion object {
        @SuppressLint("DiffUtilEquals")
        val diffUtil = object : DiffUtil.ItemCallback<chatitem2>() {
            override fun areItemsTheSame(oldItem: chatitem2, newItem: chatitem2): Boolean {
                // 현재 노출하고 있는 아이템과 새로운 아이템이 같은지 비교;
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: chatitem2, newItem: chatitem2): Boolean {

                // equals 비교;
                return oldItem == newItem

            }

        }
    }
}

