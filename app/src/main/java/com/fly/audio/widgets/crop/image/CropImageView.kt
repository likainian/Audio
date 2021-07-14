package com.fly.audio.widgets.crop.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import com.fly.audio.util.RectUtils.getCornersFromRect
import com.fly.audio.util.RectUtils.getRectSidesFromCorners
import com.fly.audio.util.RectUtils.trapToRect

open class CropImageView constructor(context: Context,attrs: AttributeSet?) : TransformImageView(context,attrs) {
    val mCropRect = RectF()
    private val mTempMatrix = Matrix()
    var mTargetAspectRatio = 0f
    fun cropImage(): Bitmap? {
        setImageToWrapCropBounds()
        var mViewBitmap = viewBitmap
        if (currentAngle != 0.0f) {
            val tempMatrix = Matrix()
            tempMatrix.setRotate(currentAngle, (mViewBitmap.width / 2).toFloat(), (mViewBitmap.height / 2).toFloat())
            mViewBitmap = Bitmap.createBitmap(mViewBitmap, 0, 0, mViewBitmap.width, mViewBitmap.height, tempMatrix, true)
        }
        val cropOffsetX = Math.round((mCropRect.left - trapToRect(mCurrentImageCorners).left) / currentScale)
        val cropOffsetY = Math.round((mCropRect.top - trapToRect(mCurrentImageCorners).top) / currentScale)
        val mCroppedImageWidth = Math.round(mCropRect.width() / currentScale)
        val mCroppedImageHeight = Math.round(mCropRect.height() / currentScale)
        return Bitmap.createBitmap(
            mViewBitmap, cropOffsetX, cropOffsetY,
            mCroppedImageWidth, mCroppedImageHeight)
    }

    fun setTargetAspectRatio(targetAspectRatio: Float) {
        mTargetAspectRatio = targetAspectRatio
        initCropRect()
        setImageToWrapCropBounds()
    }

    private fun zoomInImage(scale: Float, centerX: Float, centerY: Float) {
        postScale(scale / currentScale, centerX, centerY)
    }

    fun postRotate(angle: Float) {
        postRotate(angle - currentAngle, mCropRect.centerX(), mCropRect.centerY())
    }

    fun setImageToWrapCropBounds() {
        if (mBitmapLaidOut && !isImageWrapCropBounds) {
            val currentX = mCurrentImageCenter[0]
            val currentY = mCurrentImageCenter[1]
            val currentScale = currentScale
            var deltaX = mCropRect.centerX() - currentX
            var deltaY = mCropRect.centerY() - currentY
            var deltaScale: Float
            mTempMatrix.reset()
            mTempMatrix.setTranslate(deltaX, deltaY)
            val tempCurrentImageCorners = mCurrentImageCorners.copyOf(mCurrentImageCorners.size)
            mTempMatrix.mapPoints(tempCurrentImageCorners)
            val willImageWrapCropBoundsAfterTranslate = isImageWrapCropBounds(tempCurrentImageCorners)
            if (willImageWrapCropBoundsAfterTranslate) {
                val imageIndents = calculateImageIndents()
                deltaX = -(imageIndents[0] + imageIndents[2])
                deltaY = -(imageIndents[1] + imageIndents[3])
                postTranslate(deltaX, deltaY)
            } else {
                val tempCropRect = RectF(mCropRect)
                mTempMatrix.reset()
                mTempMatrix.setRotate(currentAngle)
                mTempMatrix.mapRect(tempCropRect)
                val currentImageSides = getRectSidesFromCorners(mCurrentImageCorners)
                deltaScale = Math.max(
                    tempCropRect.width() / currentImageSides[0],
                    tempCropRect.height() / currentImageSides[1])
                deltaScale = deltaScale * currentScale - currentScale
                zoomInImage(currentScale + deltaScale, mCropRect.centerX(), mCropRect.centerY())
                setImageToWrapCropBounds()
            }
        }
    }

    private fun calculateImageIndents(): FloatArray {
        mTempMatrix.reset()
        mTempMatrix.setRotate(-currentAngle)
        val unRotatedImageCorners = mCurrentImageCorners.copyOf(mCurrentImageCorners.size)
        val unRotatedCropBoundsCorners = getCornersFromRect(mCropRect)
        mTempMatrix.mapPoints(unRotatedImageCorners)
        mTempMatrix.mapPoints(unRotatedCropBoundsCorners)
        val unRotatedImageRect = trapToRect(unRotatedImageCorners)
        val unRotatedCropRect = trapToRect(unRotatedCropBoundsCorners)
        val deltaLeft = unRotatedImageRect.left - unRotatedCropRect.left
        val deltaTop = unRotatedImageRect.top - unRotatedCropRect.top
        val deltaRight = unRotatedImageRect.right - unRotatedCropRect.right
        val deltaBottom = unRotatedImageRect.bottom - unRotatedCropRect.bottom
        val indents = FloatArray(4)
        indents[0] = if (deltaLeft > 0) deltaLeft else 0f
        indents[1] = if (deltaTop > 0) deltaTop else 0f
        indents[2] = if (deltaRight < 0) deltaRight else 0f
        indents[3] = if (deltaBottom < 0) deltaBottom else 0f
        mTempMatrix.reset()
        mTempMatrix.setRotate(currentAngle)
        mTempMatrix.mapPoints(indents)
        return indents
    }

    override fun onImageLaidOut() {
        super.onImageLaidOut()
        val drawable = drawable ?: return
        val drawableWidth = drawable.intrinsicWidth.toFloat()
        val drawableHeight = drawable.intrinsicHeight.toFloat()
        initCropRect()
        setupInitialImagePosition(drawableWidth, drawableHeight)
    }

    private fun initCropRect() {
        val height = (mThisWidth / mTargetAspectRatio).toInt()
        if (height > mThisHeight) {
            val width = (mThisHeight * mTargetAspectRatio).toInt()
            val halfDiff = (mThisWidth - width) / 2
            mCropRect[halfDiff.toFloat(), 0f, width + halfDiff.toFloat()] = mThisHeight.toFloat()
        } else {
            val halfDiff = (mThisHeight - height) / 2
            mCropRect[0f, halfDiff.toFloat(), mThisWidth.toFloat()] = height + halfDiff.toFloat()
        }
    }

    private val isImageWrapCropBounds: Boolean
        get() = isImageWrapCropBounds(mCurrentImageCorners)

    private fun isImageWrapCropBounds(imageCorners: FloatArray): Boolean {
        mTempMatrix.reset()
        mTempMatrix.setRotate(-currentAngle)
        val unrotatedImageCorners = imageCorners.copyOf(imageCorners.size)
        mTempMatrix.mapPoints(unrotatedImageCorners)
        val unrotatedCropBoundsCorners = getCornersFromRect(mCropRect)
        mTempMatrix.mapPoints(unrotatedCropBoundsCorners)
        return trapToRect(unrotatedImageCorners).contains(trapToRect(unrotatedCropBoundsCorners))
    }

    private fun setupInitialImagePosition(drawableWidth: Float, drawableHeight: Float) {
        val cropRectWidth = mCropRect.width()
        val cropRectHeight = mCropRect.height()
        val widthScale = mCropRect.width() / drawableWidth
        val heightScale = mCropRect.height() / drawableHeight
        val initialMinScale = Math.max(widthScale, heightScale)
        val tw = (cropRectWidth - drawableWidth * initialMinScale) / 2.0f + mCropRect.left
        val th = (cropRectHeight - drawableHeight * initialMinScale) / 2.0f + mCropRect.top
        mCurrentImageMatrix.reset()
        mCurrentImageMatrix.postScale(initialMinScale, initialMinScale)
        mCurrentImageMatrix.postTranslate(tw, th)
        imageMatrix = mCurrentImageMatrix
    }
}