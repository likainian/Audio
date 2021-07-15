package com.fly.audio.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fly.audio.databinding.ActivitySpeechBinding
import com.fly.audio.databing.onbind.Bind
import com.fly.audio.ext.toast
import com.fly.audio.util.FileUtil
import com.fly.audio.util.PermissionUtil
import com.fly.audio.viewmodel.SpeechViewModel
import com.fly.core.base.bindbase.BaseBindActivity
import com.iflytek.cloud.SpeechSynthesizer
import java.io.File

/**
 * Created by likainian on 2021/7/15
 * Description:合成音频页面
 */

class SpeechActivity : BaseBindActivity<ActivitySpeechBinding>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SpeechActivity::class.java))
        }
    }

    override fun createBinding(): ActivitySpeechBinding =
        ActivitySpeechBinding.inflate(layoutInflater)
            .apply {
                owner = this@SpeechActivity
            }

    override fun initView() {
        PermissionUtil.checkAudio(this)
    }

    val vm by lazy {
        ViewModelProvider(this).get(SpeechViewModel::class.java)
    }

    private val createSynthesizer by lazy {
        SpeechSynthesizer.createSynthesizer(this,{})
    }


    val onClickPlay = Bind.onClick {
        vm.startSpeaking(createSynthesizer,mViewDataBinding.etText.text.toString())
    }
    val onClickSave = Bind.onClick {
        val pcm = FileUtil.getPcmFileAbsolutePath(System.currentTimeMillis().toString() + "HC")
        vm.saveSpeaking(createSynthesizer,mViewDataBinding.etText.text.toString(),pcm)
    }

    val onClickUpload = Bind.onClick {
        val path = FileUtil.getPcmFileAbsolutePath(System.currentTimeMillis().toString() + "HC")
        vm.upload(File(path))
    }

    override fun registerObserver() {
        vm.uploadResult.observe(this, Observer {
            it.toast()
        })
        vm.speechResult.observe(this, Observer {
            it.toast()
        })
        vm.saveResult.observe(this, Observer {
            it.toast()
            vm.getPcmFiles()
        })
    }
}