package com.example.mnndeepseek

import android.app.ComponentCaller
import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.mnndeepseek.activity.AiHomeActivity
import com.example.mnndeepseek.databinding.ActivityMainBinding
import com.example.mnndeepseek.jni.Chat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.jvm.Throws

class MainActivity : AppCompatActivity() {
    private var mChat: Chat? = null
    private var mIntent: Intent? = null

    private val mSearchPath = "/data/local/tmp/"
    private var mModelName = "DeepSeek-R1-Distill-Qwen-1.5B"
    private var mModelDir = "$mSearchPath$mModelName/"
    private lateinit var binding: ActivityMainBinding

    private val startActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                100 -> binding.processBar.isVisible = false
                101 -> {

                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mIntent = Intent(this, AiHomeActivity::class.java)

        binding.tvBtn.setOnClickListener {
            loadModel()
        }

    }

    private fun loadModel() {
        val ready = onCheckModels()
        binding.processBar.isVisible = true
        if (ready) {
            Thread {
                mChat = Chat()
                if (mChat?.init(mModelDir) == true) {
                    runOnUiThread {
                        mIntent?.putExtra("chat", mChat)
                        mIntent?.let {
                            startActivityResultLauncher.launch(it)
                        }
                        binding.processBar.isVisible = false
                    }
                } else {
                    Toast.makeText(this, "加载模型失败", Toast.LENGTH_SHORT).show()
                }
            }.start()
        } else {
            Toast.makeText(this, "模型不存在，加载失败： ", Toast.LENGTH_SHORT).show()
            binding.processBar.isVisible = false
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        when (resultCode) {
            100 -> binding.processBar.isVisible = false
            101 -> {

            }
        }
    }

    private fun onCheckModels(): Boolean {
        var modelReady = checkModelsReady()
        if (!modelReady) {
            try {
                mModelDir = copyAssetResource2File(this, mModelName)
                modelReady = checkModelsReady()
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
        return modelReady
    }

    private fun checkModelsReady(): Boolean {
        return File(mModelDir).exists()
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun copyAssetResource2File(activity: AppCompatActivity, assetsDir: String): String {
        val assetManager = activity.baseContext.assets
        // make output dir
        val outDir = activity.cacheDir.toString() + "/" + assetsDir
        val outPath = File(outDir)
        if (!outPath.exists()) {
            outPath.mkdirs()
        }
        // visit input files and copy
        val files = assetManager.list(assetsDir)
        val num = files?.size ?: 0
        val threads: Array<CopyThread?> = arrayOfNulls(num)
        for (i in 0 until num) {
            val assetsFile = files?.get(i)
            if (!File("$outDir/$assetsFile").exists()) continue
            threads[i] = CopyThread(assetManager, "$assetsDir/$assetsFile", "$outDir/$assetsFile")
            threads[i]?.start()
        }
        for (i in 0 until num) {
            if (threads[i] != null) {
                threads[i]?.join()
            }
        }
        return outDir
    }


    class CopyThread(private val mAsset: AssetManager, private val mSrcPath: String, private val mDstPath: String): Thread() {
        override fun run() {
            try {
                val inS = mAsset.open(mSrcPath)
                val outF = File(mDstPath)
                val outS = FileOutputStream(outF)
                var byteCount: Int
                val buffer = ByteArray(1024)
                while ((inS.read(buffer).also { byteCount = it }) != -1) {
                    outS.write(buffer, 0, byteCount)
                }
                outS.flush()
                inS.close()
                outS.close()
                outF.setReadable(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
