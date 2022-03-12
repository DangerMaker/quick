package com.hundsun.zjfae.fragment.home.view.dialog

import android.support.v4.app.FragmentManager
import android.view.WindowManager
import android.widget.ImageView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.utils.gilde.ImageLoad
import com.hundsun.zjfae.fragment.BaseDialogfragment
import com.hundsun.zjfae.fragment.home.bean.ShareBean
import kotlinx.android.synthetic.main.activity_my_invitation.*

class HomeLevelDialog : BaseDialogfragment() {


    private var iv_ad_image: ImageView? = null

    private var iv_close: ImageView? = null

     var levelOnclick :HomeLevelOnclick? = null
    set(value) {
        field = value
    }

    var imageUrl = ""
        set(value) {
            field = value
        }


    var openUrl = ""
        set(value) {
            field = value
        }

    var jumpRule = ""
    set(value) {
        field = value
    }


    override fun initView() {
        dimAmount = 0f
        iv_ad_image = findViewById(R.id.iv_ad_image)

        iv_close = findViewById(R.id.iv_close)
        iv_close!!.setOnClickListener {

           if (levelOnclick != null){
               levelOnclick!!.close()
           }
        }

        setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)


    }

    override fun initData() {
        ImageLoad.getImageLoad().LoadImage(context, imageUrl, iv_ad_image)

        iv_ad_image!!.setOnClickListener {

            if (levelOnclick != null){

                if (!jumpRule.equals("0")){

                    levelOnclick!!.openUrl()
                }



            }
        }
    }



    fun showDialog(fragmentManager: FragmentManager?) {

        if (!imageUrl.equals("")){
            super.showDialog(fragmentManager, "HomeLevelDialog")
        }

    }



    override fun getLayoutId(): Int {


        return R.layout.home_ad_layout

    }

    override fun isCancel(): Boolean {


        return true
    }

    interface HomeLevelOnclick{


        fun openUrl()

        fun close ()

    }






}