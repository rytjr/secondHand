package com.example.mainfragment

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.example.mainfragment.databinding.ActivityMainBinding
import com.example.mainfragment.databinding.ActivitySubBinding

import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.common.util.Utility







class SubActivity : AppCompatActivity() {


    private var mBinding: ActivitySubBinding? = null
    private val binding get() = mBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)




            setFragment(0)        //프래그먼트 실행하는 구문

            binding.homebutton2.setOnClickListener{
                setFragment(0)
            }
            binding.searchbutton2.setOnClickListener{
                setFragment(1)
            }
            binding.talkbutton2.setOnClickListener{
                setFragment(2)
            }
            binding.mybutton2.setOnClickListener{
                setFragment(3)
            } //여기까지 프래그먼트 실행 버튼을 누구면 버튼에 연결된 페이지 실행




        }


        private fun setFragment(fragt:Int) {       //프래그먼트 메소드
            val ft=supportFragmentManager.beginTransaction()
            when(fragt){

                0->{
                    ft.replace(R.id.frame_layout1,HomeFragment()).commit()
                }
                1->{
                    ft.replace(R.id.frame_layout1,SearchFragment()).commit()
                }
                2->{
                    ft.replace(R.id.frame_layout1,TalkFragment()).commit()
                }
                3->{
                    ft.replace(R.id.frame_layout1,MyFragment()).commit()
                }

            }
        }              //여기까지 프래그먼트 메소드


        override fun onDestroy() {
            mBinding = null
            super.onDestroy()



        }

    }
