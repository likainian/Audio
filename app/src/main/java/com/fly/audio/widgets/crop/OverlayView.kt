package com.fly.audio.widgets.crop

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.fly.audio.ext.dp2px

class OverlayView @JvmOverloads constructor(context: Context?,
    attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {
    private val mCropViewRect = RectF()
    private var mThisWidth = 0
    private var mThisHeight = 0
    private var mGridPoints = FloatArray(0)
    private var mTargetAspectRatio = 0f
    private val mCropGridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCropFramePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mMaskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCropPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mShouldSetupCropBounds = false

    init {
        mMaskPaint.setARGB(80, 0, 0, 0)
        mCropPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mCropFramePaint.strokeWidth = 2.dp2px().toFloat()
        mCropFramePaint.setARGB(255, 255, 255, 255)
        mCropFramePaint.style = Paint.Style.STROKE
        mCropGridPaint.strokeWidth = 1.dp2px().toFloat()
        mCropGridPaint.setARGB(255, 255, 255, 255)
    }

    fun setTargetAspectRatio(targetAspectRatio: Float) {
        mTargetAspectRatio = targetAspectRatio
        if (mThisWidth > 0) {
            setupCropBounds()
            postInvalidate()
        } else {
            mShouldSetupCropBounds = true
        }
    }

    private fun setupCropBounds() {
        val height = (mThisWidth / mTargetAspectRatio).toInt()
        if (height > mThisHeight) {
            val width = (mThisHeight * mTargetAspectRatio).toInt()
            val halfDiff = (mThisWidth - width) / 2
            mCropViewRect[paddingLeft + halfDiff.toFloat(), paddingTop.toFloat(),
                paddingLeft + width + halfDiff.toFloat()] = paddingTop + mThisHeight.toFloat()
        } else {
            val halfDiff = (mThisHeight - height) / 2
            mCropViewRect[paddingLeft.toFloat(), paddingTop + halfDiff.toFloat(),
                paddingLeft + mThisWidth.toFloat()] = paddingTop + height + halfDiff.toFloat()
        }
        mGridPoints = FloatArray(0)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            mThisWidth = width - paddingRight - paddingLeft
            mThisHeight = height - paddingBottom - paddingTop
            if (mShouldSetupCropBounds) {
                mShouldSetupCropBounds = false
                setTargetAspectRatio(mTargetAspectRatio)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawMask(canvas)
        drawCropGrid(canvas)
    }
    private fun drawMask(canvas: Canvas) {
        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mMaskPaint)
        canvas.drawRect(mCropViewRect, mCropPaint)
        canvas.restoreToCount(layerId)
    }

    private fun drawCropGrid(canvas: Canvas) {
        if (mGridPoints.isEmpty() && !mCropViewRect.isEmpty) {
            mGridPoints = FloatArray(mCropGridRowCount * 4 + mCropGridColumnCount * 4)
            var index = 0
            for (i in 0 until mCropGridRowCount) {
                mGridPoints[index++] = mCropViewRect.left
                mGridPoints[index++] = mCropViewRect.height() * ((i.toFloat() + 1.0f) /
                    (mCropGridRowCount + 1).toFloat()) + mCropViewRect.top
                mGridPoints[index++] = mCropViewRect.right
                mGridPoints[index++] = mCropViewRect.height() * ((i.toFloat() + 1.0f) /
                    (mCropGridRowCount + 1).toFloat()) + mCropViewRect.top
            }
            for (i in 0 until mCropGridColumnCount) {
                mGridPoints[index++] = mCropViewRect.width() * ((i.toFloat() + 1.0f) /
                    (mCropGridColumnCount + 1).toFloat()) + mCropViewRect.left
                mGridPoints[index++] = mCropViewRect.top
                mGridPoints[index++] = mCropViewRect.width() * ((i.toFloat() + 1.0f) /
                    (mCropGridColumnCount + 1).toFloat()) + mCropViewRect.left
                mGridPoints[index++] = mCropViewRect.bottom
            }
        }
        canvas.drawLines(mGridPoints, mCropGridPaint)
        canvas.drawRect(mCropViewRect, mCropFramePaint)
    }

    companion object {
        private const val mCropGridRowCount = 2
        private const val mCropGridColumnCount = 2
    }
}