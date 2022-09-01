package com.example.mainfragment

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk

class kakaosero : Application() {

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "1c8069aeadf0210456d3e346b48ae6f4")
    }

}

