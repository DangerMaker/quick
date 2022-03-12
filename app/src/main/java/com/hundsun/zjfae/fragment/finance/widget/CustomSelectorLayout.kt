package com.hundsun.zjfae.fragment.finance.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.fragment.finance.bean.ConditionEntity
import com.hundsun.zjfae.fragment.finance.dialog.OnItemSelectListener
import com.hundsun.zjfae.fragment.finance.dialog.TransferAmountSelectDialog
import kotlin.properties.Delegates

class CustomSelectorLayout : LinearLayout, View.OnClickListener {
    private var dialog: TransferAmountSelectDialog
    private var list = mutableListOf<ConditionEntity>()//popupWindow条件列表数据

    private var text_title: TextView by Delegates.notNull()

    private var layout_selector: LinearLayout by Delegates.notNull()

    private var img_index: ImageView by Delegates.notNull()

    var defaultName = ""


    constructor(context: Context, attributeSet: AttributeSet) : this(
        context,
        attributeSet,
        0
    )


    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {

        View.inflate(context, R.layout.layout_custom_selector, this)

        text_title = findViewById(R.id.text_title)

        layout_selector = findViewById(R.id.layout_selector)

        img_index = findViewById(R.id.img_index)
        val typedArrays = context.obtainStyledAttributes(attributeSet, R.styleable.CustomSelector)
        //根据自定义属性，为筛选添加文本
        text_title.text = typedArrays.getString(R.styleable.CustomSelector_selector_text)
        dialog = TransferAmountSelectDialog(context)
        dialog.onItemClickListener = object : OnItemSelectListener {

            override fun onSelectItem(
                conditionNameList: MutableList<String>,
                uuidValue: String,
                selectMap: MutableMap<Int, Boolean>
            ) {
                selectConditionListener?.onItemOnClick(
                    mutableListOf(uuidValue),
                    selectMap,
                    conditionNameList
                )
            }

        }
        layout_selector.isEnabled =
            typedArrays.getBoolean(R.styleable.CustomSelector_selector_enable, true)
        layout_selector.setOnClickListener(this)

    }


    private var selectConditionListener: SelectConditionListener? = null

    fun setSelectConditionListener(conditionListener: SelectConditionListener) {


        selectConditionListener = conditionListener
    }

    /**
     * 设置选择器是否可用
     */
    fun setSelectorEnable(enable: Boolean) {
        layout_selector.isEnabled = enable
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_selector -> {

                dialog.showDialog()

            }
        }
    }

    /**
     * 设置popwindow的显示状态
     * （如果当前显示，则执行关闭；如果当前关闭，则执行显示）
     */
//    private fun setPopupWindowStatus() {
//        img_index.isSelected = true
//        if (this.popupWindow.isShowing) {
//            this.popupWindow.dismiss()
//        } else {
//            this.popupWindow.show()
//            //条件列表展开，指示图片旋转
//            //rotateExpandIcon(img_index, 0f, 180f)
//        }
//    }

    fun setSelectText(strValue: String) {
        text_title.text = strValue
        text_title.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
    }

    fun setSelectText(selectName: MutableList<String>) {
        setSelectInfo(selectName)


    }

    /**
     * 更新popupWindow条件列表数据
     */
    fun updateSelectorData(
        list: MutableList<ConditionEntity>,
        selectMap: MutableMap<Int, Boolean>
    ) {

        this.list.clear()
        // this.list.add(text_title.text.toString())
        this.list.addAll(list)
        dialog.selectTypeName = "请选择${defaultName}"
        dialog.setTransferDefaultSortList(list)
        dialog.setSelectMap(selectMap)
        //popupWindow.setSelectorData(this.list, selectMap)
    }

    /**
     * 设置标题文字
     */
    fun setTitleText(title: String) {
        text_title.text = title
        text_title.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
    }


    /**
     * 设置选中状态和未选中状态下的文字，颜色及图标
     * */
    private fun setSelectInfo(selectName: MutableList<String>) {
        img_index.isSelected = false
        //选中的筛选名字集合不为空
        if (selectName.isNotEmpty()) {
            val stringBuilder = StringBuilder()
            selectName.forEach {
                stringBuilder.append(it).append("、")
            }
            img_index.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.popup_transfer_condition_select_text_shape
                )
            )
            text_title.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            val conditionNameStr =
                stringBuilder.toString().substring(0, stringBuilder.toString().length - 1)
            text_title.text = conditionNameStr

        } else {
            img_index.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.popup_transfer_condition_text_default_shape
                )
            )
            text_title.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            val conditionNameStr = defaultName
            text_title.text = conditionNameStr
        }
    }

}


/**
 * 监听关闭PopupWindow回调
 */
interface PopupDismissListener {

    fun onDismiss(selectName: MutableList<String>)


}