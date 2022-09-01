package com.example.mainfragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mainfragment.databinding.FragmentMyBinding

import com.example.mainfragment.databinding.FragmentSearchBinding
import com.kakao.sdk.user.UserApiClient


class MyFragment : Fragment() {


    lateinit var binding: FragmentMyBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)



        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                binding.nickname.text = getString(R.string.f5, "${user.kakaoAccount?.profile?.nickname}")
            }
        }


        binding.logout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(context, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                    activity?.finish()
                }
            }
        }


            binding.run.setOnClickListener {
                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        Toast.makeText(context, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                        activity?.finish()
                    }
                }
            }

            return binding.root


        }
    }

