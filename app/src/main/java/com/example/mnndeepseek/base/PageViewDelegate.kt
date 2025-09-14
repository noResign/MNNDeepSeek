package com.example.mnndeepseek.base

import android.content.Intent
import android.os.Bundle

class PageViewDelegate : PageViewCallback() {

    protected val mCallbacks: MutableList<IPageViewCallback> = mutableListOf()

    fun addCallback(callback: IPageViewCallback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback)
        }
    }

    fun removeCallback(callback: IPageViewCallback) {
        callback.onDetach()
        mCallbacks.remove(callback)
    }

    override fun initView(savedInstanceSta: Bundle?) {
        super.initView(savedInstanceSta)
        for (callback in mCallbacks) {
            callback.initView(savedInstanceSta)
        }
    }

    override fun initData() {
        super.initData()
        for (callback in mCallbacks) {
            callback.initData()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        for (callback in mCallbacks) {
            callback.onRefresh()
        }
    }

    override fun onBackPressed(): Boolean {
        for (callback in mCallbacks) {
            if (callback.onBackPressed()) return true
        }
        return super.onBackPressed()
    }

    override fun onNextResume() {
        super.onNextResume()
        for (callback in mCallbacks) {
            callback.onNextResume()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, date: Intent) {
        super.onActivityResult(requestCode, resultCode, date)
        for (callback in mCallbacks) {
            callback.onActivityResult(requestCode, resultCode, date)
        }
    }
}