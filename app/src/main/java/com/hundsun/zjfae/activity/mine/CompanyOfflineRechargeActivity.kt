package com.hundsun.zjfae.activity.mine

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.activity.mine.presenter.CompanyOfflineRecharge
import com.hundsun.zjfae.activity.mine.view.CompanyOfflineRechargeView
import com.hundsun.zjfae.common.base.CommActivity
import kotlinx.android.synthetic.main.activity_company_offline_recharge.*

class CompanyOfflineRechargeActivity : CommActivity<CompanyOfflineRecharge>() , CompanyOfflineRechargeView ,View.OnClickListener{


    override fun onNoticeContent(content: String) {

        wb_content.loadDataWithBaseURL(null,getHtmlData(content),"text/html", "UTF-8", null)
    }

    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.bt_close ->{
                finish()
            }
        }

    }

    override fun createPresenter(): CompanyOfflineRecharge {

        return CompanyOfflineRecharge(this)
    }

    override fun resetLayout() {


    }

    override fun getLayoutId(): Int {

        return R.layout.activity_company_offline_recharge
    }


    override fun initData() {
        setTitle("线下充值")
        presenter.initData()

    }

    override fun initView() {

        wb_content.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
        wb_content.setHorizontalScrollBarEnabled(false)
        wb_content.setVerticalScrollBarEnabled(false)
        val webSetting = wb_content.getSettings()
        //支持JS，此项必不可少
        webSetting.setJavaScriptEnabled(true)
        //1.网上说是设置此选项提高渲染的优先级，
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        //2.首先阻塞图片，让图片不显示
        webSetting.setBlockNetworkImage(true)
        //3.页面加载好以后，在放开图片：
        webSetting.setBlockNetworkImage(false)
        // 设置缓存模式（下面会详细介绍缓存）
        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        // 开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true)
        webSetting.setAllowFileAccess(true)
        //开启 database storage API 功能
        webSetting.setDatabaseEnabled(true)
        //开启 Application Caches 功能
        webSetting.setAppCacheEnabled(true)
        //此设置是否保存H5表单数据，发现一个蛋疼的问题，在小米手机上当H5 input框设置为search后，当点击input框的时候//竟然会有历史的搜索记录，而且样式十分难看，设置此属性可以取消历史搜索记录
        webSetting.setSaveFormData(false)

        bt_close.setOnClickListener(this)

    }
}

