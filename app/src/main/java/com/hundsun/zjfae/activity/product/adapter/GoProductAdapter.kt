package com.hundsun.zjfae.activity.product.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.activity.product.bean.GoActivityBean

class GoProductAdapter : RecyclerView.Adapter<GoProductAdapter.GoProductViewHolder> {

    private val context: Context
    private val list: MutableList<GoActivityBean>
    private val itemClick : onProductItemClick





    constructor(context:Context,mutableList: MutableList<GoActivityBean>,itemClick : onProductItemClick) : super() {
        this.context = context
        this.list = mutableList
        this.itemClick = itemClick
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GoProductViewHolder {

        val rootView = LayoutInflater.from(context).inflate(R.layout.goproduct_adapter_layout, p0, false)
        return GoProductViewHolder(rootView)

    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(viewHolder: GoProductViewHolder, i: Int) {

        if (i % 2 == 0) {
            viewHolder.attach_layout.setBackgroundResource(R.color.hui)
        } else {
            viewHolder.attach_layout.setBackgroundResource(R.color.white)
        }

        if (list[i].areaJumpType == "03") {
            viewHolder.btn.setText("立即交易")
        } else {
            viewHolder.btn.setText("去交易")
        }
        viewHolder.tv_title.setText(list[i].productName)
        viewHolder.btn.setOnClickListener(View.OnClickListener { itemClick.onItemClick(list[i]) })
    }


    class GoProductViewHolder: RecyclerView.ViewHolder{

         var attach_layout: RelativeLayout
         var tv_title: TextView
         var btn: Button

        constructor(itemView: View) : super(itemView){
            tv_title = itemView.findViewById(R.id.tv_title)
            attach_layout = itemView.findViewById(R.id.attach_layout)
            btn = itemView.findViewById(R.id.btn)
        }



    }


     interface onProductItemClick {
        fun onItemClick(goActivityBean: GoActivityBean)
    }
}