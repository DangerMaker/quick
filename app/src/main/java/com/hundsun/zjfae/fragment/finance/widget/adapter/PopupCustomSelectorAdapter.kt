package com.hundsun.zjfae.fragment.finance.widget.adapter

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
import com.hundsun.zjfae.fragment.finance.bean.ConditionEntity

class PopupCustomSelectorAdapter(
    private val context: Context,
    private val list: MutableList<ConditionEntity>,
    private var selectMap: MutableMap<Int, Boolean> = mutableMapOf()
) :
    RecyclerView.Adapter<PopupCustomSelectorViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PopupCustomSelectorViewHolder {


        val layoutInflater = LayoutInflater.from(context)

        val rootView =
            layoutInflater.inflate(R.layout.item_transfer_amount_selsect_layout, p0, false)


        return PopupCustomSelectorViewHolder(rootView)

    }

    override fun onBindViewHolder(viewHolder: PopupCustomSelectorViewHolder, position: Int) {

        viewHolder.tvContent.text = list[position].conditionName

        viewHolder.tvContent.isSelected = selectMap[position] == true

        viewHolder.itemLayout.isSelected = selectMap[position] == true

        if (selectMap[position] == true) {

            viewHolder.iv_content_icon.visibility = View.VISIBLE


        } else {

            viewHolder.iv_content_icon.visibility = View.GONE
        }

        viewHolder.itemLayout.setOnClickListener {

            if (!selectMap.containsKey(position)) {

                selectMap[position] = true

            } else {

                selectMap[position] = selectMap[position] != true
            }
            notifyDataSetChanged()

        }


    }

    override fun getItemCount(): Int {


        return list.size
    }

//    private var saveSelectMapData = mutableMapOf<Int, Boolean>()
//
//    fun setSaveSelectMapData(map: MutableMap<Int, Boolean>) {
//        saveSelectMapData = HashMap(map)
//    }


    fun setSelectMap(map: MutableMap<Int, Boolean>) {

        selectMap = HashMap(map)
        notifyDataSetChanged()
    }


    fun getSelectMap(): MutableMap<Int, Boolean> {

        return selectMap
    }


    fun getSelectUUidValue(): MutableList<String> {

        val mutableList = mutableListOf<String>()

        selectMap.forEach {

            if (it.value) {

                mutableList.add(list[it.key].uuid)
            }

        }


        return mutableList
    }


    fun getSelectName(): MutableList<String> {

        val mutableList = mutableListOf<String>()

        selectMap.forEach {
            if (it.value) {
                mutableList.add(list[it.key].conditionName)
            }
        }

        return mutableList

    }


}


class PopupCustomSelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val itemLayout = itemView.findViewById<LinearLayout>(R.id.item_layout)

    val tvContent = itemView.findViewById<TextView>(R.id.tv_content)

    val iv_content_icon = itemView.findViewById<ImageView>(R.id.iv_content_icon)

}