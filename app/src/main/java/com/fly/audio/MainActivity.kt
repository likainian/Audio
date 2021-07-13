package com.fly.audio

import android.os.Bundle
import com.fly.audio.databinding.ActivityMainBinding
import com.fly.core.base.bindbase.BaseBindActivity

class MainActivity : BaseBindActivity<ActivityMainBinding>() {

    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}