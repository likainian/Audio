package com.fly.audio.frag

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.view.View
import com.fly.audio.R
import com.fly.audio.databinding.FragCropEditBinding
import com.fly.audio.databing.onbind.Bind
import com.fly.audio.ext.Res
import com.fly.audio.ext.cropRate
import com.fly.audio.ext.rotateBitmap
import com.fly.audio.ext.saveToFile
import com.fly.audio.util.BitmapUtil
import com.fly.audio.util.FileUtil
import com.fly.core.base.bindbase.BaseBindFragment
import java.io.File

class CropEditFrag : BaseBindFragment<FragCropEditBinding>() {

    companion object {
        fun newInstance(path: String): CropEditFrag {
            val args = Bundle()
            args.putString("path",path)
            val fragment = CropEditFrag()
            fragment.arguments = args
            return fragment
        }
    }

    private var degrees = 0f
    private var degreesS = 0f
    var photoFile: File? = null
    private var lastRatio: Float = 0f
    private var lastScale: Float = 0f
    private var lastDegrees = 0f
    private var lastDegreesS = 0f
    private var lastMatrix: Matrix = Matrix()

    val onClickBack = Bind.onClick {
        activity?.onBackPressed()
    }

    val onClick34 = Bind.onClick {
        setRate(3 / 4f)
        mViewDataBinding.cropView.setCropRate(3 / 4f)
    }

    val onClick11 = Bind.onClick {
        setRate(1f)
        mViewDataBinding.cropView.setCropRate(1f)
    }

    val onClick43 = Bind.onClick {
        setRate(4 / 3f)
        mViewDataBinding.cropView.setCropRate(4 / 3f)
    }

    val onClickRotate = Bind.onClick {
        degrees -= 90
        mViewDataBinding.cropView.setDegrees(degrees - degreesS)
    }

    val onClickConfirm = Bind.onClick {
        lastRatio = mViewDataBinding.cropView.getCropRate()
        lastScale = mViewDataBinding.cropView.getScale()
        lastMatrix = mViewDataBinding.cropView.getImageMatrix()
        lastDegrees = degrees
        lastDegreesS = degreesS
        val cropPicture = mViewDataBinding.cropView.cropPicture()
        arguments?.getString("path")?.let {
            val file = File(it)
            photoFile = File(FileUtil.getAbsolutePath(file.name))
            photoFile?.deleteOnExit()
        }
        photoFile?.let {
            cropPicture?.saveToFile(it, Bitmap.CompressFormat.JPEG, 100)
            arguments?.getString("path")?.let {path->
                BitmapUtil.saveExif(path, it.path)
            }
        }
    }

    override fun createBinding(): FragCropEditBinding {
        val binding = FragCropEditBinding.inflate(layoutInflater)
        binding.owner = this
        return binding
    }

    override fun initView(view: View) {
        arguments?.getString("path")?.let {
            val bitmap = BitmapFactory.decodeFile(it).rotateBitmap(it)
            mViewDataBinding.cropView.setPicture(bitmap)
            mViewDataBinding.cropView.setCropRate(bitmap.cropRate())
            setRate(bitmap.cropRate())
            mViewDataBinding.cropView.post {
                lastRatio =  mViewDataBinding.cropView.getCropRate()
                lastScale =  mViewDataBinding.cropView.getScale()
                lastMatrix =  mViewDataBinding.cropView.getImageMatrix()
            }
        }
        mViewDataBinding.degreesView.onValueChangeListener = {
            degreesS = it.toFloat()
            mViewDataBinding.cropView.setDegrees(degrees - degreesS)
            mViewDataBinding.tvDegrees.text = String.format(getString(R.string.placeholder_crop_degrees), it)
        }
    }

    private fun setRate(rate: Float) {
        mViewDataBinding.tv34.setTextColor(Res.getColor(R.color.color_7d7d7d))
        mViewDataBinding.tv11.setTextColor(Res.getColor(R.color.color_7d7d7d))
        mViewDataBinding.tv43.setTextColor(Res.getColor(R.color.color_7d7d7d))
        when (rate) {
            3 / 4f -> {
                mViewDataBinding.tv34.setTextColor(Res.getColor(R.color.color_f99d3b))
            }

            1f -> {
                mViewDataBinding.tv11.setTextColor(Res.getColor(R.color.color_f99d3b))
            }

            4 / 3f -> {
                mViewDataBinding.tv43.setTextColor(Res.getColor(R.color.color_f99d3b))
            }
        }
    }
}