package com.example.mnndeepseek.jni

import java.io.Serializable

class Chat : Serializable {
    companion object {
        init {
            System.loadLibrary("mnn_deep_seek")
        }
    }

    external fun init(modelDir: String): Boolean // 加载模型
    external fun submit(input: String): String // 输入请求
    external fun respose(): ByteArray // 模型输出
    external fun done()
    external fun reset()

}