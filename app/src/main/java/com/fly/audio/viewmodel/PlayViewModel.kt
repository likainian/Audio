package com.fly.audio.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fly.audio.audio.AudioPlay
import com.fly.audio.util.FileUtil
import com.fly.core.base.BaseViewModel
import io.reactivex.Observable
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by likainian on 2021/7/13
 * Description:
 */

class PlayViewModel : BaseViewModel() {

    var recordList = MutableLiveData<List<File>>() //录音列表

    fun getPcmFiles(){
        addDisposable(
            Observable.timer(1,TimeUnit.SECONDS)
                .subscribe {
                    val list = ArrayList<File>()
                    val pcmFiles = FileUtil.getPcmFiles()
                    val wavFiles = FileUtil.getWavFiles()
                    list.addAll(pcmFiles)
                    list.addAll(wavFiles)
                    recordList.postValue(list)
                }
        )
    }

    fun play(file:File){
        //AudioTrack,只能播放pcm
        if(file.absolutePath.endsWith(".pcm")){
            AudioPlay.playPcm(file)
        }else{
            //MediaPlayer 可以播放多种格式的声音文件，例如 MP3，AAC，WAV，OGG，MIDI 等
            AudioPlay.play(file)
        }
    }

}