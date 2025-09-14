package com.example.mnndeepseek.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.mnndeepseek.jni.Chat
import com.example.mnndeepseek.adapter.ConversationRecycleViewAdapter
import com.example.mnndeepseek.base.BaseAiHomeFragmentViewCallback
import com.example.mnndeepseek.base.BaseFragment
import com.example.mnndeepseek.base.PageViewDelegate
import com.example.mnndeepseek.callbacks.AiHomeListViewCallback
import com.example.mnndeepseek.databinding.FragmentAiHomeBinding
import com.example.mnndeepseek.model.ChatData
import java.text.SimpleDateFormat
import java.util.Date

class AiHomeFragment(private var mChat: Chat? = null) : BaseFragment() {

    private lateinit var binding: FragmentAiHomeBinding
    var isUserScrolling = false
    private lateinit var callback: Callback
    private lateinit var mAdapter: ConversationRecycleViewAdapter


    private val pageViewDelegate = PageViewDelegate()

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): ViewBinding {
        binding = FragmentAiHomeBinding.inflate(inflater, parent, attachToParent)
        callback = Callback()
        mAdapter = ConversationRecycleViewAdapter(getInitData()) {
            if (!isUserScrolling) {
                callback.scrollToEnd()
            }
        }
        return binding
    }

    override fun initView(savedInstanceState: Bundle?) {
        registerCallbacks()
        pageViewDelegate.initView(savedInstanceState)
    }

    override fun initData() {
        super.initData()
        pageViewDelegate.initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        pageViewDelegate.onDestroy()
    }

    fun onBackPressed(): Boolean {
        return pageViewDelegate.onBackPressed()
    }

    private fun registerCallbacks() {
        listOfNotNull(
            AiHomeListViewCallback(this, callback)
        ).forEach {
            pageViewDelegate.addCallback(it)
        }
    }

    private fun getInitData(): MutableList<ChatData> {
        val data: MutableList<ChatData> = ArrayList()
        data.add(ChatData(SimpleDateFormat("yyyy-MM-dd").format(Date()), "0", ""))
        data.add(ChatData(SimpleDateFormat("hh:mm aa")?.format(Date()) ?: "--", "1", "您好，我是deepseek，欢迎向我提问。"))
        return data
    }


    inner class Callback: BaseAiHomeFragmentViewCallback.ICallback {
        override fun viewBinding(): FragmentAiHomeBinding {
            return binding
        }

        override fun listAdapter(): ConversationRecycleViewAdapter {
            return mAdapter
        }

        override fun mChat(): Chat? {
            return mChat
        }

        override fun scrollToEnd() {
            binding.recyclerView.post {
                val pos = mAdapter.itemCount - 1
                if (pos >= 0) {
                    binding.recyclerView.scrollToPosition(pos)
                }
            }
        }
    }


}