package com.hundsun.zjfae.activity.mine.adapter

import android.content.Context
import android.widget.TextView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.adapter.BaseAdapter
import onight.zjfae.afront.gens.LoadWithDrawBankInfo

class WithdrawalBankAdapter(context: Context,list : MutableList<LoadWithDrawBankInfo.PBIFE_fund_loadWithDrawBankInfo.FundCorrelateRecordList>) : BaseAdapter<LoadWithDrawBankInfo.PBIFE_fund_loadWithDrawBankInfo.FundCorrelateRecordList>(context,list) {



    override fun getLayoutId(position: Int): Int {


        return R.layout.item_withdrawalbank_layout

    }

    override fun bindData(t: LoadWithDrawBankInfo.PBIFE_fund_loadWithDrawBankInfo.FundCorrelateRecordList?, position: Int) {


        val tv_bankNumber = getView<TextView>(R.id.tv_bankNumber)

        tv_bankNumber.text = t!!.bankAccount


        holder.rootView.setOnClickListener{

            if (onItemClickListner != null){
                onItemClickListner.onItemClickListener(it,t,position)
            }


        }

    }
}