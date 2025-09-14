package com.example.mnndeepseek.model

import android.net.Uri

data class ChatData(
    val time: String = "",
    val type: String = "",
    val text: String = "",
    var imageUri: Uri? = null
)