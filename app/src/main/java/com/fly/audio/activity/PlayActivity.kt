package com.fly.audio.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fly.audio.adapter.PlayAdapter
import com.fly.audio.databinding.ActivityPlayBinding
import com.fly.audio.util.PermissionUtil
import com.fly.audio.viewmodel.PlayViewModel
import com.fly.core.base.bindbase.BaseBindActivity

/**
 * Created by likainian on 2021/7/13
 * Description:  播放列表页面
 */

class PlayActivity : BaseBindActivity<ActivityPlayBinding>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PlayActivity::class.java))
        }
    }

    override fun createBinding(): ActivityPlayBinding {
        return ActivityPlayBinding.inflate(layoutInflater).apply {
            owner = this@PlayActivity
        }
    }

    override fun initView() {
        PermissionUtil.checkAudio(this)
    }

    private val vm by lazy {
        ViewModelProvider(this).get(PlayViewModel::class.java)
    }

    val layoutManager by lazy {
        LinearLayoutManager(this)
    }

    val adapter by lazy {
        PlayAdapter()
            .apply {
                onPlayClickListener = {
                    vm.play(it)
                }
            }
    }

    override fun initData() {
        vm.getPcmFiles()
        showLoading()
    }

    override fun registerObserver() {
        vm.recordList.observe(this, {
            hideLoading()
            adapter.setNewData(it.toMutableList())
        })
    }
}