package com.hundsun.zjfae.fragment.finance.widget

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.hundsun.zjfae.R
import com.hundsun.zjfae.fragment.finance.bean.TransferIncome
import com.hundsun.zjfae.fragment.finance.bean.TransferIncomeNewSelectEntity
import com.hundsun.zjfae.fragment.finance.bean.TransferIncomeSelectEntity
import java.util.*

class ConditionLinearLayout : LinearLayout {

    //当控件高度不指定时，使用默认40f高度
    private val defaultHeight = 50f

    constructor(context: Context) : super(context) {
        orientation = HORIZONTAL
    }


    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        orientation = HORIZONTAL
    }


    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        orientation = HORIZONTAL
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val viewHeight =
            if (heightMode == MeasureSpec.AT_MOST) dip2px(defaultHeight) else heightSize



        setMeasuredDimension(widthSize, viewHeight)
    }


    private val selectImageList = mutableListOf<ImageView>()
    private var selectName = ""
    private var controlSort = ""
    private var selectUUID = ""

    fun addConditionView(
        transferIncomeList: MutableList<TransferIncome>,
        selectEntity: TransferIncomeSelectEntity?,
        onChildClickListener: ConditionLinearLayoutChildClickListener
    ) {


        selectEntity?.let {

            controlSort = it.controlSort
            if (!it.selectName.isNullOrEmpty()) {
                selectName = it.selectName[0]
                selectUUID = it.selectUUID[0]



                Log.i("selectName", selectName)

                Log.i("controlSort", controlSort)

                Log.i("selectUUID", selectUUID)
            }
        }



        if (!transferIncomeList.isNullOrEmpty()) {

            for (index in 0 until transferIncomeList.size) {

                val controlNname = transferIncomeList[index].controlNname
                val rootView = LayoutInflater.from(context)
                    .inflate(R.layout.widget_transfer_condition_layout, null)

                val textView: TextView = rootView.findViewById(R.id.tv_condition_content)

                val imageView: ImageView = rootView.findViewById(R.id.iv_select)
                selectImageList.add(imageView)

                textView.text = controlNname

                if (selectName.contains(controlNname)) {

                    //是否是升序,0是升序，1是降序
                    //0最低
//                    if (controlSort == "0") {
//
//                        setChildViewSelectStatus(index)
//                    }
                    // 1最高
                    setChildViewSelectStatus(index, controlSort)

                }

                rootView.tag = index

                rootView.setOnClickListener {

                    Log.i("tag", "${it.tag}")

                    val controlSortUUIDMap = transferIncomeList[index].controlSortUUIDMap

                    val index = it.tag as Int


                    var selectUUid = ""

                    if (selectImageList[index].isSelected) {
                        controlSort = "1"
                        //取1
                        selectUUid = "${controlSortUUIDMap["1"]}"
                    } else {
                        //取0
                        controlSort = "0"
                        selectUUid = "${controlSortUUIDMap["0"]}"
                    }
                    val tv = it.findViewById<TextView>(R.id.tv_condition_content)
                    selectName = tv.text.toString()
                    setChildViewSelectStatus(index)

                    onChildClickListener.onChildClick(selectUUid, selectName)

                }


                val layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f)

                rootView.layoutParams = layoutParams

                addView(rootView)

                if (index in 0 until transferIncomeList.size - 1) {
                    val lineView = View(context)
                    val lineLayoutParams =
                        LayoutParams(dip2px(1f), LayoutParams.MATCH_PARENT)
                    lineView.setBackgroundColor(Color.parseColor("#DDDDDD"))
                    lineLayoutParams.setMargins(dip2px(5f), 0, dip2px(5f), 0)
                    lineView.layoutParams = lineLayoutParams
                    addView(lineView)
                }
            }

        }


    }


    /**
     * 描述一下方法的作用
     * @date: 2021/5/14 13:47
     * @author: moran
     * @param position 选择子view的下标
     * @param controlSort 升序或降序
     *
     */
    private fun setChildViewSelectStatus(
        position: Int,
        controlSort: String = ""
    ) {

        for (index in 0 until selectImageList.size) {

            if (position == index) {

                //0是最低
                when (controlSort) {
                    "0" -> {

                        selectImageList[index].setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.widget_transfer_condition_select_iv_selector
                            )
                        )
                        selectImageList[index].isSelected = true
                    }
                    //1是最高
                    "1" -> {

                        selectImageList[index].setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.widget_transfer_condition_select_iv_selector
                            )
                        )
                        selectImageList[index].isSelected = false
                    }
                    else -> {
                        selectImageList[index].setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.widget_transfer_condition_select_iv_selector
                            )
                        )
                        selectImageList[index].isSelected = !selectImageList[index].isSelected
                    }
                }


            } else {
                selectImageList[index].setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.widget_transfer_condition_iv_selector
                    )
                )
                selectImageList[index].isSelected = false
            }
        }

    }


    /**
     * 点击更新新的筛选信息
     * */
    fun getTransferIncomeNewSelectEntity(): String {

        val entity = TransferIncomeNewSelectEntity()
        entity.selectConditionName = selectName
        entity.controlSort = controlSort
        entity.selectUUID = selectUUID

        selectName = selectName.replace("最高", "").trim()

        selectName = selectName.replace("最低", "").trim()

        if (selectName != "") {

            //最低
            if (controlSort == "0") {

                entity.selectConditionName = "${selectName}最低"
            }
            //最高
            else if (controlSort == "1") {

                entity.selectConditionName = "${selectName}最高"


            }
        }


        return Gson().toJson(entity)
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private fun px2dip(pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

}


interface ConditionLinearLayoutChildClickListener {

    /**
     * @date: 2021/5/14 13:42
     * @author: moran
     * @param uuid 用户点击选择的条件
     * @param selectConditionName 选择条件的name
     */
    fun onChildClick(uuid: String, selectConditionName: String)

}