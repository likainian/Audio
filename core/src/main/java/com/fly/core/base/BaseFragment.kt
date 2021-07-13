package com.fly.core.base

import androidx.fragment.app.Fragment
import com.fly.core.widget.LoadingDialog

abstract class BaseFragment : Fragment(){

    private val mLoadingDialog by lazy {
        LoadingDialog.Builder(requireActivity())
            .setCancelable(false)
            .setCancelOutside(false).create()
    }

    protected fun showLoading() {
        requireActivity().runOnUiThread {
            if (!mLoadingDialog.isShowing) {
                mLoadingDialog.show()
            }
        }
    }

    protected fun hideLoading() {
        requireActivity().runOnUiThread {
            if (mLoadingDialog.isShowing) {
                mLoadingDialog.dismiss()
            }
        }
    }

}