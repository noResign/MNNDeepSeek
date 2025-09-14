package com.example.mnndeepseek.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mnndeepseek.R
import com.example.mnndeepseek.databinding.ItemConversationDateBinding
import com.example.mnndeepseek.databinding.ItemConversationMeBinding
import com.example.mnndeepseek.databinding.ItemConversationYouBinding
import com.example.mnndeepseek.model.ChatData
import com.example.mnndeepseek.utils.inflate

class ConversationRecycleViewAdapter(
    private val items: MutableList<ChatData>,
    private val onUpdateCallback: (() -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder?>() {

    private var itemUpdateCount = 0

    override fun getItemViewType(position: Int): Int {
        return when(items[position].type) {
            "0" -> DATE
            "1" -> YOU
            "2" -> ME
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            DATE -> DateItemViewHolder(parent.inflate(R.layout.item_conversation_date))
            YOU -> YouItemViewHolder(parent.inflate(R.layout.item_conversation_you))
            ME -> MeItemViewHolder(parent.inflate(R.layout.item_conversation_me))
            else -> MeItemViewHolder(parent.inflate(R.layout.item_conversation_me))
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatData = items[position]
        when(getItemViewType(position)) {
            DATE -> (holder as DateItemViewHolder).bind(chatData)
            YOU -> (holder as YouItemViewHolder).bind(chatData)
            ME -> (holder as MeItemViewHolder).bind(chatData)
        }
    }

    fun addItem(item: ChatData) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun updateRecentItem(item: ChatData) {
        items[items.size - 1] = item
        notifyItemChanged(items.size - 1)
        ++itemUpdateCount
        if (itemUpdateCount % 10 == 0) {
            itemUpdateCount = 0
            onUpdateCallback?.invoke()
        }
    }


    private class MeItemViewHolder(itemView: View): ViewHolder(itemView) {
        val binding = ItemConversationMeBinding.bind(itemView)
        fun bind(data: ChatData) {
            binding.tvChatText.text = data.text
            binding.tvTime.text = data.time
        }
    }

    private class YouItemViewHolder(itemView: View): ViewHolder(itemView) {
        val binding = ItemConversationYouBinding.bind(itemView)
        fun bind(data: ChatData) {
            binding.tvChatText.text = data.text
        }
    }

    private class DateItemViewHolder(itemView: View): ViewHolder(itemView) {
        val binding = ItemConversationDateBinding.bind(itemView)
        fun bind(data: ChatData) {
            binding.tvDate.text = data.time
        }
    }


    companion object {
        private const val DATE = 0
        private const val YOU = 1
        private const val ME = 2
    }


}