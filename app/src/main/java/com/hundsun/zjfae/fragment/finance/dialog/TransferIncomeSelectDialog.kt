package com.hundsun.zjfae.fragment.finance.dialog

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.view.dialog.BaseBottomSheetDialog
import com.hundsun.zjfae.fragment.finance.adapter.TransferIncomeSelectAdapter
import com.hundsun.zjfae.fragment.finance.bean.ConditionEntity
import com.hundsun.zjfae.fragment.finance.bean.TransferIncomeNewSelectEntity
import onight.zjfae.afront.AllAzjProto

/**
 * ClassName:      TransferIncomeSelectDialog
 * Description:    受让后参考收益率弹框
 * Author:         moran
 * CreateDate:     2021/5/13 13:43
 * UpdateUser:     更新者：moran
 * UpdateDate:     2021/5/13 13:43
 * UpdateRemark:   更新说明：
 * Version:        1.0
 */
class TransferIncomeSelectDialog(val context: Context) : BaseBottomSheetDialog(context) {

    //数据List
    private val transferSort: MutableList<ConditionEntity> = mutableListOf()

    /**
     * 回调事件
     * */
    var onItemIncomeSelectEntityListener: OnItemIncomeSelectEntityListener? = null

    //recyclerView适配器
    private var adapter: TransferIncomeSelectAdapter? = null


    private val selectTransferIncomeNewSelectEntity = TransferIncomeNewSelectEntity()

    var selectTypeName = ""

    /**
     * 初始化view
     * @date: 2021/5/13 13:45
     * @author: moran
     * @param rootView 根布局
     */
    override fun initView(rootView: View) {

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler)

        val tvSelectTypeName = rootView.findViewById<TextView>(R.id.tv_select_type_name)

        tvSelectTypeName.text = selectTypeName

        adapter =
            TransferIncomeSelectAdapter(context, transferSort, selectTransferIncomeNewSelectEntity)

        recyclerView.adapter = adapter


        rootView.findViewById<Button>(R.id.bt_commit).setOnClickListener {

            onDismissDialog()

            adapter?.let {

                val entity = it.getIncomeSelectEntity()

                if (entity.selectName.isNotEmpty()) {

                    selectTransferIncomeNewSelectEntity.controlSort = entity.controlSort
                    selectTransferIncomeNewSelectEntity.selectConditionName = entity.selectName[0]
                }

                else {
                    selectTransferIncomeNewSelectEntity.selectConditionName = ""
                    selectTransferIncomeNewSelectEntity.controlSort = ""
                }

                onItemIncomeSelectEntityListener?.onItemIncomeSelectEntity(
                    entity
                )


            }

        }
    }

    /**
     * @param transferSortList 筛选条件
     * 设置筛选条件
     * */
    fun setTransferDefaultSortList(transferSortList: MutableList<ConditionEntity>) {

        transferSort.clear()

        transferSort.addAll(transferSortList)

    }

    /**
     * 设置返显信息
     * */
    fun setSelectStatusEntity(entity: TransferIncomeNewSelectEntity) {

        selectTransferIncomeNewSelectEntity.controlSort = entity.controlSort

        selectTransferIncomeNewSelectEntity.selectConditionName = entity.selectConditionName



    }

    override fun showDialog() {
        adapter?.setSelectStatusEntity(selectTransferIncomeNewSelectEntity)
        super.showDialog()
    }


    /**
     * 清除所有条件选择
     * */
    fun cleanSelectCondition() {

        selectTransferIncomeNewSelectEntity.controlSort = ""

        selectTransferIncomeNewSelectEntity.selectConditionName = ""

        adapter?.cleanSelectCondition()


    }

    /**
     * 返回布局id
     * @date: 2021/5/13 13:45
     * @author: moran
     * @return 布局id
     */
    override fun getLayoutId(): Int {

        return R.layout.dialog_transfer_condition_layout
    }


}