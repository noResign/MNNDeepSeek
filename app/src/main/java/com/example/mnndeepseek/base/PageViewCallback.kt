package com.example.mnndeepseek.base

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver

abstract class PageViewCallback : IPageViewCallback {

    override fun initView(savedInstanceSta: Bundle?) {}

    override fun initData() {}

    override fun onCreate() {}

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {}

    override fun onDetach() {}

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun onNextResume() {}

    override fun onRefresh() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, date: Intent) {}

    protected val lifecycleObserver: LifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate()
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            else -> {}
        }

    }

}