package com.example.mnndeepseek.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mnndeepseek.jni.Chat
import com.example.mnndeepseek.fragment.AiHomeFragment
import com.example.mnndeepseek.databinding.ActivityConversationBinding

class AiHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversationBinding
    private var mChat: Chat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mChat = intent.getSerializableExtra("chat") as Chat?

        supportFragmentManager.beginTransaction()
            .replace(binding.frgContainer.id, AiHomeFragment(mChat))
            .commit()
    }

}