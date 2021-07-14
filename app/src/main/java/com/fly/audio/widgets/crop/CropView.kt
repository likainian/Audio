package com.fly.audio.widgets.crop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.fly.audio.R
import com.fly.audio.widgets.crop.image.GestureCropImageView

class CropView constructor(context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val mGestureCropImageView: GestureCropImageView
    private val mOverlayView: OverlayView
    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    fun setCropRate(ratio: Float) {
        mGestureCropImageView.setTargetAspectRatio(ratio)
        mOverlayView.setTargetAspectRatio(ratio)
    }

    fun getCropRate(): Float {
        return mGestureCropImageView.mTargetAspectRatio
    }

    fun getScale(): Float {
        return mGestureCropImageView.currentScale
    }

    fun setScale(scale: Float) {
        mGestureCropImageView.postScale(scale / mGestureCropImageView.currentScale,
            mGestureCropImageView.mCropRect.centerX(), mGestureCropImageView.mCropRect.centerY())
    }

    fun setDegrees(degrees: Float) {
        mGestureCropImageView.postRotate(degrees)
        mGestureCropImageView.setImageToWrapCropBounds()
    }

    fun getDegrees(): Float{
        return mGestureCropImageView.currentAngle
    }

    fun setPicture(bitmap: Bitmap) {
        mGestureCropImageView.setImagePicture(bitmap)
    }

    fun cropPicture(): Bitmap? {
        return mGestureCropImageView.cropImage()
    }

    fun setImageMatrix(matrix: Matrix) {
        mGestureCropImageView.setImageMatrix(matrix)
    }

    fun getImageMatrix(): Matrix {
        return Matrix(mGestureCropImageView.mCurrentImageMatrix)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.crop_view, this, true)
        mGestureCropImageView = findViewById(R.id.image_view_crop)
        mOverlayView = findViewById(R.id.view_overlay)
    }
}