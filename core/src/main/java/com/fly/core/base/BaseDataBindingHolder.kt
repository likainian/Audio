package com.fly.core.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder
/**
 * Created by likainian on 2021/7/13
 * Description:  bind的viewHolder
 */

class BaseDataBindingHolder(view: View) : BaseViewHolder(view) {
    var dataBing: ViewDataBinding? = null

    init {
        dataBing = DataBindingUtil.bind(itemView)
    }
}