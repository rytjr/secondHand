package com.example.mainfragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainfragment.DBKey.Companion.CHILD_CHAT
import com.example.mainfragment.DBKey.Companion.DB_ARTICLES
import com.example.mainfragment.DBKey.Companion.DB_USERS


import com.example.mainfragment.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var articleDB: DatabaseReference
    private lateinit var userDB: DatabaseReference
    private lateinit var articleAdapter: AticleAdapter

    private val articleList = mutableListOf<Memo>()

    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(Memo::class.java)
            articleModel ?: return

            articleList.add(articleModel) // 리스트에 새로운 항목을 더해서;
            articleAdapter.submitList(articleList) // 어뎁터 리스트에 등록;
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}

    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private var binding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fragmentHomeBinding = FragmentHomeBinding.bind(view)

        binding = fragmentHomeBinding

        articleList.clear() //리스트 초기화;

        initDB()

        initArticleAdapter(view)

        initArticleRecyclerView()

        // 데이터 가져오기;
        initListener()

    }




    private fun initArticleRecyclerView() {
        binding?:return

        binding!!.recyclerVIew.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerVIew.adapter = articleAdapter
    }

    private fun initArticleAdapter(view: View) {
        articleAdapter = AticleAdapter (onItemClicked = { Memo ->

            UserApiClient.instance.me { user, error ->
                val chatRoom = chatItem(
                    chatting = getString(R.string.f5, "${user?.kakaoAccount?.profile?.nickname}"),
                    key = System.currentTimeMillis()
                )
                userDB.child(getString(R.string.f5, "${user?.kakaoAccount?.profile?.nickname}")) // 계속 워닝 떠서 !! 처리;
                    .child(CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                userDB.child(Memo.explane)
                    .child(CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

            }

        })
    }


    private fun initListener() {
        articleDB.addChildEventListener(listener)

    }


    private fun initDB() {
        articleDB = Firebase.database.reference.child(DB_ARTICLES) // 디비 가져오기;
        userDB = Firebase.database.reference.child(DB_USERS)
    }

    override fun onDestroy() {
        super.onDestroy()

        articleDB.removeEventListener(listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        articleAdapter.notifyDataSetChanged() // view 를 다시 그림;
    }



}