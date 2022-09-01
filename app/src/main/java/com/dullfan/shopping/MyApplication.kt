package com.dullfan.shopping

import android.app.Application
import com.dullfan.shopping.loadsir.EmptyCallback
import com.dullfan.shopping.loadsir.ErrorCallback
import com.dullfan.shopping.loadsir.LoadingCallback
import com.dullfan.shopping.loadsir.TimeoutCallback
import com.dullfan.shopping.skin.SkinManager
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //初始化换肤框架
        SkinManager.init(this)

        //MMKV初始化
        MMKV.initialize(this)

        //LoadSir初始化
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
            .addCallback(TimeoutCallback())
            .addCallback(LoadingCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }
}