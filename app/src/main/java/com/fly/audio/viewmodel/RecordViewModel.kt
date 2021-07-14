package com.fly.audio.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fly.audio.audio.AudioRecorder
import com.fly.audio.ext.applyScheduler
import com.fly.audio.util.FileUtil
import com.fly.core.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * Created by likainian on 2021/7/13
 * Description:
 */

class RecordViewModel : BaseViewModel() {

    var recordList = MutableLiveData<String>() //录音列表
    var recordStatus = MutableLiveData<Boolean>() //录音状态
    var recordTime = MutableLiveData<Int>() //录音时间
    var recordWords = MutableLiveData("按住录音") //录音按钮

    private var subscribe: Disposable? = null


    fun startRecord(){
        recordStatus.postValue(true)
        recordWords.postValue("松开停止")
        AudioRecorder.getInstance().startRecord()
    }

    fun stopRecord(){
        if(recordStatus.value==true){
            recordStatus.postValue(false)
            recordWords.postValue("按住录音")
            AudioRecorder.getInstance().stopRecord()
        }
    }

    fun startTime(){
        recordTime.postValue(0)
        subscribe = Observable
            .interval(1, TimeUnit.SECONDS)
            .applyScheduler()
            .subscribe {
                recordTime.postValue(it.toInt())
                if(it==10L){
                    stopRecord()
                    stopTime()
                }
            }
    }

    fun stopTime(){
        subscribe?.dispose()
    }

    fun getPcmFiles(){
        addDisposable(
            Observable.timer(1,TimeUnit.SECONDS)
                .subscribe {
                    var paths = ""
                    val pcmFiles = FileUtil.getPcmFiles()
                    pcmFiles.forEach {
                        paths += it.absolutePath + "\n"
                    }
                    val wavFiles = FileUtil.getWavFiles()
                    wavFiles.forEach {
                        paths += it.absolutePath + "\n"
                    }
                    recordList.postValue(paths)
                }
        )

    }

}