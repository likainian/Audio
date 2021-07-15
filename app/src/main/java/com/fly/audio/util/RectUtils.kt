package com.fly.audio.util

import android.graphics.RectF

object RectUtils {
    @JvmStatic
    fun getCornersFromRect(r: RectF): FloatArray {
        return floatArrayOf(
            r.left, r.top,
            r.right, r.top,
            r.right, r.bottom,
            r.left, r.bottom
        )
    }

    @JvmStatic
    fun getRectSidesFromCorners(corners: FloatArray): FloatArray {
        return floatArrayOf(
            Math.sqrt(Math.pow(corners[0] - corners[2].toDouble(), 2.0) + Math.pow(corners[1] - corners[3].toDouble(), 2.0)).toFloat(),
            Math.sqrt(Math.pow(corners[2] - corners[4].toDouble(), 2.0) + Math.pow(corners[3] - corners[5].toDouble(), 2.0)).toFloat())
    }

    @JvmStatic
    fun getCenterFromRect(r: RectF): FloatArray {
        return floatArrayOf(r.centerX(), r.centerY())
    }

    @JvmStatic
    fun trapToRect(array: FloatArray): RectF {
        val r = RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
        var i = 1
        while (i < array.size) {
            val x = Math.round(array[i - 1] * 10) / 10f
            val y = Math.round(array[i] * 10) / 10f
            r.left = Math.min(x, r.left)
            r.top = Math.min(y, r.top)
            r.right = Math.max(x, r.right)
            r.bottom = Math.max(y, r.bottom)
            i += 2
        }
        r.sort()
        return r
    }
}