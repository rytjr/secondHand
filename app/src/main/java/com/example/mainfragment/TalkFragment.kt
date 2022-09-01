package com.example.mainfragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mainfragment.DBKey.Companion.CHILD_CHAT
import com.example.mainfragment.DBKey.Companion.DB_USERS
import com.example.mainfragment.databinding.ChatBinding
import com.example.mainfragment.databinding.FragmentMyBinding

import com.example.mainfragment.databinding.FragmentSearchBinding
import com.example.mainfragment.databinding.FragmentTalkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient


class TalkFragment : Fragment(R.layout.fragment_talk) {


    private lateinit var binding: FragmentTalkBinding
    private lateinit var chatListAdapter: chatAdapter
    private val chatRoomList = mutableListOf<chatItem>()

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatlistBinding =FragmentTalkBinding.bind(view)

        binding = fragmentChatlistBinding

        initchartListAdapter()

        chatRoomList.clear()

        initChartRecyclerView()

        initChatDB()
    }

    private fun initchartListAdapter() {
        chatListAdapter = chatAdapter(onItemClicked = { chatRoom ->
            context?.let {
                val intent = Intent(it, chatActivity::class.java)
                intent.putExtra("chatKey", chatRoom.key) // 인텐트로 키를 전달해서 start
                startActivity(intent)
            }

        })
    }

    private fun initChartRecyclerView() {
        binding.recy1.adapter = chatListAdapter
        binding.recy1.layoutManager = LinearLayoutManager(context)
    }

    private fun initChatDB() {

        UserApiClient.instance.me { user, error ->

            val chatDB =
                Firebase.database.reference.child(DB_USERS).child(getString(R.string.f5, "${user?.kakaoAccount?.profile?.nickname}")).child(CHILD_CHAT)

            // db에 있는 채팅 리스트를 불러와 각각 리스트에 더해준다.
            chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val model = it.getValue(chatItem::class.java)
                        model ?: return
                        chatRoomList.add(model)
                    }
                    chatListAdapter.submitList(chatRoomList)
                    chatListAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        }
    }

    // view가 새로 그려졌을 때;
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }


}