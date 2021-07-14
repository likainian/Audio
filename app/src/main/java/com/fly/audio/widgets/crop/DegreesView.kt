package com.fly.audio.widgets.crop

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.fly.audio.ext.dp2px
import com.fly.core.base.appContext

class DegreesView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {
    private var mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG) //垂直刻度线画笔
    private var mIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG) //指示器刻度值画笔
    private var scaleMaxNum = 30 //刻度尺的最大值
    private val eachScalePix = 15.dp2px() //每个刻度值的像素
    private var mSlidingMoveX = 0 //滑动的差值
    private var totalX = 0 //滑动总距离
    private var zeroX = 0 //滑动初始
    private var mDownX = 0
    private var mCenterY = 0
    private var mCenterX = 0
    private val spaceUnit = 5 //单位间隔

    var onValueChangeListener: ((Int) -> Unit)? = null

    init {
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.strokeWidth = 1.dp2px().toFloat()
        mLinePaint.setARGB(255, 216, 216, 216)
        mIndicatorPaint.setARGB(255, 249, 157, 59)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        mCenterY = measuredHeight / 2
        mCenterX = measuredWidth / 2
        if(zeroX == 0){
            zeroX = mCenterX - scaleMaxNum * eachScalePix / 2
            totalX = zeroX
        }
    }

    override fun onDraw(canvas: Canvas) {
        val currentValue = (-totalX + zeroX) * 3 / eachScalePix
        onValueChangeListener?.invoke(currentValue)
        drawCurrentScale(canvas)
        drawNum(canvas)
    }

    fun setDegrees(degrees: Float) {
        totalX = zeroX - (degrees * eachScalePix / 3).toInt()
        invalidate()
    }

    fun reset() {
        totalX = zeroX
        invalidate()
    }

    /**
     * 绘画数字
     */
    private fun drawNum(canvas: Canvas) {
        for (i in 0 until width) {
            val alpha = ((1 - 2 * Math.abs((i - mCenterX) / width.toFloat())) * 255).toInt()
            mLinePaint.setARGB(alpha, 216, 216, 216)
            if ((-totalX + i) % eachScalePix == 0) {
                if (-totalX + i >= 0 && -totalX + i <= scaleMaxNum * eachScalePix) {
                    if ((-totalX + i) % (eachScalePix * spaceUnit) == 0) {
                        canvas.drawLine(
                            i.toFloat(), (mCenterY - 6.dp2px()).toFloat(), i.toFloat(), (mCenterY + 6.dp2px()).toFloat(), mLinePaint)
                    } else {
                        canvas.drawLine(
                            i.toFloat(), (mCenterY - 4.dp2px()).toFloat(), i.toFloat(), (mCenterY + 4.dp2px()).toFloat(), mLinePaint)
                    }

                }
            }
        }
    }

    /**
     * 绘画刻度
     */
    private fun drawCurrentScale(canvas: Canvas) {
        val roundRectF = RectF()
        roundRectF.left = mCenterX - 2.dp2px().toFloat()
        roundRectF.right = mCenterX + 2.dp2px().toFloat()
        roundRectF.top = mCenterY - 12.dp2px().toFloat()
        roundRectF.bottom = mCenterY + 12.dp2px().toFloat()
        canvas.drawRoundRect(roundRectF, 2.dp2px().toFloat(), 2.dp2px().toFloat(), mIndicatorPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = event.x.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                mSlidingMoveX = (event.x - mDownX).toInt() //滑动距离
                totalX += mSlidingMoveX
                if (mSlidingMoveX < 0) {
                    //向左滑动,刻度值增大
                    if (-totalX + mCenterX > scaleMaxNum * eachScalePix) {
                        //向左滑动如果刻度值大于最大值，则不能滑动了
                        totalX = mCenterX - scaleMaxNum * eachScalePix
                    }
                    invalidate()
                } else {
                    //向右滑动，刻度值减小
                    if (totalX - mCenterX > 0) {
                        //向右滑动刻度值小于最小值则不能滑动了
                        totalX = mCenterX
                    }
                    invalidate()
                }
                mDownX = event.x.toInt()
            }

            MotionEvent.ACTION_UP -> {
                if ((totalX - zeroX > -eachScalePix / 3) && (totalX - zeroX < eachScalePix / 3)) {
                    vibrator()
                }
            }
        }
        return true
    }

    private fun vibrator() {
        //震动的效果
        val vibrator = appContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

}