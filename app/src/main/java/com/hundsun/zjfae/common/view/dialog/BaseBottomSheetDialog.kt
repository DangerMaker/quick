package com.hundsun.zjfae.common.view.dialog

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View

abstract class BaseBottomSheetDialog(private val context: Context) {



    protected var bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)

    private var cancelable = true

    //是否已经加载过布局
    private var loadRootView = false

    init {
        bottomSheetDialog.setCancelable(cancelable)
        bottomSheetDialog.setCanceledOnTouchOutside(cancelable)
    }


     /**
      * 初始化view
      * @date: 2021/5/13 13:45
      * @author: moran
      * @param rootView 根布局
      */
    protected abstract fun initView(rootView: View)

     /**
      * 返回布局id
      * @date: 2021/5/13 13:45
      * @author: moran
      * @return 布局id
      */
    protected abstract fun getLayoutId(): Int


     open fun showDialog() {
        if (!bottomSheetDialog.isShowing) {
            if (!loadRootView) {
                val layoutInflater = LayoutInflater.from(context)
                val rootView = layoutInflater.inflate(getLayoutId(), null)
                bottomSheetDialog.setContentView(rootView)


                initView(rootView)
                loadRootView = true
            }

            bottomSheetDialog.show()
        }

    }

   open fun onDismissDialog() {
        bottomSheetDialog.dismiss()
    }

    fun setCancelable(cancelable: Boolean = false) {
        this.cancelable = cancelable
        bottomSheetDialog.setCancelable(cancelable)
        bottomSheetDialog.setCanceledOnTouchOutside(cancelable)
    }
}