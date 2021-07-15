package com.fly.audio.widgets.crop.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.fly.audio.util.RectUtils.getCenterFromRect
import com.fly.audio.util.RectUtils.getCornersFromRect

/**
 * Created by likainian on 2021/7/13
 * Description:  图片矩阵变换
 */

open class TransformImageView constructor(context: Context,attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    protected val mCurrentImageCorners = FloatArray(8)
    protected val mCurrentImageCenter = FloatArray(2)
    private val mMatrixValues = FloatArray(9)
    var mCurrentImageMatrix = Matrix()
    protected var mThisWidth = 0
    protected var mThisHeight = 0
    private var mInitialImageCorners =  FloatArray(0)
    private var mInitialImageCenter = FloatArray(0)
    protected var mBitmapLaidOut = false
    lateinit var viewBitmap: Bitmap

    fun setImagePicture(bitmap: Bitmap) {
        viewBitmap = bitmap
        setImageBitmap(bitmap)
    }

    val currentScale: Float
        get() = getMatrixScale(mCurrentImageMatrix)

    private fun getMatrixScale(matrix: Matrix): Float {
        return Math.sqrt(
            Math.pow(getMatrixValue(matrix, Matrix.MSCALE_X).toDouble(), 2.0)
                + Math.pow(getMatrixValue(matrix, Matrix.MSKEW_Y).toDouble(), 2.0)).toFloat()
    }

    val currentAngle: Float
        get() = getMatrixAngle(mCurrentImageMatrix)

    private fun getMatrixAngle(matrix: Matrix): Float {
        return (-(Math.atan2(
            getMatrixValue(matrix, Matrix.MSKEW_X).toDouble(),
            getMatrixValue(matrix, Matrix.MSCALE_X).toDouble()) * (180 / Math.PI))).toFloat()
    }

    override fun setImageMatrix(matrix: Matrix) {
        super.setImageMatrix(matrix)
        mCurrentImageMatrix.set(matrix)
        updateCurrentImagePoints()
    }

    fun postTranslate(deltaX: Float, deltaY: Float) {
        if (deltaX != 0f || deltaY != 0f) {
            mCurrentImageMatrix.postTranslate(deltaX, deltaY)
            imageMatrix = mCurrentImageMatrix
        }
    }

    fun postScale(deltaScale: Float, px: Float, py: Float) {
        if (deltaScale != 0f) {
            mCurrentImageMatrix.postScale(deltaScale, deltaScale, px, py)
            imageMatrix = mCurrentImageMatrix
        }
    }

    fun postRotate(deltaAngle: Float, px: Float, py: Float) {
        if (deltaAngle != 0f) {
            mCurrentImageMatrix.postRotate(deltaAngle, px, py)
            imageMatrix = mCurrentImageMatrix
        }
    }

    init {
        scaleType = ScaleType.MATRIX
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed || !mBitmapLaidOut) {
            mThisWidth = width - paddingRight - paddingLeft
            mThisHeight = height - paddingBottom - paddingTop
            onImageLaidOut()
        }
    }

    protected open fun onImageLaidOut() {
        val drawable = drawable ?: return
        val w = drawable.intrinsicWidth.toFloat()
        val h = drawable.intrinsicHeight.toFloat()
        val initialImageRect = RectF(0f, 0f, w, h)
        mInitialImageCorners = getCornersFromRect(initialImageRect)
        mInitialImageCenter = getCenterFromRect(initialImageRect)
        mBitmapLaidOut = true
    }

    private fun getMatrixValue(matrix: Matrix, valueIndex: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[valueIndex]
    }

    private fun updateCurrentImagePoints() {
        mCurrentImageMatrix.mapPoints(mCurrentImageCorners, mInitialImageCorners)
        mCurrentImageMatrix.mapPoints(mCurrentImageCenter, mInitialImageCenter)
    }
}