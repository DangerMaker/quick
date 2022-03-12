package com.hundsun.zjfae

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import kotlinx.android.synthetic.main.user_private_notice_dialog_layout.*

class UserPrivateNoticeDialog : Dialog {


    constructor(context: Context) : super(context, R.style.mystyle)


    private var tv_title: TextView? = null

    private var notice_webView: WebView? = null

    private var tv_close: TextView? = null

    private var tv_agree: TextView? = null

    private  var dialog : UserPrivateNoticeDialog? = null

    var noticeTitle: String = ""
        set(value) {
            field = value
        }

    var loadUrl: String = ""
        set(value) {
            field = value
        }

    var clickListener: ClickListener? = null
        set(value) {

            field = value
        }

    var agreeText: String = ""
        set(value) {
            field = value
        }

    var force: Boolean = false
        set(value) {
            field = value

        }


    fun createDialog(): UserPrivateNoticeDialog {

        val inflater = LayoutInflater.from(context)

        dialog= UserPrivateNoticeDialog(context)

        var layout = inflater.inflate(R.layout.user_private_notice_dialog_layout, null)

        initView(layout)
        initData()

        dialog!!.addContentView(layout, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        dialog!!.setCancelable(false)
        return dialog!!

    }

    override fun show() {

        if (!isShowing) {
            super.show()
        }


    }


    private fun initView(rootView: View) {

        tv_title = rootView.findViewById(R.id.tv_title)

        notice_webView = rootView.findViewById(R.id.notice_webView)
        notice_webView!!.isHorizontalScrollBarEnabled = false
        notice_webView!!.isVerticalScrollBarEnabled = false

        tv_close = rootView.findViewById(R.id.tv_close)

        tv_agree = rootView.findViewById(R.id.tv_agree)

    }


    private fun initData() {

        tv_title!!.text = noticeTitle

        tv_close!!.text = if (force) "不同意，退出APP" else "暂不同意"

        tv_agree!!.text = agreeText

        notice_webView!!.loadDataWithBaseURL(null, loadUrl, "text/html", "UTF-8", null)

        tv_close!!.setOnClickListener {

            if (clickListener != null) {
                dismiss()
                clickListener!!.onClose(dialog!!,DialogInterface.BUTTON_NEGATIVE,force)
            }
        }


        tv_agree!!.setOnClickListener {

            if (clickListener != null) {
                dismiss()
                clickListener!!.onAgree(dialog!!,DialogInterface.BUTTON_POSITIVE)
            }


        }


        notice_webView!!.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                if (url!!.contains("http")) {
                    if (clickListener != null) {
                        clickListener!!.startWebActivity(url)
                    }

                }


                return true
            }
        }

    }


    interface ClickListener {

        fun onAgree(dialog : DialogInterface, which : Int)

        fun onClose(dialog : DialogInterface, which : Int,force: Boolean)

        fun startWebActivity(url: String)

    }


}