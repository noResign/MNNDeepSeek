#include <android/asset_manager_jni.h>
#include <android/bitmap.h>
#include <android/log.h>

#include <jni.h>
#include <string>
#include <vector>
#include <sstream>
#include <thread>

#include "MNN/llm.hpp"

#ifndef LOG_TAG
#define LOG_TAG "MNNDeepSeek"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,LOG_TAG ,__VA_ARGS__) // 定义LOGF类型
#endif



static std::unique_ptr<MNN::Transformer::Llm> llm(nullptr);
static std::stringstream response_buffer;

extern "C" {

// 模型加载
JNIEXPORT jboolean JNICALL
Java_com_example_mnndeepseek_jni_Chat_init(JNIEnv *env, jobject thiz, jstring modelDir) {
    const char* model_dir = env->GetStringUTFChars(modelDir, 0);
    if (!llm.get()) {
        llm.reset(MNN::Transformer::Llm::createLLM(model_dir));
        try {
            llm->load();
        } catch (const std::exception& e) {
            LOGI("=== 异常：%s ====", e.what());
            return JNI_FALSE;
        }
    }
    return JNI_TRUE;
}

// 将问题输入模型
JNIEXPORT jstring JNICALL
Java_com_example_mnndeepseek_jni_Chat_submit(JNIEnv *env, jobject thiz, jstring inputStr) {
    if (!llm.get()) {
        return env->NewStringUTF("Failed, Chat is not ready!");
    }
    const char* input_str = env->GetStringUTFChars(inputStr, 0);
    auto chat = [&](std::string str) {
        llm->response(str, &response_buffer, "<eop>");
    };
    std::thread chat_thread(chat, input_str); //子线程运行
    chat_thread.detach();
    jstring result = env->NewStringUTF("Submit success!");
    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_example_mnndeepseek_jni_Chat_respose(JNIEnv *env, jobject thiz) {
    auto len = response_buffer.str().size();
    jbyteArray res = env->NewByteArray(len);
    env->SetByteArrayRegion(res, 0, len, (const jbyte*)response_buffer.str().c_str());
    return res;
}

JNIEXPORT void JNICALL
Java_com_example_mnndeepseek_jni_Chat_done(JNIEnv *env, jobject thiz) {
    response_buffer.str("");
}

JNIEXPORT void JNICALL
Java_com_example_mnndeepseek_jni_Chat_reset(JNIEnv *env, jobject thiz) {
    llm->reset();
}

} // extern "C"