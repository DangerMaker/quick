package com.hundsun.zjfae.fragment.finance.widget

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.google.gson.Gson
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.utils.CCLog
import com.hundsun.zjfae.fragment.finance.bean.ConditionEntity
import com.hundsun.zjfae.fragment.finance.widget.adapter.PopupCustomSelectorAdapter

class CustomSelectorPopupWindow(
    private var context: Context,
    private var anchorView: LinearLayout
) : PopupWindow(
    context
), View.OnClickListener {
    private var list: MutableList<ConditionEntity> = mutableListOf() //筛选条件框列表数据

    private var adapter: PopupCustomSelectorAdapter? = null

    var selectConditionListener: SelectConditionListener? = null


    init {
        //initPopup()
        initView()
    }

    private var saveSelectMapData = mutableMapOf<Int, Boolean>()

    private fun initView() {

        val rootView =
            LayoutInflater.from(context).inflate(R.layout.popup_custom_selector, null, false)
        rootView.findViewById<View>(R.id.bt_commit).setOnClickListener(this)

        val recyclerSelector = rootView.findViewById<RecyclerView>(R.id.recycler_selector)

        adapter = PopupCustomSelectorAdapter(context, list)
        recyclerSelector.adapter = adapter
        contentView = rootView
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ContextCompat.getDrawable(context, android.R.color.white))
        this.setOnDismissListener {

            Log.i("TAG", "onDismiss")

        }

        update()

    }


    /**
     * 设置筛选条件列表数据
     */
    fun setSelectorData(list: MutableList<ConditionEntity>, map: MutableMap<Int, Boolean>) {
        this.list.clear()
        this.list.addAll(list)
        saveSelectMapData = HashMap(map)
    }

    fun show() {
        if (!isShowing) {
            adapter?.setSelectMap(saveSelectMapData)
            showAsDropDown(anchorView)
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_commit -> {
                dismiss()
                adapter?.let {
                    saveSelectMapData = HashMap(it.getSelectMap())
                }


                Log.i("gson", "确认点击")
                //点击确定的时候把selectMap重新赋值
                selectConditionListener?.onItemOnClick(
                    getSelectUUidValue(),
                    getSelectMap(),
                    getSelectName()
                )
            }
        }
    }



    private fun getSelectName(): MutableList<String> {

        val mutableList = mutableListOf<String>()
        saveSelectMapData.forEach {
            if (it.value) {
                mutableList.add(list[it.key].conditionName)
            }
        }
        Log.i("gsonNameValue", Gson().toJson(mutableList))
        return mutableList

    }



    fun getSelectMap(): MutableMap<Int, Boolean> {

        return saveSelectMapData
    }


    fun getSelectUUidValue(): MutableList<String> {

        val mutableList = mutableListOf<String>()

        saveSelectMapData.forEach {

            if (it.value) {

                mutableList.add(list[it.key].uuid)
            }

        }

        Log.i("gsonUUidValue", Gson().toJson(mutableList))

        return mutableList
    }

    private fun backgroundAlpha(alpha: Float) {

        if (context is Activity) {
            val lp: WindowManager.LayoutParams = (context as Activity).getWindow()
                .attributes
            // lp.alpha = 0.6f

            lp.alpha = alpha
            (context as Activity).window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            (context as Activity).window.attributes = lp
        }
    }


}


/**
 * 选择条件回调事件
 */
interface SelectConditionListener {

    /**
     * @描述一下方法的作用
     * @method
     * @date: 2021/5/19 19:46
     * @author: moran
     * @param uuidList 选中条件的uuid集合
     * @param selectMap 选中条件状态map集合
     * @param conditionNameList 选中条件名字集合
     */
    fun onItemOnClick(
        uuidList: MutableList<String>,
        selectMap: MutableMap<Int, Boolean>,
        conditionNameList: MutableList<String>
    )


}


