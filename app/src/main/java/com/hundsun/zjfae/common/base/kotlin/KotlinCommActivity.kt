package com.hundsun.zjfae.common.base.kotlin

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import com.hundsun.zjfae.HomeActivity
import com.hundsun.zjfae.common.view.dialog.CustomProgressDialog

abstract class KotlinCommActivity<P :KotlinBasePresenter> : KotlinBaseActivity(),KotlinBaseView {

    protected lateinit var presenter:P;

    private lateinit var mCustomProgressDialog:CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        presenter = createPresenter()
        initProgressDialog()
        initData()
    }




    private fun initProgressDialog(){
        mCustomProgressDialog = CustomProgressDialog(this,"正在加载")
        mCustomProgressDialog.setCanceledOnTouchOutside(false)
    }



    protected abstract fun createPresenter():P

    override fun showLoading() {
        if (!mCustomProgressDialog.isShowing){
            mCustomProgressDialog.show()
        }
    }

    override fun showError(msg: String) {
        if (!mCustomProgressDialog.isShowing){
            mCustomProgressDialog.show(msg)
        }
    }

    override fun isShowLoad(): Boolean {
        return true
    }

    override fun showLoading(content: String) {

    }

    override fun hideLoading() {
        if (mCustomProgressDialog.isShowing){
            mCustomProgressDialog.dismiss()
        }
    }

    override fun onAttach(): Context {

        return  this;
    }

    override fun isFinishActivity(returnMsg: String) {

    }

    override fun loginTimeOut(msg: String) {

        HomeActivity.show(this,HomeActivity.HomeFragmentType.HOME_FRAGMENT)
    }
}