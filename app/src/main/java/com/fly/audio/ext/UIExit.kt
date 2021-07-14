package com.fly.audio.ext

import android.content.res.Resources

fun Float.dp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Int.dp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Float.px2dp(): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (this * fontScale + 0.5f).toInt()
}

fun Int.px2dp(): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (this * fontScale + 0.5f).toInt()
}


