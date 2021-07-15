package com.fly.audio.activity

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import com.fly.audio.R
import com.fly.audio.databinding.ActivityCropBinding
import com.fly.audio.frag.CropEditFrag
import com.fly.audio.util.PermissionUtil
import com.fly.core.base.bindbase.BaseBindActivity


class CropActivity : BaseBindActivity<ActivityCropBinding>(){

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, CropActivity::class.java))
        }
    }

    override fun createBinding(): ActivityCropBinding =
        ActivityCropBinding.inflate(layoutInflater)


    override fun initView() {
        PermissionUtil.checkAudio(this)
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //获取返回的数据，这里是android自定义的Uri地址
        data?.data?.let {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            //获取选择照片的数据视图
            val cursor = contentResolver.query(
                it,
                filePathColumn, null, null, null
            )
            cursor?.moveToFirst()
            //从数据视图中获取已选择图片的路径
            cursor?.getColumnIndex(filePathColumn[0])?.let {
                cursor.getString(it)?.let {path->
                    supportFragmentManager.beginTransaction()
                        .add(R.id.frameLayout, CropEditFrag.newInstance(path))
                        .commit()
                }
                cursor.close()

            }

        }

    }
}