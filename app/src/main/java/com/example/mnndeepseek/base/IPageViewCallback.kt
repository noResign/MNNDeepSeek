package com.example.mnndeepseek.base

import android.content.Intent
import android.os.Bundle
import java.util.Date

interface IPageViewCallback {

    fun initView(savedInstanceSta: Bundle?)

    fun initData()

    fun onCreate()

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    fun onNextResume()

    fun onDetach()

    /**
     * 返回按键，返回true表示拦截这次点击
     */
    fun onBackPressed(): Boolean

    /**
     * 页面刷新
     */
    fun onRefresh()

    /**
     * 页面回调
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, date: Intent)

}