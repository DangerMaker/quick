package com.hundsun.zjfae.activity.mine

import android.view.View
import android.widget.Button
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.base.BasicsActivity

class CertificationStatus : BasicsActivity(),View.OnClickListener {

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.finish -> finish()
        }
    }


    override fun initView() {
        findViewById<Button>(R.id.finish).setOnClickListener(this)
    }

    override fun resetLayout() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_certification_status;
    }


}
