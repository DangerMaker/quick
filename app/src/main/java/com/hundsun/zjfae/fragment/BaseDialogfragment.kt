package com.hundsun.zjfae.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StyleRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.*
import com.hundsun.zjfae.R

abstract class BaseDialogfragment : DialogFragment(), DialogInterface.OnDismissListener {


    //dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的，1.0f时候，背景全部变黑暗。
     var dimAmount = 0.5f
    set(value) {
        field = value
    }

    protected var mRootView: View? = null

    //透明度 1不透明，0全透明
    var alpha = 1f
    set(value) {
        field = value
    }

    private var width = WindowManager.LayoutParams.WRAP_CONTENT

    private var height = WindowManager.LayoutParams.WRAP_CONTENT

    protected var gravity = Gravity.CENTER


     protected var mContext : Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDialogStyle(STYLE_NO_TITLE, R.style.BaseDialogFragmentStyle)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        mRootView = inflater.inflate(getLayoutId(), container, false)

        initView()

        initData()

        return mRootView
    }

    override fun onStart() {
        super.onStart()

        if (this.dialog != null) {

            this.dialog!!.setCancelable(isCancel())

            dialog!!.setCanceledOnTouchOutside(isCancel())

            val window = dialog.window

            if (window != null) {
                val params = window.attributes
                params.alpha = alpha
                Log.e("TAG,alpha",alpha.toString())
                params.dimAmount = dimAmount
                Log.e("TAG,dimAmount",dimAmount.toString())
                params.width = width
                params.height = height

                params.gravity = gravity

                window.attributes = params
            }

        }



    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }


    fun <V : View> findViewById(id: Int): V {


        return mRootView!!.findViewById(id)

    }


    //设置dialog的style和主题
    protected open fun setDialogStyle(style: Int, @StyleRes theme: Int) {

        super.setStyle(style, theme)

    }





    protected fun setLayoutParams(width: Int, height: Int) {
        this.width = width
        this.height = height
    }


    protected open fun setLayoutGravity(gravity: Int) {
        this.gravity = gravity
    }


     open fun showDialog(fragmentManager: FragmentManager?,tag:String) {

         if (!isAdded){

             super.show(fragmentManager,tag)
         }


    }




     fun dismissDialog() {
       onDismiss(dialog)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if (this.dialog != null){

            val window = this.dialog.window
            if (window != null) {
                val params = window.attributes
                params.dimAmount = 0f
                window.attributes = params
            }



        }
        super.onDismiss(dialog)
    }


    //布局id
    protected abstract fun getLayoutId(): Int

    //初始化控件
    protected abstract fun initView()

    //初始化数据
    protected abstract fun initData()

    //点击外部是否可关闭
    protected abstract fun isCancel(): Boolean



    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null){
            dialog.cancel()
        }
    }

}