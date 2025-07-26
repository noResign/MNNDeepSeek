package com.example.mnndeepseek

import java.io.Serializable

class Chat : Serializable {
    companion object {
        init {
            System.loadLibrary("mnn_deep_seek")
        }
    }

    external fun Init(modelDir: String): Boolean // 加载模型
    external fun Submit(input: String): String // 输入请求
    external fun Respose(): ByteArray // 模型输出
    external fun Done()
    external fun Reset()

}