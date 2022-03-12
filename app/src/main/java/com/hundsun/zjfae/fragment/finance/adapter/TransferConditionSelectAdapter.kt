package com.hundsun.zjfae.fragment.finance.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.utils.CCLog
import com.hundsun.zjfae.fragment.finance.bean.ConditionEntity
import onight.zjfae.afront.AllAzjProto

class TransferConditionSelectAdapter(
    private val context: Context,
    private val list: MutableList<ConditionEntity> = mutableListOf()
) :
    RecyclerView.Adapter<TransferSelectViewHolder>() {


    /**
    * 点击选择的条件
    * */
    private var mapStatus = mutableMapOf<Int, Boolean>()


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TransferSelectViewHolder {


        val layoutInflater = LayoutInflater.from(context)

        val rootView =
            layoutInflater.inflate(R.layout.item_transfer_amount_selsect_layout, p0, false)


        return TransferSelectViewHolder(rootView)
    }

    override fun onBindViewHolder(viewHolder: TransferSelectViewHolder, position: Int) {


        viewHolder.tvContent.text = list[position].conditionName

        viewHolder.tvContent.isSelected = mapStatus[position] == true

        viewHolder.itemLayout.isSelected = mapStatus[position] == true

        if (mapStatus[position] == true) {

            viewHolder.iv_content_icon.visibility = View.VISIBLE


        } else {

            viewHolder.iv_content_icon.visibility = View.GONE
        }


        viewHolder.itemLayout.setOnClickListener {

            if (!mapStatus.containsKey(position)) {

                mapStatus[position] = true


            } else {

                mapStatus[position] = mapStatus[position] != true
            }
            notifyDataSetChanged()

        }


    }


//    /**
//     * 获取所有选中的条件名字
//     * */
//    fun getSelectList(): MutableList<String> {
//
//        val mutableList = mutableListOf<String>()
//
//        mapStatus.forEach {
//
//            if (it.value) {
//
//                mutableList.add(list[it.key].conditionName)
//            }
//
//        }
//
//        Log.i("gsonName", Gson().toJson(mutableList))
//        return mutableList
//    }


    /**
     * 获取选中条件map状态
     * */
    fun getSelectUUidValue(): MutableList<String> {

        val mutableList = mutableListOf<String>()


        mapStatus.forEach {

            if (it.value) {

                mutableList.add(list[it.key].uuid)
            }

        }

        Log.i("gsonUUidValue", Gson().toJson(mutableList))

        return mutableList
    }


    /**
    * 获取选中条件名字集合
    * */
    fun getSelectName(): MutableList<String> {
        val mutableList = mutableListOf<String>()
        mapStatus.forEach {
            if (it.value) {
                mutableList.add(list[it.key].conditionName)
            }

        }

        Log.i("gsonSelectName", Gson().toJson(mutableList))

        return mutableList

    }



    fun getSelectMap(): MutableMap<Int, Boolean> {

        return mapStatus
    }


    fun setSelectMap(selectMap: MutableMap<Int, Boolean>) {
        CCLog.i("adapter的mapStatus", Gson().toJson(selectMap))
        this.mapStatus.clear()
        this.mapStatus = HashMap(selectMap)


        notifyDataSetChanged()
    }


    /**
     * 清除所有条件选择
     * */
    fun cleanSelectCondition() {

        mapStatus.clear()

        notifyDataSetChanged()

    }


    override fun getItemCount(): Int {


        return list.size
    }


}


class TransferSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val itemLayout = itemView.findViewById<LinearLayout>(R.id.item_layout)

    val tvContent = itemView.findViewById<TextView>(R.id.tv_content)

    val iv_content_icon = itemView.findViewById<ImageView>(R.id.iv_content_icon)

}




