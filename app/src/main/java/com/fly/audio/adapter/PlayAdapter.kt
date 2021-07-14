package com.fly.audio.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.fly.audio.R
import com.fly.audio.databinding.ItemPlayBinding
import com.fly.core.base.BaseDataBindingHolder
import java.io.File

/**
 * Created by likainian on 2021/7/14
 * Description:
 */

class PlayAdapter : BaseQuickAdapter<File, BaseDataBindingHolder>(R.layout.item_play) {

    var onPlayClickListener: ((path: File) -> Unit)? = null

    override fun convert(helper: BaseDataBindingHolder, item: File) {
        val itemBinding = helper.dataBing as ItemPlayBinding
        itemBinding.item = item
        itemBinding.tvPlay.setOnClickListener {
            onPlayClickListener?.invoke(item)
        }

        itemBinding.executePendingBindings()
    }
}