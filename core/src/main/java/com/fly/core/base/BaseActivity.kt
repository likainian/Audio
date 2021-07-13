package com.fly.core.base

import androidx.appcompat.app.AppCompatActivity
import com.fly.core.widget.LoadingDialog

abstract class BaseActivity : AppCompatActivity(){

    private val mLoadingDialog by lazy {
        LoadingDialog.Builder(this)
            .setCancelable(false)
            .setCancelOutside(false).create()
    }

    protected fun showLoading() {
        runOnUiThread {
            if (!mLoadingDialog.isShowing) {
                mLoadingDialog.show()
            }
        }
    }

    protected fun hideLoading() {
        runOnUiThread {
            if (mLoadingDialog.isShowing) {
                mLoadingDialog.dismiss()
            }
        }
    }
}