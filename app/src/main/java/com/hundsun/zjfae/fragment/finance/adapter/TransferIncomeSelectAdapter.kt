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
import com.hundsun.zjfae.fragment.finance.bean.ConditionEntity
import com.hundsun.zjfae.fragment.finance.bean.TransferIncomeSelectEntity
import com.hundsun.zjfae.fragment.finance.bean.TransferIncomeNewSelectEntity
import onight.zjfae.afront.AllAzjProto

class TransferIncomeSelectAdapter(
    private val context: Context,
    private val list: MutableList<ConditionEntity> = mutableListOf(),
    //从列表返回之后，用户修改排序条件后进行返显的数据封装类
    private var selectStatusEntity: TransferIncomeNewSelectEntity?
) :
    RecyclerView.Adapter<TransferIncomeSelectViewHolder>() {

    private val selectMapStatus = mutableMapOf<Int, Boolean>()


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TransferIncomeSelectViewHolder {

        val layoutInflater = LayoutInflater.from(context)

        val rootView =
            layoutInflater.inflate(R.layout.item_transfer_amount_selsect_layout, p0, false)


        return TransferIncomeSelectViewHolder(rootView)
    }

    override fun onBindViewHolder(viewHolder: TransferIncomeSelectViewHolder, position: Int) {

        viewHolder.tvContent.text = list[position].conditionName

        /**
         * 选中的名字
         * */
        var selectConditionName = ""

        /**
         * 升序或降序
         * */
        var controlSort = ""

        selectStatusEntity?.let {

            selectConditionName = it.selectConditionName

            controlSort = it.controlSort

        }

        //说明转让列表界面有修改过排序条件
        if (selectConditionName != "" && controlSort != "") {

            if (list[position].conditionName.contains(selectConditionName) && list[position].controlSort == controlSort) {

                viewHolder.tvContent.isSelected = true

                viewHolder.itemLayout.isSelected = true

                viewHolder.iv_content_icon.visibility = View.VISIBLE
                selectMapStatus.clear()
                selectMapStatus[position] = true
            } else {

                viewHolder.tvContent.isSelected = false

                viewHolder.itemLayout.isSelected = false

                viewHolder.iv_content_icon.visibility = View.GONE
            }

        } else {

            viewHolder.tvContent.isSelected = selectMapStatus[position] == true

            viewHolder.itemLayout.isSelected = selectMapStatus[position] == true

            if (selectMapStatus[position] == true) {

                viewHolder.iv_content_icon.visibility = View.VISIBLE


            } else {

                viewHolder.iv_content_icon.visibility = View.GONE
            }

        }


        viewHolder.itemLayout.setOnClickListener {
            selectStatusEntity = null
            if (!selectMapStatus.containsKey(position)) {
                selectMapStatus.clear()
                selectMapStatus[position] = true

            } else {
                //selectMapStatus.clear()
                selectMapStatus[position] = selectMapStatus[position] != true

            }

            notifyDataSetChanged()

        }

    }

    override fun getItemCount(): Int {

        return list.size
    }


    /**
     * 获取所有选中的条件
     * */
    fun getSelectList(): MutableList<String> {

        val mutableList = mutableListOf<String>()

        selectMapStatus.forEach {

            if (it.value) {

                mutableList.add(list[it.key].conditionName)
            }

        }

        Log.i("gson1", Gson().toJson(mutableList))
        return mutableList
    }


    fun getSelectUUidValue(): MutableList<String> {

        val mutableList = mutableListOf<String>()


        selectMapStatus.forEach {

            if (it.value) {
                list[it.key].controlSort
                mutableList.add(list[it.key].uuid)
            }

        }

        Log.i("gsonValue", Gson().toJson(mutableList))

        return mutableList
    }


    /**
     * 清除所有条件选择
     * */
    fun cleanSelectCondition() {
        selectStatusEntity = null
        selectMapStatus.clear()
        notifyDataSetChanged()

    }


    /**
     * 从列表返回之后，用户修改排序条件后进行返显的数据封装类
     * */
    fun setSelectStatusEntity(entity: TransferIncomeNewSelectEntity) {
        selectStatusEntity = null
        selectMapStatus.clear()
        selectStatusEntity = entity
        notifyDataSetChanged()

    }


    //选中的下标
    fun getSelectPosition() {

        var position = 0

        selectMapStatus.forEach {

            position = it.key


        }


    }

    /**
     * 获取排序选择条件
     * */
    fun getIncomeSelectEntity(): TransferIncomeSelectEntity {


        val selectName = mutableListOf<String>()

        val selectUUID = mutableListOf<String>()

        var position = -1

        var controlSort = ""

        Log.i("mapStatus", "${selectMapStatus.size}")
        selectMapStatus.forEach {

            if (it.value) {
                position = it.key
                controlSort = list[position].controlSort
                selectName.add(list[position].conditionName)

                selectUUID.add(list[position].uuid)
            }


        }
        val entity = TransferIncomeSelectEntity()

        entity.selectPosition = position

        entity.controlSort = controlSort

        entity.selectName = selectName

        entity.selectUUID = selectUUID


        Log.i("筛选条件", Gson().toJson(entity))


        return entity
    }

}


class TransferIncomeSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val itemLayout = itemView.findViewById<LinearLayout>(R.id.item_layout)

    val tvContent = itemView.findViewById<TextView>(R.id.tv_content)

    val iv_content_icon = itemView.findViewById<ImageView>(R.id.iv_content_icon)

}