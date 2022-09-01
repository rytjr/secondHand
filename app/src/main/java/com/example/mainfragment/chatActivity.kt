package com.example.mainfragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mainfragment.DBKey.Companion.DB_CHAT
import com.example.mainfragment.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import kotlin.properties.Delegates

class chatActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val chatList = mutableListOf<chatitem2>()

    private val adapter = chattingAdapter()
    lateinit var chatDB : DatabaseReference
    var chatKey by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chatKey = intent.getLongExtra("chatKey", -1)

        initChatDB()

        initChatListRecyclerView()

        initSendButton()

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }



    private fun initChatDB() {

        chatDB = Firebase.database.reference.child(DB_CHAT).child("$chatKey")

        chatDB.addChildEventListener(object: ChildEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val chatItem = snapshot.getValue(chatitem2::class.java)
                chatItem?:return

                // 채팅 추가;
                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })

    }

    private fun initChatListRecyclerView() {
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
    }

    private fun initSendButton() {
        binding.fly.setOnClickListener {

            UserApiClient.instance.me { user, error ->

                val chatItem = chatitem2(
                    senderId = getString(R.string.f10, "${user?.kakaoAccount?.profile?.nickname}"),
                    message = binding.edit.text.toString()
                )


                chatDB.push().setValue(chatItem)
            }
        }
    }
}