package com.fly.audio.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.fly.audio.api.UploadRepo
import com.fly.audio.util.FileUtil
import com.fly.core.base.BaseViewModel
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechSynthesizer
import com.iflytek.cloud.SynthesizerListener
import io.reactivex.Observable
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by likainian on 2021/7/13
 * Description:  上传录音文件，文字转录音，保存合成文件，获取保存列表
 */

class SpeechViewModel : BaseViewModel() {
    private val repo = UploadRepo()
    var uploadResult = MutableLiveData<String>()
    var speechResult = MutableLiveData<String>()
    var saveResult = MutableLiveData<String>()
    var recordList = MutableLiveData<String>() //录音列表

    fun upload(file:File){
        addDisposable(
            repo.upload(file)
                .subscribe({
                    uploadResult.postValue("上传成功")
                },{
                    uploadResult.postValue("上传失败（没有接口）")
                })
        )
    }

    fun startSpeaking(speechSynthesizer: SpeechSynthesizer,text: String){
        speechSynthesizer.startSpeaking(text,object:
            SynthesizerListener {
            override fun onSpeakBegin() {

            }

            override fun onBufferProgress(p0: Int, p1: Int, p2: Int, p3: String?) {

            }

            override fun onSpeakPaused() {

            }

            override fun onSpeakResumed() {

            }

            override fun onSpeakProgress(p0: Int, p1: Int, p2: Int) {

            }

            override fun onCompleted(p0: SpeechError?) {
                speechResult.postValue("合成完成")
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {

            }
        })
    }

    fun saveSpeaking(speechSynthesizer: SpeechSynthesizer,text: String,path: String){
        speechSynthesizer.synthesizeToUri(text,path,object:SynthesizerListener{
            override fun onSpeakBegin() {

            }

            override fun onBufferProgress(p0: Int, p1: Int, p2: Int, p3: String?) {

            }

            override fun onSpeakPaused() {

            }

            override fun onSpeakResumed() {

            }

            override fun onSpeakProgress(p0: Int, p1: Int, p2: Int) {

            }

            override fun onCompleted(p0: SpeechError?) {
                saveResult.postValue("合成完成")
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {

            }
        })
    }

    fun getPcmFiles(){
        addDisposable(
            Observable.timer(1, TimeUnit.SECONDS)
                .subscribe {
                    var paths = ""
                    val pcmFiles = FileUtil.getPcmFiles()
                    pcmFiles.forEach {
                        paths += it.absolutePath + "\n"
                    }
                    recordList.postValue(paths)
                }
        )

    }
}