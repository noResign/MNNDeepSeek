# 基于MNN的Android端部署DeepSeek

## 1 介绍
> 博客： [android端部署DeepSeek](https://juejin.cn/post/7485199344942727209?searchId=20250608173907D6B0C87218F49502D79C)

## 2 环境安装

### 2.1 MNN转化工具

```
cd MNN
mkdir build && cd build
cmake .. -DMNN_BUILD_CONVERTER=ON
make -j8
```

### 2.2 模型转换工具

```
cd path/MNN/transformers/llm/export
pip install -r requirements.txt
```

### 2.3 模型下载

```
git lfs install
git clone https://www.modelscope.cn/deepseek-ai/DeepSeek-R1-Distill-Qwen-1.5B
```

### 2.4 模型转换

```
python llmexport.py --path path/model/DeepSeek-R1-Distill-Qwen-1.5B --export mnn
```

### 2.5 模型推送

```
 adb push path/DeepSeek-R1-Distill-Qwen-1.5B /data/local/tmp
```

## 3 参考

- https://github.com/alibaba/MNN
- https://github.com/wangzhaode/mnn-llm
