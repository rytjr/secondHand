package com.example.mainfragment

import android.provider.Settings.Secure.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mainfragment.databinding.ActivitySubBinding
import com.example.mainfragment.databinding.ChatBinding
import com.example.mainfragment.databinding.ChattingBinding
import com.example.mainfragment.databinding.UerfileBinding
import com.kakao.sdk.user.UserApiClient

class chatAdapter(val onItemClicked: (chatItem) -> Unit) : ListAdapter<chatItem, chatAdapter.ViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ChattingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])

    }

    inner class ViewHolder(val binding: ChattingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: chatItem) {
            binding.root.setOnClickListener {
                onItemClicked(item)
            }

            binding.chatting.text=item.chatting

        }

    }
    companion object{
        val diffUtil = object  : DiffUtil.ItemCallback<chatItem>(){
            override fun areItemsTheSame(oldItem: chatItem, newItem: chatItem): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(oldItem: chatItem, newItem: chatItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}