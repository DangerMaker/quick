package com.hundsun.zjfae.common.view.dialog

import android.content.Context
import android.view.Window
import android.widget.TextView
import com.hundsun.zjfae.R

class CardDictionaryDialog(context: Context) : BaseDialog(context) {


    private var tv_context : TextView? = null

    private var confirm_btn : TextView? = null



    override fun getLayoutId(): Int {

        return R.layout.dialog_card_layout
    }



    override fun initView() {

        tv_context = findViewById(R.id.tv_context)

        confirm_btn = findViewById(R.id.confirm_btn)

    }

    override fun initData() {

        tv_context?.text = contextStr

        confirm_btn?.setOnClickListener {

            onDismiss()

        }
    }

    override fun isCancel(): Boolean {

        return false
    }

    override fun initWindow(window: Window) {

    }

    var contextStr : String = ""
    set(value)  {
        field = value

    }
}