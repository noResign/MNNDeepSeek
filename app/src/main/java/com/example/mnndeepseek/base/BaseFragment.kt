package com.example.mnndeepseek.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class BaseFragment : Fragment() {
    protected var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeCreateView(savedInstanceState)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = onCreateViewBinding(inflater, container)
        mView = binding?.root
        return mView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initData()
    }

    protected open fun onCreateViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean = false): ViewBinding? {
        return null
    }

    open fun initView(savedInstanceState: Bundle?) {}
    open fun initData() {}
    open fun beforeCreateView(savedInstanceState: Bundle?) {}


}