package com.example.mnndeepseek.base

import com.example.mnndeepseek.adapter.ConversationRecycleViewAdapter
import com.example.mnndeepseek.databinding.FragmentAiHomeBinding
import com.example.mnndeepseek.fragment.AiHomeFragment
import com.example.mnndeepseek.jni.Chat

open class BaseAiHomeFragmentViewCallback(fragment: AiHomeFragment, val iCallback: ICallback): FragmentViewCallback(fragment, true) {

    protected val binding = iCallback.viewBinding()

    protected val listAdapter = iCallback.listAdapter()

    protected val mChat = iCallback.mChat()

    protected val viewModel = null




    interface ICallback {
        fun viewBinding():FragmentAiHomeBinding

        fun listAdapter():ConversationRecycleViewAdapter

        fun mChat(): Chat?

        fun scrollToEnd()
    }
}

