package com.hundsun.zjfae.common.base.kotlin

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.hundsun.zjfae.BuildConfig
import com.hundsun.zjfae.common.base.BaseApplication
import com.zjfae.jpush.Extras
import com.zjfae.jpush.JPush
import com.zjfae.jpush.OpenJPushMessage

abstract class KotlinBaseActivity : AppCompatActivity() {


    private lateinit var mBaseApplication: BaseApplication

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        updateConfiguration()
        setContentView(getLayoutId())
        mBaseApplication = BaseApplication.getInstance()
        mBaseApplication.add(this)
        initJpush(mBaseApplication)
    }

    protected abstract fun initView()


    protected abstract fun initData()


    protected fun baseStartActivity( intent:Intent){
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

    protected fun baseStartActivity(context: Context,cls:Class<*>){

        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.setClass(context,cls)
        this.startActivity(intent)
    }




    protected  abstract fun getLayoutId():Int

    private  val FONT_SCALE = 1.0f;
    private fun updateConfiguration(){
        val res: Resources = super.getResources()
        val config : Configuration  = res.configuration;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            if (config.fontScale>FONT_SCALE){
                config.fontScale = 1.3f
                //res.updateConfiguration(config,res.displayMetrics)
                createConfigurationContext(config)
            }
        }
    }


    /**
     * 极光推送
     * **/

    private fun initJpush(application: BaseApplication){
        JPush.getInstance(application, BuildConfig.DEBUG).setOpenJpushMessage(openJPushMessage)
    }


    private val openJPushMessage = object :OpenJPushMessage{
        override fun openMessage(context: Context?, result: Extras?) {

        }
    }

}