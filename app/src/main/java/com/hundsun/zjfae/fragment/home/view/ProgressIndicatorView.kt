package com.hundsun.zjfae.fragment.home.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.hundsun.zjfae.R


class ProgressIndicatorView : View {



    private  var mPaint: Paint


    private var leftX = 0f

    private var topY = 10f

    private var rightX = 54f

    private var bottomY = 20f


    private var defaultWidth = 54




    constructor(context: Context) : super(context){

    }


    init {

        mPaint = Paint()

        mPaint.strokeWidth = 10f

        mPaint.style = Paint.Style.FILL

        mPaint.strokeCap = Paint.Cap.ROUND

        mPaint.color = context.resources.getColor(R.color.colorRed)

         //screenWidth = (getScreenWidth(context) / 2).toFloat()


       // leftX = screenWidth-defaultWidth




    }


    override fun onDraw(canvas: Canvas?) {


        var rect = RectF(leftX,topY,rightX,bottomY)

        canvas!!.drawRect(rect,mPaint)

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


        setMeasuredDimension(defaultWidth,heightMeasureSize)
    }



    protected fun getScreenWidth(context : Context) : Int{

        val dm = DisplayMetrics()

        val vm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        vm.defaultDisplay.getMetrics(dm)


        return dm.widthPixels

    }


}