package com.fly.audio.activity

import com.fly.audio.databinding.ActivityMainBinding
import com.fly.audio.databing.onbind.Bind
import com.fly.core.base.bindbase.BaseBindActivity

class MainActivity : BaseBindActivity<ActivityMainBinding>() {

    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)
        .apply {
            owner = this@MainActivity
        }

    val onClickRecord = Bind.onClick {
        RecordActivity.startActivity(this)
    }

    val onClickPlay = Bind.onClick {
        PlayActivity.startActivity(this)
    }
    val onClickCrop = Bind.onClick {
        CropActivity.startActivity(this)
    }
}