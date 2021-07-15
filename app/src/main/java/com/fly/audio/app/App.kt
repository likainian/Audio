package com.fly.audio.app

import com.fly.core.base.BaseApplication
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility

/**
 * Created by likainian on 2021/7/14
 * Description: app入口
 */

class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=24259071")
    }

}