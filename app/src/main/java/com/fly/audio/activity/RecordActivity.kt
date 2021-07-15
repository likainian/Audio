package com.fly.audio.activity

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import com.fly.audio.databinding.ActivityRecordBinding
import com.fly.audio.databing.onbind.Bind
import com.fly.audio.ext.toast
import com.fly.audio.util.PermissionUtil
import com.fly.audio.viewmodel.RecordViewModel
import com.fly.core.base.bindbase.BaseBindActivity


/**
 * Created by likainian on 2021/7/13
 * Description:录音界面
 */

class RecordActivity : BaseBindActivity<ActivityRecordBinding>() {

    companion object{
        //java调用添加注解@JvmStatic
        fun startActivity(context: Context){
            context.startActivity(Intent(context,RecordActivity::class.java))
        }
    }

    val vm by lazy {
        ViewModelProvider(this).get(RecordViewModel::class.java)
    }

    override fun createBinding(): ActivityRecordBinding {
        val binding = ActivityRecordBinding.inflate(layoutInflater)
        binding.owner = this
        return binding
    }

    override fun initView() {
        PermissionUtil.checkAudio(this)
    }

    override fun initData() {
        vm.getAudioFiles()
    }

    val onTouchRecord = Bind.onTouch { _, event ->
        if(PermissionUtil.checkAudio(this)){
            when (event.action) {
                MotionEvent.ACTION_DOWN->{
                    vm.startRecord()
                    vm.startTime()
                }
                MotionEvent.ACTION_UP->{
                    vm.stopRecord()
                    vm.stopTime()
                    vm.getAudioFiles()
                }
            }
        }else{
            "没有录音权限".toast()
        }

        return@onTouch true
    }

    override fun registerObserver() {
        vm.recordTime.observe(this, {
            mViewDataBinding.progressView.setProgress(it*10)
        })
    }

}