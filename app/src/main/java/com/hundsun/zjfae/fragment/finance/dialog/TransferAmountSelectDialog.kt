package com.hundsun.zjfae.fragment.finance.dialog

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.utils.CCLog
import com.hundsun.zjfae.common.view.dialog.BaseBottomSheetDialog
import com.hundsun.zjfae.fragment.finance.adapter.TransferConditionSelectAdapter
import com.hundsun.zjfae.fragment.finance.bean.ConditionEntity
import onight.zjfae.afront.AllAzjProto.PBAPPSearchSortControl_l2


/**
 * ClassName:      TransferAmountSelectDialog
 * Description:    选择起投金额与选择剩余期限弹框
 * Author:         moran
 * CreateDate:     2021/5/13 13:44
 * UpdateUser:     更新者：moran
 * UpdateDate:     2021/5/13 13:44
 * UpdateRemark:   更新说明：
 * @param context 上下文对象
 * Version:        1.0
 */
class TransferAmountSelectDialog(val context: Context) : BaseBottomSheetDialog(context) {
    //数据List
    private val transferSort: MutableList<ConditionEntity> = mutableListOf()

    /**
     * 回调事件
     * */
    var onItemClickListener: OnItemSelectListener? = null


    //recyclerView适配器
    private var adapter: TransferConditionSelectAdapter? = null


    private var mapStatus = mutableMapOf<Int, Boolean>()

    var selectTypeName = ""

    override fun initView(rootView: View) {

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler)

        val tvSelectTypeName = rootView.findViewById<TextView>(R.id.tv_select_type_name)

        tvSelectTypeName.text = selectTypeName

        adapter = TransferConditionSelectAdapter(context, transferSort)


        recyclerView.adapter = adapter

        rootView.findViewById<Button>(R.id.bt_commit).setOnClickListener {
            onDismissDialog()

            adapter?.let {

                val selectList = it.getSelectName()

                mapStatus = HashMap(it.getSelectMap())

                onItemClickListener?.onSelectItem(
                    selectList,
                    getSelectUUidValue(),
                    it.getSelectMap()
                )
            }
        }

    }


    private fun getSelectUUidValue(): String {

        var uuids = "-"

        adapter?.let {
            val stringBuilder = StringBuilder()
            if (!it.getSelectUUidValue().isNullOrEmpty()) {
                it.getSelectUUidValue().forEach {
                    stringBuilder.append(it)
                    stringBuilder.append("|")
                }
                val uuidValue = stringBuilder.toString()

                uuids = uuidValue.substring(0, uuidValue.length - 1)


            }
            // mutableList.addAll(it.getSelectUUidValue())
        }


        return uuids
    }


    /**
     * @param transferSortList 筛选条件
     * 设置筛选条件
     * */
    fun setTransferDefaultSortList(transferSortList: MutableList<ConditionEntity>) {

        transferSort.clear()

        transferSort.addAll(transferSortList)

    }

    override fun showDialog() {
        CCLog.i("展开的mapStatus", Gson().toJson(mapStatus))
        super.showDialog()
        adapter?.setSelectMap(mapStatus)
    }


    fun setSelectMap(map: MutableMap<Int, Boolean>) {
        this.mapStatus = HashMap(map)

    }

    /**
     * 清除所有条件选择
     * */
    fun cleanSelectCondition() {

        mapStatus.clear()

        adapter?.cleanSelectCondition()

    }


    override fun getLayoutId(): Int {

        return R.layout.dialog_transfer_condition_layout
    }


}