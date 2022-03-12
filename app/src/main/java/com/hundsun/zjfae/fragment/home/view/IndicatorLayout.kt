package com.hundsun.zjfae.fragment.home.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout


class IndicatorLayout : RelativeLayout {


    private val indicatorView: IndicatorView


    private val progressIndicatorView: ProgressIndicatorView


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    init {

        indicatorView = IndicatorView(context)

        progressIndicatorView = ProgressIndicatorView(context)

        addView(indicatorView)

        addView(progressIndicatorView)
    }



    fun getScaleWidth(): Int {

        Log.e("TAG,indicatorView宽度", indicatorView.measuredWidth.toString())

        Log.e("TAG,progressIndicator宽度",progressIndicatorView.measuredWidth.toString())

        return indicatorView.measuredWidth - progressIndicatorView.measuredWidth

    }

    fun moveView(moveX : Float){

        progressIndicatorView.translationX = moveX

    }


}