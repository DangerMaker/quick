package com.hundsun.zjfae.activity.product

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.activity.home.WebActivity
import com.hundsun.zjfae.activity.product.adapter.GoProductAdapter
import com.hundsun.zjfae.activity.product.bean.GoActivityBean
import com.hundsun.zjfae.activity.productreserve.ReserveProductDetailActivity
import com.hundsun.zjfae.common.view.dialog.BaseDialog
import com.hundsun.zjfae.fragment.home.bean.ShareBean

class OpenHomeReservationProductDialog : BaseDialog,View.OnClickListener {

    private var recyclerView : RecyclerView? = null

    private var mutableList: MutableList<GoActivityBean>? = null


    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.cancel_img ->{
                onDismiss()
            }

        }


    }


    constructor(context:Context) :super(context)

    override fun getLayoutId(): Int {


        return R.layout.fragment_go_product;
    }

    override fun initView() {
        findViewById<ImageView>(R.id.cancel_img).setOnClickListener(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.setLayoutManager(LinearLayoutManager(context))
    }

    override fun initData() {

        val adapter = GoProductAdapter(context,mutableList!!,object :GoProductAdapter.onProductItemClick {

            override fun onItemClick(goActivityBean: GoActivityBean) {

                if (goActivityBean.areaJumpType == "02") {
                    val intent = Intent()
                    intent.putExtra("productCode", goActivityBean.productCode)
                    intent.putExtra("sellingStatus", "1")
                    intent.putExtra("orderType", goActivityBean.orderType)
                    intent.putExtra("orderNum", goActivityBean.orderNum)
                    intent.setClass(context, ReserveProductDetailActivity::class.java)
                    context.startActivity(intent)

                } else if (goActivityBean.areaJumpType == "03") {

                    val intent = Intent()
                    intent.setClass(context, WebActivity::class.java)
                    val shareBean = ShareBean()
                    shareBean.funcUrl = goActivityBean.jumpUrl
                    intent.putExtra("shareBean", shareBean)
                    context.startActivity(intent)

                }

                onDismiss()

            }

        })

        recyclerView!!.setAdapter(adapter)
    }

    override fun isCancel(): Boolean {

        return false
    }

    override fun initWindow(window: Window) {

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window.decorView.setPadding(15,0,15,0)

        val lp = window.attributes
        lp.alpha = 1f
        lp.gravity = Gravity.CENTER
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT
        window.attributes = lp


    }

    fun setData(mutableList: MutableList<GoActivityBean>){

        this.mutableList = mutableList
    }
}