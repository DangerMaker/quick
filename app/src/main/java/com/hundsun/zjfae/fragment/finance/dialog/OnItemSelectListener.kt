package com.hundsun.zjfae.fragment.finance.dialog

import com.hundsun.zjfae.fragment.finance.bean.TransferIncomeSelectEntity


/**
 * ClassName:      OnItemSelectListener
 * Description:    点击确认回调事件
 * Author:         moran
 * CreateDate:     2021/5/13 10:00
 * UpdateUser:     更新者：moran
 * UpdateDate:     2021/5/13 10:00
 * UpdateRemark:   更新说明：
 * Version:        1.0
 */
interface OnItemSelectListener {

    /**
     * 选择条件集合回调
     * @date: 2021/5/13 13:47
     * @author: moran
     * @param mutableList 选择的条件集合
     * @param uuidValue 多个条件拼接的uuid
     * @param selectMap 选择条件的map集合
     * @return
     */
    fun onSelectItem(
        mutableList: MutableList<String>,
        uuidValue: String,
        selectMap: MutableMap<Int, Boolean>
    )

}


interface OnItemIncomeSelectEntityListener {

    /**
     * 筛选条件排序
     * @date: 2021/5/13 18:22
     * @author: moran
     * @param entityTransfer 筛选条件信息封装类
     */
    fun onItemIncomeSelectEntity(entityTransfer: TransferIncomeSelectEntity)

}