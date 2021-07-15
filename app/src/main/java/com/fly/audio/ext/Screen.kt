package com.fly.audio.ext

import com.fly.core.base.appContext

/**
 * Created by likainian on 2021/7/13
 * Description:  屏幕的宽高
 */

object Screen {

    var width = 0 // 屏幕宽度
        private set

    /**
     * 屏幕高度: 包含statusBar不包含navigationBar的高度
     */
    var height = 0
        private set

    var density = 0f // 屏幕密度 0.75 / 1.0 / 1.5 / 2.0 / 3.0 / 4.0
        private set

    init {
        reset()
    }

    fun reset() {
        val metrics = appContext.resources.displayMetrics
        width = metrics.widthPixels
        height = metrics.heightPixels
        density = metrics.density
    }

}