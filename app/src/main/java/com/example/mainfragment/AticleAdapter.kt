package com.example.mainfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mainfragment.DBKey.Companion.DB_ARTICLES
import com.example.mainfragment.DBKey.Companion.DB_USERS
import com.example.mainfragment.databinding.TradetestBinding
import com.example.mainfragment.databinding.UerfileBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


class AticleAdapter(val onItemClicked: (Memo) -> Unit) : ListAdapter<Memo, AticleAdapter.ViewHolder>(diffUtil){



    inner class ViewHolder (private val binding: UerfileBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Memo){

            binding.fwe12.text=item.price
            binding.fwe13.text=item.explane

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val format = sdf.format(item.timestamp)
            binding.fwe14.text=format

            if(item.imagefrofiel.isNotEmpty()) {
                Glide.with(binding.fwe11)
                    .load(item.imagefrofiel)
                    .into(binding.fwe11)

            }
            binding.root.setOnClickListener {
                onItemClicked(item)
            }


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UerfileBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])

    }

    companion object{
        val diffUtil = object  : DiffUtil.ItemCallback<Memo>(){
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem == newItem
            }
        }
    }

}