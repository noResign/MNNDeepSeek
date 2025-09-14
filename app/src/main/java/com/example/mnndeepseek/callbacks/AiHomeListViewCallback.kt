package com.example.mnndeepseek.callbacks

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mnndeepseek.base.BaseAiHomeFragmentViewCallback
import com.example.mnndeepseek.fragment.AiHomeFragment
import com.example.mnndeepseek.model.ChatData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import kotlin.math.abs

class AiHomeListViewCallback(fragment: AiHomeFragment, iCallback: ICallback): BaseAiHomeFragmentViewCallback(fragment, iCallback) {
    private var mDataFormat: SimpleDateFormat? = null
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private var linearLayoutManager: LinearLayoutManager? = null


    override fun initView(savedInstanceSta: Bundle?) {
        super.initView(savedInstanceSta)
        mDataFormat = SimpleDateFormat("hh:mm aa")
        linearLayoutManager = LinearLayoutManager(fragment.context)
        binding.recyclerView.layoutManager = linearLayoutManager

        binding.recyclerView.adapter = listAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (abs(dy) > 0) {
                    (fragment as AiHomeFragment).isUserScrolling = true
                }
            }
        })

        binding.etMessage.setOnClickListener {
            smoothScrollToBottom()
        }

        binding.btSend.setOnClickListener {
            handleSendClick()
        }

    }

    private fun smoothScrollToBottom() {
        binding.recyclerView.post {
            listAdapter.let { adapter ->
                val pos = adapter.itemCount - 1
                binding.recyclerView.scrollToPosition(pos)
            }
        }
    }

    private fun handleSendClick() {
        val inputStr = binding.etMessage.text.toString().trim { it <= ' ' }
        if (inputStr.isNotBlank()) {
            val combinedInput = inputStr
            addUserMessage(combinedInput)
            binding.etMessage.text.clear()
            if (inputStr == "/reset") {
                mChat?.reset()
            } else {
                addBotResponsePlaceholder()
                val finalCombinedInput = combinedInput
                executor.execute { handleBotResponse(finalCombinedInput) }
            }
        }
    }

    private fun addBotResponsePlaceholder() {
        listAdapter.addItem(ChatData(mDataFormat?.format(Date()) ?: "--", "1", ""))
        smoothScrollToBottom()
    }

    private fun handleBotResponse(input: String?) {
        input?.let {
            mChat?.submit(it)
        }
        var lastResponse = ""
        while (!lastResponse.contains("<eop>")) {
            try {
                Thread.sleep(50)
                val response = String(mChat?.respose() ?: ByteArray(0))
                if (response != lastResponse) {
                    lastResponse = response
                    lifecycleScope.launch {
                        updateBotResponse(
                            response.replaceFirst(
                                "<eop>".toRegex(),
                                ""
                            )
                        )
                    }
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
        mChat?.done()
    }

    private fun updateBotResponse(responseText: String) {
        listAdapter.updateRecentItem(ChatData(mDataFormat?.format(Date()) ?: "--", "1", responseText))
    }

    private fun addUserMessage(message: String) {
        val userData = ChatData(mDataFormat?.format(Date()) ?: "--", "2", message)
        listAdapter.addItem(userData)
        smoothScrollToBottom()
    }

    override fun initData() {
        super.initData()
        Log.i("renbin", "AiHomeListViewCallback initdata")
    }

}