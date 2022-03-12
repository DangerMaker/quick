package com.hundsun.zjfae.fragment.home.view.dialog

import android.support.v4.app.FragmentManager
import android.view.WindowManager
import android.widget.ImageView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.utils.gilde.ImageLoad
import com.hundsun.zjfae.fragment.BaseDialogfragment


class HomeAdvertiseDialogFragment : BaseDialogfragment() {

    private var iv_ad_image: ImageView? = null

    private var iv_close: ImageView? = null


    var onclick: HomeAdOnclick? = null
        set(value) {
            field = value
        }
    var iconUrl : String? = ""
    set(value) {
        field = value
    }

    override fun initData() {


            if (iconUrl != null && !iconUrl.equals("")) {

                ImageLoad.getImageLoad().LoadImage(context, iconUrl, iv_ad_image)

                iv_ad_image!!.setOnClickListener {

                    if (onclick != null){
                        onclick!!.openUrl()


                    }

                }
            }



    }


    override fun initView() {
        dimAmount = 0f
        iv_ad_image = findViewById(R.id.iv_ad_image)

        iv_close = findViewById(R.id.iv_close)
        iv_close!!.setOnClickListener {

            if (onclick != null) {
                onclick!!.close()
            }
        }

        setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

    }


    fun showDialog(fragmentManager: FragmentManager?) {

        if (!iconUrl.equals("")){

            super.showDialog(fragmentManager, "HomeAdvertiseDialogFragment")
        }



    }

    override fun getLayoutId(): Int {


        return R.layout.home_ad_layout

    }

    override fun isCancel(): Boolean {


        return true
    }


    interface HomeAdOnclick {


        fun openUrl()

        fun close()

    }
}