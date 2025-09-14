package com.example.mnndeepseek.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes res: Int, attachRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(res, this, attachRoot)
}