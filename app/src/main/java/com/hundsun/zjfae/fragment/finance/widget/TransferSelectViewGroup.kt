package com.hundsun.zjfae.fragment.finance.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.provider.CalendarContract
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.hundsun.zjfae.R

class TransferSelectViewGroup : ViewGroup {


    //当控件高度不指定时，使用默认40f高度
    private val defaultHeight = 40f

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //计算出所有的child宽度和高度
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        //不判断widthMode的类型，布局文件宽度是match_parent，所以直接取widthSize的值
        //val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        Log.i("widthSize", "$widthSize")

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        Log.i("heightSize", "$heightSize")

        val viewHeight =
            if (heightMode == MeasureSpec.AT_MOST) dip2px(defaultHeight) else heightSize

        setMeasuredDimension(widthSize, viewHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        // 子View大于1
        if (childCount > 1) {

            //最后一个子View
            val lastChildView = getChildAt(childCount - 1)

            //除去最后一个子view的宽度
            val availableWidth = width - lastChildView.measuredWidth

            //中间子控件总宽度
            var centerChildViewWidthCount = 0
            // 布局开始左边界
            var centerChildViewLeft = 0

            //从第二个子view开始遍历
            // 例如有4个View  0 1 2 3
            for (index in 0 until childCount - 1) {

                // 获取View
                val centerChildView = getChildAt(index)
                // 每循环一次 计算一下宽度
                centerChildViewWidthCount += centerChildView.measuredWidth

                // 当子View总宽度小于View的宽度，直接布局
                if (centerChildViewWidthCount  <= width) {

                        val centerChildViewWidth = centerChildView.measuredWidth
                        val centerChildViewHeight = centerChildView.measuredHeight

                        // 计算出往后布局的最后一个View的右边界
                        val centerChildViewRight = centerChildViewLeft + centerChildViewWidth
                        // 得到当前View的上边界  例如总高度50 View高度20 那么上边界为 （50-20）/2 = 15
                        val centerChildViewTop = (height - centerChildViewHeight) / 2
                        // 计算当前View的下边界
                        val centerChildViewBottom = centerChildViewTop + centerChildViewHeight
                        // 布局该View
                        centerChildView.layout(
                            centerChildViewLeft,
                            centerChildViewTop,
                            centerChildViewRight,
                            centerChildViewBottom
                        )
                        // 每循环布局完毕一次，就重置左边界为新布局完毕最后一个View的右边界
                        centerChildViewLeft = centerChildViewRight

                } else {

                    // 子View的宽度大于父布局的宽度的情况

                    // 得到最后一个View的高度
                    val lastChildViewHeight = lastChildView.measuredHeight

                    // 最后一个子view左起点
                    val lastChildViewLeft = centerChildViewLeft
                    // 得到最后一个子View的上边界
                    val lastChildViewTop = ((height - lastChildViewHeight) / 2)

                    Log.i("lastChildViewTop", "$lastChildViewTop")

                    // 直接让最后一个子View的右边界等于宽度 也就是不让其出界
                    val lastChildViewRight = width
                    // 得到最后一个子View的下边界
                    val lastChildViewBottom = lastChildViewTop + lastChildViewHeight

                    // 布局最后一个子View
                    lastChildView.layout(
                        lastChildViewLeft,
                        lastChildViewTop,
                        lastChildViewRight,
                        lastChildViewBottom
                    )

                    break
                }
                Log.i("centerWidthCount", "$centerChildViewWidthCount")

            }
        } else {
            // 只有一个子View 请选择起投金额/请选择剩余期限
            val childView = getChildAt(0)
            val childViewWidth = childView.measuredWidth
            val childViewHeight = childView.measuredHeight

            // 直接布局
            val left = 0
            val top = ((height - childViewHeight) / 2)
            val right = left + childViewWidth
            val bottom = top + childViewHeight
            childView.layout(left, top, right, bottom)

        }

    }

    /**
     * 往布局中添加子View
     */
    fun addChildView(mutableList: MutableList<String>, defaultName : String = "") {
        removeAllViews()

        if (mutableList.isEmpty()) {

            addDefaultView(defaultName)
        }
        else {
            mutableList.forEach { value ->

                Log.i("选择的item", value)

                val mInflater = LayoutInflater.from(context)

                val rootView = mInflater.inflate(R.layout.widget_text_layout, null)

                val textView = rootView.findViewById<TextView>(R.id.tv_content)

                textView.text = value

                addView(rootView)

            }

            val mInflater = LayoutInflater.from(context)

            val rootView = mInflater.inflate(R.layout.widget_text_layout, null)

            val textView = rootView.findViewById<TextView>(R.id.tv_content)

            textView.text = "..."

            addView(rootView)
        }
    }


    fun addDefaultView(contentStr: String) {

        removeAllViews()

        val mInflater = LayoutInflater.from(context)

        val rootView = mInflater.inflate(R.layout.transfer_default_text_layout, null)

        val textView = rootView.findViewById<TextView>(R.id.tv_content_str)

        textView.text = contentStr

        addView(rootView)

    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
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