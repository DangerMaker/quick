package com.hundsun.zjfae.fragment.home.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.hundsun.zjfae.R

class IndicatorView : View {



    private  var mPaint:Paint


    private var leftX = 0f

    private var topY = 10f

    private var rightX = 108f

    private var bottomY = 20f

    //44

    private var defaultWidth = 108f


    constructor(context: Context) : super(context){

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var widthMeasure = measuredWidth

        var heightMeasure = measuredHeight


        var widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec)

        var widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)


        var heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec)

        var heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec)


        when(heightMeasureMode){

            MeasureSpec.AT_MOST ->

                heightMeasureSize = 20

        }


        setMeasuredDimension(defaultWidth.toInt(),heightMeasureSize)
    }

    init {

        mPaint = Paint()

        mPaint.strokeWidth = 10f

        mPaint.style = Paint.Style.FILL
        mPaint.strokeCap = Paint.Cap.ROUND

        mPaint.color = context.resources.getColor(R.color.product_color)

        //屏幕的2分之一




    }


    override fun onDraw(canvas: Canvas?) {


            var rect = RectF(leftX,topY,rightX,bottomY)

            canvas!!.drawRect(rect,mPaint)


    }


    protected fun getScreenWidth(context : Context) : Int{

        val dm = DisplayMetrics()

        val vm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        vm.defaultDisplay.getMetrics(dm)

        Log.e("TAG，屏幕宽度",dm.widthPixels.toString())

        return dm.widthPixels

    }






}